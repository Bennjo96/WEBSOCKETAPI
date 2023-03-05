package io.boxhit.logic.subject;

import io.boxhit.socket.messages.MessageModule;
import io.boxhit.socket.messages.MessageSender;
import io.boxhit.socket.messages.MessageTemplates;
import io.boxhit.socket.messages.OutputMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Game {
    private final int gameID;
    private ArrayList<Player> players;
    private final String gameName;

    private boolean isRunning;

    public final int MAX_PLAYERS = 5;
    public final int MAP_SIZE = 380;

    public Game(int gameID, boolean forceStart, String name) {
        this.gameID = gameID;
        this.players = new ArrayList<>();
        this.isRunning = forceStart;
        this.gameName = name;
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
        return players;
    }

    /**
     * Get the game's canvas
     * @return the game's canvas
     */
    private void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * remove a Player from the game
     * @param player the player to remove
     */
    private void removePlayer(Player player) {
        players.remove(player);
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
        for (Player player : players) {
            if (player.getState() == Player.State.PLAYING) addPlayer(player);
        }
        return alivePlayers;
    }

    /**
     * gets all dead players
     * @return the dead players
     */
    public ArrayList<Player> getDeadPlayers() {
        ArrayList<Player> deadPlayers = new ArrayList<>();
        for (Player player : players) {
               if (player.getState() == Player.State.DEAD) addPlayer(player);
        }
        return deadPlayers;
    }

    /**
     * gets all players in the attack-range
     * @param playerID the playerID
     * @return the players in the radius
     */
    public ArrayList<Player> getPlayersInAttackRadiusOfPlayer(String playerID) {
        ArrayList<Player> playersInRadius = new ArrayList<>();
        for (Player player : players) {
            if (player.getPlayerID() == playerID) continue;
            if (player.getState() == Player.State.DEAD) continue;
            if (Math.sqrt(Math.pow(player.getPositionX() - getPlayerByID(playerID).getPositionX(), 2) + Math.pow(player.getPositionY() - getPlayerByID(playerID).getPositionY(), 2)) <= player.ATTACK_RADIUS) {
                playersInRadius.add(player);
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
        for (Player player : players) {
            if (player.getPlayerID() == playerID) {
                return player;
            }
        }
        return null;
    }

    public void leaveGame(Player player){
        despawnPlayer(player, (player.getState() == Player.State.WAITING));
        removePlayer(player);
    }

    public void despawnPlayer(Player player, boolean die){
        if(player.getState() == Player.State.PLAYING){
            if(die) player.setState(Player.State.DEAD);
            player.setPositionX(-1);
            player.setPositionY(-1);
            //broadcastGameEventExcludePlayer(MessageModule.ACTION_DESPAWN_PLAYER, player.getPlayerID(), player);
        }
    }

    /**
     * spawns a player
     * @param player the player
     */
    private void spawnPlayer(Player player) {
        if(randomPlayerPosition(player)){
            String data = "";

            broadcastGameEventExcludePlayer(MessageModule.ACTION_JOIN_GAME_OTHER, data, player);

        }
    }

    /**
     * sets a random position for a player
     * @param player the player
     */
    private boolean randomPlayerPosition(Player player){
        player.setPositionX((int) (Math.random() * MAP_SIZE));
        player.setPositionY((int) (Math.random() * MAP_SIZE));
        return true;
    }

    public void startGame(){
        //change player state to playing
        for(Player player : players){
            if(player.getState() == Player.State.WAITING) player.setState(Player.State.PLAYING);
        }
    }

    public boolean requestPlayerJoinGame(Player player){
        if(isRunning) return false;
        if(players.size() >= MAX_PLAYERS) return false;
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
            jsonObject.put("x", p.getPositionX());
            jsonObject.put("y", p.getPositionY());
        }

        player.setState(Player.State.WAITING);

        return gameData.toString();
    }

    public void broadcastGameEvent(MessageModule messageModule, String json){
        OutputMessage outputMessage = new OutputMessage().setModule(messageModule.getModule());
        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
        outputMessage.setJson(json);

        for(Player player : players){
            String id = player.getPlayerID();
            MessageSender ms = new MessageSender();
            ms.send(outputMessage,id);
        }
    }

    public void broadcastGameEventExcludePlayer(MessageModule messageModule, String json, Player player){
        OutputMessage outputMessage = new OutputMessage().setModule(messageModule.getModule());
        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
        outputMessage.setJson(json);

        for(Player p : players){
            if(p.getPlayerID() == player.getPlayerID()) continue;
            String id = p.getPlayerID();
            MessageSender ms = new MessageSender();
            ms.send(outputMessage,id);
        }
    }
}
