package io.boxhit.logic;

import io.boxhit.logic.subject.Game;
import io.boxhit.socket.database.playlog.PlayLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class Controller {

    private static GameInstanceHandler gameInstanceHandler;
    private static PlayerInstanceHandler playerInstanceHandler;
    private static GameProtectionHandler gameProtectionHandler;
    private static GameLogicHandler gameLogicHandler;

    private static Long lastServerChecked = 0L;

    public static void start(){
        gameInstanceHandler = new GameInstanceHandler();
        playerInstanceHandler = new PlayerInstanceHandler();
        gameProtectionHandler = new GameProtectionHandler();
        gameLogicHandler = new GameLogicHandler();
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

    public static GameLogicHandler getGameLogicHandler(){
        return gameLogicHandler;
    }

    public static void checkServer(){
        //check all 10 seconds
        if(System.currentTimeMillis() - lastServerChecked > 10000){
            lastServerChecked = System.currentTimeMillis();

            for(Game game : gameInstanceHandler.getGames().values()){
                if(game.isRunning()){
                    if(game.getPlayers().size() == 0){
                        gameInstanceHandler.resetGame(game.getGameID());
                    }
                }
            }

        }
    }

}
