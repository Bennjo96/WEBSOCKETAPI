package io.boxhit.logic;

import io.boxhit.logic.subject.Player;

import java.util.HashMap;

public class GameProtectionHandler {

    HashMap<Player, Long> lastDisconnect = new HashMap<>();
    private HashMap<String, Long> lastMove = new HashMap<>();
    private HashMap<String, Long> lastAttack = new HashMap<>();

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

    public boolean isAllowedToMove(String playerId){
        if(lastMove.containsKey(playerId)){
            if(lastMove.get(playerId) + 420 > System.currentTimeMillis()){
                return false;
            }
        }
        return Controller.getPlayerInstanceHandler().getPlayer(playerId).getState() == Player.State.PLAYING;
    }

    public void setLastMove(String playerId, Long time){
        lastMove.put(playerId, time);
    }

    public boolean isAllowedToAttack(String playerId){
        if(lastAttack.containsKey(playerId)){
            if(lastAttack.get(playerId) + 5000 > System.currentTimeMillis()){
                return false;
            }
        }
        return true;
    }

    public void setLastAttack(String playerId, Long time){
        lastAttack.put(playerId, time);
    }



}
