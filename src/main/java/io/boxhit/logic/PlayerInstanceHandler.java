package io.boxhit.logic;

import io.boxhit.logic.subject.Game;
import io.boxhit.logic.subject.Player;

import java.util.ArrayList;

public class PlayerInstanceHandler {

    private ArrayList<Player> players = new ArrayList<>();

    private void registerPlayer(Player player) {
        players.add(player);
    }

    private void unregisterPlayer(Player player) {
        players.remove(player);
    }

    public boolean prepareRegisterPlayer(String playerID, String name, String hexColor) {
        if(getPlayer(playerID) != null) return false;
        Player player = new Player(playerID, name, hexColor);
        player.setState(Player.State.IDLE);
        registerPlayer(player);
        return true;
    }

    public boolean prepareUnregisterPlayer(String playerID) {
        Player player = getPlayer(playerID);
        if(player == null) return false;

        int gameId = player.getCurrentGameID();
        if(gameId != -1) {
            Controller.getGameInstanceHandler().leaveGame(player, gameId);
        }

        unregisterPlayer(player);
        return true;
    }

    public Player getPlayer(String playerID) {
        for (Player player : players) {
            if (player.getPlayerID().equals(playerID)) {
                return player;
            }
        }
        return null;
    }



}
