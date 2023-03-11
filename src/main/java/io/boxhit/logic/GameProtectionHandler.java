package io.boxhit.logic;

import io.boxhit.logic.subject.Player;

import java.util.HashMap;

public class GameProtectionHandler {

    HashMap<Player, Long> lastDisconnect = new HashMap<>();

    public void setLastDisconnect(String playerId, Long time){
        Player player = Controller.getPlayerInstanceHandler().getPlayer(playerId);
        if(player == null) return;
        lastDisconnect.put(player, time);
    }

    public boolean isAllowedToJoin(String playerId) {
        Player player = Controller.getPlayerInstanceHandler().getPlayer(playerId);
        if(player == null) return false;
        if (lastDisconnect.containsKey(player)) {
            if (lastDisconnect.get(player) + 3000 > System.currentTimeMillis()) {
                return false;
            }
        }
        return true;
    }

}
