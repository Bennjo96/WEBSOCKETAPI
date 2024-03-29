package io.boxhit.logic;

import io.boxhit.logic.score.Score;
import io.boxhit.logic.subject.Game;
import io.boxhit.logic.subject.Player;
import io.boxhit.socket.messages.MessageModule;
import jakarta.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class GameInstanceHandler {
    private HashMap<Integer, Game> games;

    //Data for client request
    private Long lastRequestFromClient = 0L;
    //Data for client request
    private int dotLoadingCounter = 0;


    /**
     * The GameInstanceHandler constructor
     */
    public GameInstanceHandler() {
        this.games = new HashMap<>();
        games.put(1, new Game(1, false, "Server 1"));
        games.put(2, new Game(2, false, "Server 2"));
        games.put(3, new Game(3, false, "Server 3"));
        games.put(4, new Game(4, false, "Server 4"));
        games.put(5, new Game(5, false, "Server 5"));
        games.put(6, new Game(6, false, "Server 6"));
    }

    /**
     * Get the game with the given id
     * @param gameID the game's id
     * @return the game with the given id or null if not found
     */
    @Nullable
    public Game getGame(int gameID){
        if(games.containsKey(gameID)) return games.get(gameID);
        return null;
    }

    public HashMap<Integer, Game> getGames() {
        return games;
    }

    public void startGame(int gameID){
        Game game = getGame(gameID);
        if(game != null && game.getPlayers().size() > 1 && !game.isRunning()){
            game.startGame();
            game.setRunning(true);
        }
    }

    public void endGame(int gameID){
        Game game = getGame(gameID);
        if(game != null && game.isRunning()){
            ArrayList<Player> players = game.getAlivePlayers();
            boolean winScore = false;
            if(players.size() == 1) winScore = true;
            String winnerId = "";

            for(Player winner : players){
                if(winScore) winner.addScore(Score.WIN.getScore());
                winnerId = winner.getPlayerID();

                JSONObject dataInfo = new JSONObject();
                JSONArray arrayInfo = new JSONArray();

                JSONObject obj = new JSONObject();
                obj.put("playerID", winner.getPlayerID());
                obj.put("score", winner.getScore());
                obj.put("health", winner.getHealth());
                arrayInfo.put(obj);
                dataInfo.put("players", arrayInfo);
                game.broadcastGameEvent(MessageModule.ACTION_GAME_SCORE_HEALTH, dataInfo.toString());

            }

            JSONObject jsonObject = new JSONObject();
            if(winScore) {
                jsonObject.put("playerId", winnerId);
                game.broadcastGameEvent(MessageModule.ACTION_GAME_PLAYER_WON, jsonObject.toString());
            }else{
                jsonObject.put("playerId", "NULL");
                game.broadcastGameEvent(MessageModule.ACTION_GAME_PLAYER_WON, jsonObject.toString());
            }
        }

        //timer for 10 seconds
        Timer timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                resetGame(gameID);
            }
        }, 10000);

    }

    public void resetGame(int gameID){
        Game game = getGame(gameID);
        if(game != null){
            game.broadcastGameEvent(MessageModule.ACTION_LEAVE_FORCED, new JSONObject().toString());


            Timer timer = new Timer();
            timer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    game.setRunning(false);
                }
            }, 1000);

        }
    }


    public boolean requestPlayerJoinGame(String playerId, int gameID){
        Game game = getGame(gameID);
        if(game != null){
            Player player = Controller.getPlayerInstanceHandler().getPlayer(playerId);
            if(player == null) return false;
            if (game.getPlayers().contains(player)) return false;
            player.setCurrentGameID(gameID);
            return game.requestPlayerJoinGame(player);
        }
        return false;
    }

    public String playerRetrieveGameData(Player player, int gameID){
        Game game = getGame(gameID);
        if(game != null) return game.retrieveGameData(player);
        return "{}";
    }

    public void movePlayer(String id, String direction){
        Player player = Controller.getPlayerInstanceHandler().getPlayer(id);
        if(player == null) return;
        Game game = getGame(player.getCurrentGameID());
        if(game == null) return;
        int x = 0;
        int y = 0;
        switch (direction){
            case "up":
                y = -30;
                break;
            case "down":
                y = 30;
                break;
            case "left":
                x = -30;
                break;
            case "right":
                x = 30;
                break;
        }
        JSONObject data = new JSONObject();
        data.put("x", x);
        data.put("y", y);
        data.put("playerID", id);
        player.move(x, y);
        System.out.println("Player moved to " + player.getPositionX() + " " + player.getPositionY());
        game.broadcastGameEvent(MessageModule.ACTION_GAME_MOVE_OTHER, data.toString());
        Controller.getGameLogicHandler().checkPlayerMovement(player, game);
    }


    /**
     * Generating response for client
     * @return json
     */
    public String getGameListJson(){
        if(System.currentTimeMillis() - lastRequestFromClient > 1000) {
            lastRequestFromClient = System.currentTimeMillis();
            dotLoadingCounter++;
            if (dotLoadingCounter > 3) dotLoadingCounter = 0;
        }
        String dots = "";
        for (int i = 0; i < dotLoadingCounter; i++) {
            dots += ".";
        }
        //fill dots with spaces
        for (int i = 0; i < 3 - dotLoadingCounter; i++) {
            dots += " ";
        }
        String emojiController = new String(Character.toChars(0x1F3AE));
        String emojiSandglass = new String(Character.toChars(0x23F3));
        String json = "[";
        //"{id: 1, name: \"Test\", info: \"game info\", players: \"0/5\"}";
        for (int i : games.keySet()) {
            Game game = games.get(i);
            String info = (game.isRunning()) ? emojiController + " Game is running"+dots : emojiSandglass+" Waiting for players" + dots;
            json += "{id: " + game.getGameID() + ", name: \"" + game.getGameName() + "\", info: \"" + info + "\", players: \"" + game.getPlayers().size() + "/"+game.MAX_PLAYERS+"\"},";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    public void leaveGame(Player player, int gameID) {
        Game game = getGame(gameID);
        if(game != null){
            game.leaveGame(player);
        }
    }
}
