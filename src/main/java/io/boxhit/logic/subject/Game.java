package io.boxhit.logic.subject;

import java.util.ArrayList;

public class Game {

    /**
     * The game's id
     */
    private final int gameID;
    /**
     * The game's players
     */
    private ArrayList<Player> players;
    /**
     * The game's mapsize
     */
    private final int map_size;

    private boolean isRunning;

    public Game(int gameID, boolean forceStart) {
        this.gameID = gameID;
        this.players = new ArrayList<>();
        this.map_size = 650;
        this.isRunning = forceStart;
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
     * Get the game's mapsize
     * @return the game's mapsize
     */
    public int getMap_size() {
        return map_size;
    }

    /**
     * Get the game's canvas
     * @return the game's canvas
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * remove a Player from the game
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
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

    /**
     * gets all alive players
     * @return the alive players
     */
    public ArrayList<Player> getAlivePlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isAlive()) {
                alivePlayers.add(player);
            }
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
            if (!player.isAlive()) {
                deadPlayers.add(player);
            }
        }
        return deadPlayers;
    }

    /**
     * gets all players in the attack-range
     * @param playerID the playerID
     * @return the players in the radius
     */
    public ArrayList<Player> getPlayersInAttackRadiusOfPlayer(int playerID) {
        ArrayList<Player> playersInRadius = new ArrayList<>();
        for (Player player : players) {
            if (player.getPlayerID() == playerID) continue;
            if (player.isAlive()) {
                if (Math.sqrt(Math.pow(player.getPositionX() - getPlayerByID(playerID).getPositionX(), 2) + Math.pow(player.getPositionY() - getPlayerByID(playerID).getPositionY(), 2)) <= player.ATTACK_RADIUS) {
                    playersInRadius.add(player);
                }
            }
        }
        return playersInRadius;
    }

    /**
     * gets a player by its playerID
     * @param playerID the playerID
     * @return the player
     */
    public Player getPlayerByID(int playerID) {
        for (Player player : players) {
            if (player.getPlayerID() == playerID) {
                return player;
            }
        }
        return null;
    }

    /**
     * spawns a player
     * @param playerID the playerID
     * @param name the name
     * @param color the color
     */
    public void spawnPlayer(int playerID, String name, String color) {
        Player player = new Player(playerID, name, color);
        randomPlayerPosition(player);
        players.add(player);
        //TODO: finish or is it even needed?
    }

    /**
     * sets a random position for a player
     * @param player the player
     */
    public void randomPlayerPosition(Player player){
        //TODO: finish or is it even needed?
    }
}
