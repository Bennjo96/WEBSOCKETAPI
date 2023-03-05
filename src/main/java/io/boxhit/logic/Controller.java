package io.boxhit.logic;

public class Controller {

    private static GameInstanceHandler gameInstanceHandler;
    private static PlayerInstanceHandler playerInstanceHandler;

    public static void start(){
        gameInstanceHandler = new GameInstanceHandler();
        playerInstanceHandler = new PlayerInstanceHandler();
    }

    public static GameInstanceHandler getGameInstanceHandler(){
        return gameInstanceHandler;
    }

    public static PlayerInstanceHandler getPlayerInstanceHandler(){
        return playerInstanceHandler;
    }

}
