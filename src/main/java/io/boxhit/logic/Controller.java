package io.boxhit.logic;

public class Controller {

    private static GameInstanceHandler gameInstanceHandler;
    private static PlayerInstanceHandler playerInstanceHandler;

    private static GameProtectionHandler gameProtectionHandler;

    public static void start(){
        gameInstanceHandler = new GameInstanceHandler();
        playerInstanceHandler = new PlayerInstanceHandler();
        gameProtectionHandler = new GameProtectionHandler();
    }

    public static GameInstanceHandler getGameInstanceHandler(){
        return gameInstanceHandler;
    }

    public static PlayerInstanceHandler getPlayerInstanceHandler(){
        return playerInstanceHandler;
    }

    public static GameProtectionHandler getGameProtectionHandler(){
        return gameProtectionHandler;
    }

}
