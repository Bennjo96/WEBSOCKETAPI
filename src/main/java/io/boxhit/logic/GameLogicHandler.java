package io.boxhit.logic;

import io.boxhit.logic.score.Score;
import io.boxhit.logic.subject.Game;
import io.boxhit.logic.subject.Player;
import io.boxhit.socket.messages.MessageModule;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameLogicHandler {

    private final int MAP_SIZE = 350;

    public void checkPlayerMovement(Player player, Game game){
        if(player.getState() == Player.State.PLAYING){
            int x = player.getPositionX();
            int y = player.getPositionY();
            System.out.println("Checking player positions: "+player.getName()+ " x:"+x+" y:"+y);
            if(x < -3 || x > MAP_SIZE+3 || y < -3 || y > MAP_SIZE+3){
                player.setState(Player.State.DEAD);
                player.addScore(Score.DEATH.getScore());

                JSONObject dataInfo = new JSONObject();
                JSONArray arrayInfo = new JSONArray();

                JSONObject obj = new JSONObject();
                obj.put("playerID", player.getPlayerID());
                obj.put("score", player.getScore());
                obj.put("health", player.getHealth());
                arrayInfo.put(obj);
                dataInfo.put("players", arrayInfo);
                game.broadcastGameEvent(MessageModule.ACTION_GAME_SCORE_HEALTH, dataInfo.toString());

                game.playerDies(player);
            }

        }
    }
}
