package io.boxhit.logic;

import io.boxhit.logic.subject.Game;

import java.util.ArrayList;
import java.util.Timer;

public class GameInstanceHandler {

    /**
     * The games
     */
    private ArrayList<Game> games;

    /**
     * The GameInstanceHandler constructor
     */
    public GameInstanceHandler() {
        this.games = new ArrayList<>();
        games.add(new Game(1, true));
        games.add(new Game(2, false));
        games.add(new Game(3, false));
        games.add(new Game(4, false));
        games.add(new Game(5, false));
        games.add(new Game(6, false));
        startGameInstanceTimer();
    }

    /**
     * Get the game with the given id
     * @param gameID the game's id
     * @return the game with the given id or null if not found
     */
    public Game getGame(int gameID){
        for (Game game : games) {
            if (game.getGameID() == gameID) return game;
        }
        return null;
    }

    /**
     * start the 1 minute Timer to handle GameInstances
     */
    private void startGameInstanceTimer(){
        new Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        update();
                    }
                },
                0,
                60 * 1000
        );
    }


    /**
     * All updates every 1 minute
     */
    private void update(){

    }
}
