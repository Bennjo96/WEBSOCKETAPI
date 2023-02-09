package io.boxhit.logic;

import io.boxhit.logic.score.Score;
import io.boxhit.logic.subject.Player;

public class GameLogicHandler {

    /**
     * Add Score to Player
     * @param player the player
     * @param score the specific score
     */
    private static void addScore(Player player, Score score) {
        player.setScore(player.getScore() + score.getScore());
    }
}
