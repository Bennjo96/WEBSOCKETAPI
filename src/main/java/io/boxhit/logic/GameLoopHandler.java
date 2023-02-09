package io.boxhit.logic;

import io.boxhit.logic.subject.Player;

import java.util.Timer;

public class GameLoopHandler {

    /**
     * Start the GameLoop (1 sec repeating Timer calling UpdateMethod)
     */
    public static void startGameLoop(){
        new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        update();
                    }
                },
                0,
                1000
        );
    }

    /**
     * All updates every 1 sec
     */
    private static void update(){

    }

    /**
     * Sec repeating if the Player can Attack
     * @param player the player
     */
    public void checkCooldownForAttack(Player player){
        if (player.getCoolDownForAttack() == 0) return;
        player.setCoolDownForAttack(player.getCoolDownForAttack() - 1);
    }

    /**
     * handles the Attack of the player
     * @param player the player
     */
    public void handleAttackingPlayer(Player player){
        if (player.getCoolDownForAttack() == 0) {
            player.setCoolDownForAttack(8);
        }
    }
}
