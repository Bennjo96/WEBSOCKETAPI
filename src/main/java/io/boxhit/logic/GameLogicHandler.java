package io.boxhit.logic;

import io.boxhit.logic.score.Score;
import io.boxhit.logic.subject.Player;

public class GameLogicHandler {

    /**
     * Add or Remove Score to/from Player
     * @param player the player
     * @param score the specific score
     */
    private void modifyScore(Player player, Score score) {
        player.setScore(player.getScore() + score.getScore());
    }

    public void checkPlayerMovement(Player player){
        if(player.getState() == Player.State.PLAYING){

        }
    }
}
