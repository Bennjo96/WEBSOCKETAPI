package io.boxhit.logic.subject;

import io.boxhit.logic.Controller;
import io.boxhit.logic.GameInstanceHandler;
import io.boxhit.logic.score.Score;
import io.boxhit.socket.messages.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private final int gameID;
    private ConcurrentHashMap<String, Player> players;
    private final String gameName;

    private boolean isRunning;

    public final int MAX_PLAYERS = 8;
    public final int MAP_SIZE = 350;

    public String infoMessage = "Waiting for players...";
    public int waitUntilStart = 60;

    public int maxPlayersInGame = 0;

    private Timer timer;

    public Game(int gameID, boolean forceStart, String name) {
        this.gameID = gameID;
        this.players = new ConcurrentHashMap<>();
        this.isRunning = forceStart;
        this.gameName = name;
        this.maxPlayersInGame = 0;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondlyCalled();
            }
        }, 0, 1000);
    }

    /**
     * Get the game's id
     * @return the game's id
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Get the game's players
     * @return the game's players
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    /**
     * Get the game's canvas
     * @return the game's canvas
     */
    private void addPlayer(Player player) {
        players.put(player.getPlayerID(), player);
    }

    /**
     * remove a Player from the game
     * @param player the player to remove
     */
    private void removePlayer(Player player) {
        JSONObject data = new JSONObject();
        data.put("playerID", player.getPlayerID());
        broadcastGameEventExcludePlayer(MessageModule.ACTION_LEAVE_GAME_OTHER, data.toString(), player);
        players.remove(player.getPlayerID());
    }

    /**
     * checks if the game is running
     * @return if the game is running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * sets the game running state
     * @param running the running state
     */
    public void setRunning(boolean running) {
        isRunning = running;
        maxPlayersInGame = players.size();
        updateInfoMessage();
    }

    public String getGameName() {
        return gameName;
    }

    /**
     * gets all alive players
     * @return the alive players
     */
    public ArrayList<Player> getAlivePlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.getState() == Player.State.PLAYING) alivePlayers.add(player);
        }
        System.out.println("Alive players: " + alivePlayers.size());
        return alivePlayers;
    }

    public void executePlayerAttack(Player player){

        if(player.getState() != Player.State.PLAYING) return;

        HashMap<Player, Vector2D> players = getPlayersInAttackRadiusOfPlayer(player);
        JSONObject dataInfo = new JSONObject();
        JSONArray arrayInfo = new JSONArray();

        ArrayList<Player> dyingPlayers = new ArrayList<>();

        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        for(Player p : players.keySet()){
            Controller.getGameProtectionHandler().setLastMove(p.getPlayerID(), System.currentTimeMillis()+440);
            JSONObject obj = new JSONObject();
            obj.put("playerID", p.getPlayerID());
            int x = (int) players.get(p).getX(); //read from hashmap
            int y = (int) players.get(p).getY(); //read from hashmap
            obj.put("x", x);
            obj.put("y", y);
            if(players.get(p).hard){
                p.setHealth(p.getHealth() - 1);

                if(p.getHealth() <= 0){
                    p.setState(Player.State.DEAD);
                    p.removeScore(Score.DEATH.getScore());
                    p.setHealth(0);
                    dyingPlayers.add(p);
                }

                JSONObject objDamage = new JSONObject();
                objDamage.put("playerID", p.getPlayerID());
                objDamage.put("health", p.getHealth());
                objDamage.put("score", p.getScore());
                arrayInfo.put(objDamage);
            }
            array.put(obj);
            p.move(x, y);
            Controller.getGameLogicHandler().checkPlayerMovement(p, this);
        }
        player.addScore(players.size());

        data.put("players", array);
        data.put("attacker", player.getPlayerID());
        data.put("attackerScore", player.getScore());
        broadcastGameEvent(MessageModule.ACTION_GAME_ATTACK_OTHER, data.toString());

        dataInfo.put("players", arrayInfo);
        broadcastGameEvent(MessageModule.ACTION_GAME_SCORE_HEALTH, dataInfo.toString());

        JSONObject dataInfo1 = new JSONObject();
        JSONArray arrayInfo1 = new JSONArray();

        JSONObject obj = new JSONObject();
        obj.put("playerID", player.getPlayerID());
        obj.put("score", player.getScore());
        obj.put("health", player.getHealth());
        arrayInfo1.put(obj);
        dataInfo1.put("players", arrayInfo1);
        broadcastGameEvent(MessageModule.ACTION_GAME_SCORE_HEALTH, dataInfo1.toString());

        for(Player p : dyingPlayers){
            playerDies(p);
        }
    }

    public void playerDies(Player p){
        JSONObject obj = new JSONObject();
        obj.put("playerID", p.getPlayerID());
        broadcastGameEvent(MessageModule.ACTION_GAME_PLAYER_DIED, obj.toString());

        if(!isRunning()) return;
        ArrayList<Player> alivePlayers = getAlivePlayers();
        if(alivePlayers.size() == 1){
            System.out.println("Game " + gameID + " has only one player left. Ending game...");
            Controller.getGameInstanceHandler().endGame(gameID);
        }else if(alivePlayers.size() == 0){
            System.out.println("Game " + gameID + " has no players left. Resetting game...");
            Controller.getGameInstanceHandler().resetGame(gameID);
        }
    }

    /**
     * gets all dead players
     * @return the dead players
     */
    public ArrayList<Player> getDeadPlayers() {
        ArrayList<Player> deadPlayers = new ArrayList<>();
        for (Player player : players.values()) {
               if (player.getState() == Player.State.DEAD) addPlayer(player);
        }
        return deadPlayers;
    }

    /**
     * gets all players in the attack-range
     * @param player the playerID
     * @return the players in the radius
     */
    public HashMap<Player, Vector2D> getPlayersInAttackRadiusOfPlayer(Player player) {
        HashMap<Player, Vector2D> playersInRadius = new HashMap();
        Vector2D v1 = new Vector2D(player.getPositionX(), player.getPositionY());
        for (Player p : players.values()) {
            if (p.getPlayerID() == player.getPlayerID()) continue;
            if (p.getState() == Player.State.DEAD) continue;
            Vector2D v2 = new Vector2D(p.getPositionX(), p.getPositionY());
            double distance = v1.distance(v2);
            System.out.println("Distance: " + distance);
            if (distance <= p.ATTACK_RADIUS) {
                Vector2D v3 = v1.subtract(v2);
                System.out.println("V3: "+v3.toString());
                //v3.normalize();
                //System.out.println("V3: "+v3.toString());

                if (distance > p.ATTACK_RADIUS * 0.9){
                    v3 = v3.mul(-0.1);
                }else if (distance > p.ATTACK_RADIUS * 0.8) {
                    v3 = v3.mul(-0.3);
                }else if (distance > p.ATTACK_RADIUS * 0.7) {
                    v3 = v3.mul(-0.5).setHard(true);
                }else if (distance > p.ATTACK_RADIUS * 0.6) {
                    v3 = v3.mul(-0.7).setHard(true);
                }else if (distance > p.ATTACK_RADIUS * 0.5) {
                    v3 = v3.mul(-0.9).setHard(true);
                }else if(distance > p.ATTACK_RADIUS * 0.4) {
                    v3 = v3.mul(-1.1).setHard(true);
                }else if(distance > p.ATTACK_RADIUS * 0.3) {
                    v3 = v3.mul(-1.3).setHard(true);
                }else {
                    v3 = v3.mul(-1.5).setHard(true);
                }
                System.out.println("V3: "+v3.toString());
                playersInRadius.put(p, v3);
            }
        }
        return playersInRadius;
    }

    /**
     * gets a player by its playerID
     * @param playerID the playerID
     * @return the player
     */
    public Player getPlayerByID(String playerID) {
        for (Player player : players.values()) {
            if (player.getPlayerID() == playerID) {
                return player;
            }
        }
        return null;
    }

    public void leaveGame(Player player){
        despawnPlayer(player, (player.getState() == Player.State.WAITING));
        removePlayer(player);
        updateInfoMessage();

        if(!isRunning()) return;
        ArrayList<Player> alivePlayers = getAlivePlayers();
        if(alivePlayers.size() == 1){
            System.out.println("Game " + gameID + " has only one player left. Ending game...");
            Controller.getGameInstanceHandler().endGame(gameID);
        }else if(alivePlayers.size() == 0){
            System.out.println("Game " + gameID + " has no players left. Resetting game...");
            Controller.getGameInstanceHandler().resetGame(gameID);
        }
    }

    public void despawnPlayer(Player player, boolean die){
        if(player.getState() == Player.State.PLAYING){
            if(die) player.setState(Player.State.DEAD);
            player.setPositionX(-100);
            player.setPositionY(-100);
            //broadcastGameEventExcludePlayer(MessageModule.ACTION_DESPAWN_PLAYER, player.getPlayerID(), player);
        }
    }

    /**
     * spawns a player
     * @param player the player
     */
    private void spawnPlayer(Player player) {
        if(randomPlayerPosition(player)){
            player.setHealth(Player.MAX_HEALTH);

            JSONObject data = new JSONObject();
            data.put("playerID", player.getPlayerID());
            data.put("playerName", player.getName());
            data.put("playerPosX", player.getPositionX());
            data.put("playerPosY", player.getPositionY());
            data.put("playerColor", player.getHexColor());

            broadcastGameEventExcludePlayer(MessageModule.ACTION_JOIN_GAME_OTHER, data.toString(), player);
        }
    }

    /**
     * sets a random position for a player
     * @param player the player
     */
    private boolean randomPlayerPosition(Player player){
        player.setPositionX((int) (Math.random() * (MAP_SIZE-60))+30);
        player.setPositionY((int) (Math.random() * (MAP_SIZE-60))+30);
        return true;
    }

    public void startGame(){
        //change player state to playing
        for(Player player : players.values()){
            player.setHealth(Player.MAX_HEALTH);
            if(player.getState() == Player.State.WAITING) player.setState(Player.State.PLAYING);
        }

    }

    public boolean requestPlayerJoinGame(Player player){
        if(isRunning) return false;
        if(players.size() >= MAX_PLAYERS) return false;
        player.setScore(0);
        player.setState(Player.State.JOINING);
        addPlayer(player);
        spawnPlayer(player);
        return true;
    }

    public String retrieveGameData(Player player){
        JSONObject gameData = new JSONObject();
        gameData.put("gameID", getGameID());
        gameData.put("gameName", getGameName());
        gameData.put("mapSize", MAP_SIZE);

        JSONArray players = new JSONArray();

        for(Player p : getPlayers()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerID", p.getPlayerID());
            jsonObject.put("playerName", p.getName());
            jsonObject.put("playerPosX", p.getPositionX());
            jsonObject.put("playerPosY", p.getPositionY());
            jsonObject.put("playerColor", p.getHexColor());
            jsonObject.put("yourself", p.getPlayerID() == player.getPlayerID());
            players.put(jsonObject);
        }

        gameData.put("players", players);

        player.setState(Player.State.WAITING);
        updateInfoMessage();

        return gameData.toString();
    }

    public void broadcastGameEvent(MessageModule messageModule, String json){
        OutputMessage outputMessage = new OutputMessage().setModule(messageModule.getModule());
        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
        outputMessage.setJson(json);
        if(!outputMessage.toString().contains("gameInfoMessage")){
            System.out.println("Outgoing: BROADCAST "+outputMessage.toString());
        }

        for(Player player : players.values()){
            String id = player.getPlayerID();
            MessageSender ms = new MessageSender();
            ms.send(outputMessage,id);
        }
    }

    public void broadcastGameEventExcludePlayer(MessageModule messageModule, String json, Player player){
        OutputMessage outputMessage = new OutputMessage().setModule(messageModule.getModule());
        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
        outputMessage.setJson(json);
        System.out.println("Outgoing: BROADCAST (EX) "+outputMessage.toString());
        for(Player p : players.values()){
            if(p.getPlayerID() == player.getPlayerID()) continue;
            String id = p.getPlayerID();
            MessageSender ms = new MessageSender();
            ms.send(outputMessage,id);
        }
    }

    private void updateInfoMessage(){
        if (isRunning) {
            infoMessage = "";
        } else {
            switch (players.size()) {
                case 0:
                case 1:
                    infoMessage = "Waiting for players...";
                    waitUntilStart = 60;
                    break;
                case 2:
                    infoMessage = "Waiting for players...";
                    if(waitUntilStart > 60) {
                        waitUntilStart = 60;
                    }
                    break;
                case 3:
                    infoMessage = "Waiting for players...";
                    if(waitUntilStart > 30) {
                        waitUntilStart = 30;
                    }
                    break;
                case 4:
                    infoMessage = "Waiting for players...";
                    if(waitUntilStart > 20) {
                        waitUntilStart = 20;
                    }
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    infoMessage = "Waiting for start...";
                    if(waitUntilStart > 10) {
                        waitUntilStart = 10;
                    }
                    break;
            }
        }
    }

    private synchronized void secondlyCalled(){
        if(isRunning) waitUntilStart = -1;
        else{
            if(players.size()>1) waitUntilStart--;
            if(waitUntilStart == 0){
                Controller.getGameInstanceHandler().startGame(this.gameID);
            }
        }

        //if (isRunning){
        //    if (getAlivePlayers().size() == 1){
        //        winner(getAlivePlayers().get(0));
        //    }
        //}


        //send game Info Message
        JSONObject data = new JSONObject();
        String message = infoMessage+" "+waitUntilStart;
        data.put("message", message.replace("-1",""));

        broadcastGameEvent(MessageModule.ACTION_GAME_INFO_MESSAGE, data.toString());
    }
}
