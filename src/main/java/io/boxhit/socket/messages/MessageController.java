package io.boxhit.socket.messages;

import io.boxhit.logic.Controller;
import io.boxhit.logic.subject.Game;
import io.boxhit.socket.database.players.Player;
import io.boxhit.socket.database.players.PlayerRepository;
import io.boxhit.socket.database.playlog.PlayLog;
import io.boxhit.socket.database.playlog.PlayLogRepository;
import io.boxhit.socket.database.stats.StatsRepository;
import io.boxhit.socket.users.UserManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@org.springframework.stereotype.Controller
public class MessageController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayLogRepository playLogRepository;

    @Autowired
    private StatsRepository statsRepository;

    @Autowired
    public SimpMessagingTemplate template;

    @MessageMapping("/auth")
    @SendTo("/topic/messages")
    public OutputMessage authorization(final OutputMessage message, Principal principal) {
        if(MessageSender.messageController == null) MessageSender.messageController = this;
        System.out.println("Incoming: "+ message.toString());
        //message is incoming
        MessageModule mm = MessageModule.fromText(message.getModule().replaceAll("\"", ""));
        System.out.println("Module: "+mm);
        if(mm != null){
            switch(mm){
                case AUTHORIZATION:
                    System.out.println("Authorization");
                    //get Session Token from Login from user
                    String sessionToken = new JSONObject(message.getJson()).get("loginToken").toString();
                    //get Player from Database
                    Player player = playerRepository.findPlayerBySessionToken(sessionToken);
                    System.out.println(player.toString());
                    //create new OutputMessage -> send to client
                    OutputMessage outputMessage = new OutputMessage().setModule(MessageModule.AUTHORIZATION.getModule());
                    //check if player is not null
                    if(player != null){
                        if(Controller.getPlayerInstanceHandler().isAlreadyRegistered(player.getId()+"")){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("sessionToken", "Already logged in on another device.");
                            outputMessage.setJson(jsonObject.toString());
                            System.out.println("Outgoing: "+outputMessage.toString());
                            outputMessage.setHeader(MessageTemplates.getDefaultHeader());
                            template.convertAndSendToUser(principal.getName(), "/queue/reply", outputMessage);
                            return outputMessage;
                        }
                        //get new Token from the UserManager - SessionToken for Websocket connection
                        //String token = "{\"sessionToken\":\""+UserManager.getUserToken(principal)+"\"}";
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("sessionToken", UserManager.getUserToken(principal));
                        outputMessage.setJson(jsonObject.toString());
                        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
                        template.convertAndSendToUser(principal.getName(), "/queue/reply", outputMessage);
                        //template.convertAndSend("/topic/messages", outputMessage);

                        //create Player in Backend - GamePlayer
                        Controller.getPlayerInstanceHandler().prepareRegisterPlayer(principal.getName(), player.getUsername(), player.getColor());

                        playerRepository.updatePlayerSessionToken(UserManager.getUserToken(principal), player.getId());

                        System.out.println("Outgoing: "+outputMessage.toString());
                        return outputMessage;
                    }
                    break;
            }
        }

        //outputMessage.
        //
        //login -> user + pw -> get Token
        //-> Send Request to Server -> Token+UserName
        //-> Server -> validate from database
        //-> Server -> send NEW 200 Token to client
        //-> Server Save Token in Database
        //-> Minutely check if Token is still valid (DB)
        //
        //template.convertAndSendToUser(principal.getName(), "/queue/reply", MessageTemplates.getError("Unexpected Error!"));
        return MessageTemplates.getError("Unexpected Error!");

    }

    @MessageMapping("/info")
    @SendTo("/topic/messages")
    public OutputMessage information(final OutputMessage message, Principal principal) throws Exception {
        //System.out.println("Incoming: "+message.toString());
        String header = message.getHeader();
        JSONObject object = new JSONObject(header);
        String token = object.getString("sessionToken");
        String username = UserManager.getUserNameByToken(token);
        if (username != null) {
            MessageModule mm = MessageModule.fromText(message.getModule().replaceAll("\"", ""));
            if (mm != null) {
                switch(mm) {
                    case SERVER_LIST:
                        OutputMessage outputMessage = new OutputMessage().setModule(MessageModule.SERVER_LIST.getModule());
                        outputMessage.setJson(Controller.getGameInstanceHandler().getGameListJson());
                        //System.out.println("Outgoing: "+outputMessage.toString());
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage);
                        return null;
                    case ACTION_PLAYLOG_INFO_REQUEST:
                        OutputMessage outputMessage2 = new OutputMessage().setModule(MessageModule.ACTION_PLAYLOG_INFO.getModule());

                        //String username = UserManager.getUserNameByToken(token);

                        System.out.println("Username: "+username);
                        System.out.println("Token: "+token);
                        //String token = UserManager.getUserToken(principal);

                        List<PlayLog> playLogs = playLogRepository.getPlayLogFromPlayerId(5L);

                        JSONArray jsonArray = new JSONArray();
                        for(PlayLog playLog : playLogs){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("date", playLog.getLastplayed());
                            jsonObject.put("info", playLog.getScore()+":"+playLog.getPlayercount());
                            jsonArray.put(jsonObject);
                        }
                        outputMessage2.setJson(jsonArray.toString());

                        template.convertAndSendToUser(username, "/queue/reply", outputMessage2);

                        System.out.println("Outgoing: "+outputMessage2.toString());
                        return null;
                }
            }
        }
        template.convertAndSendToUser(username, "/queue/reply", MessageTemplates.getError("Unexpected Error!"));
        return null;
    }

    @MessageMapping("/game")
    @SendTo("/topic/messages")
    public OutputMessage game(final OutputMessage message, Principal principal) throws Exception {
        System.out.println("Incoming: "+message.toString());
        String header = message.getHeader();
        JSONObject object = new JSONObject(header);
        String token = object.getString("sessionToken");
        String username = UserManager.getUserNameByToken(token);
        if (username != null) {
            MessageModule mm = MessageModule.fromText(message.getModule().replaceAll("\"", ""));
            if (mm != null) {
                switch(mm) {
                    case REQUEST_JOIN_GAME:
                        //requesting to join a game
                        JSONObject jsonObject = new JSONObject(message.getJson());
                        int gameId = jsonObject.getInt("gameId");
                        OutputMessage outputMessage;
                        if(Controller.getGameProtectionHandler().isAllowedToJoin(principal.getName())){
                            //is allowed to join (cooldown)
                            boolean success = Controller.getGameInstanceHandler().requestPlayerJoinGame(principal.getName(), gameId);
                            if(success){
                                outputMessage = new OutputMessage().setModule(MessageModule.ACTION_JOIN_GAME.getModule());
                            }else{
                                outputMessage = new OutputMessage().setModule(MessageModule.ACTION_JOIN_GAME_DENIED.getModule());
                            }
                        }else{
                            //is not allowed to join (cooldown)
                            outputMessage = new OutputMessage().setModule(MessageModule.ACTION_JOIN_GAME_DENIED.getModule());
                        }
                        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
                        System.out.println("Outgoing: "+outputMessage.toString());
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage);
                        return null;
                    case REQUEST_GAME_DATA:
                        //requesting game data
                        io.boxhit.logic.subject.Player player = Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        OutputMessage outputMessage2 = new OutputMessage().setModule(MessageModule.ACTION_GAME_DATA.getModule());
                        outputMessage2.setHeader(MessageTemplates.getDefaultHeader());
                        if(player != null){
                            String data = Controller.getGameInstanceHandler().playerRetrieveGameData(player, player.getCurrentGameID());
                            outputMessage2.setJson(data);
                        }
                        System.out.println("Outgoing: "+outputMessage2.toString());
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage2);
                        return null;
                    case ACTION_LEAVE_GAME:
                        //player leaves game
                        Controller.getGameProtectionHandler().setLastDisconnect(principal.getName(), System.currentTimeMillis());
                        io.boxhit.logic.subject.Player player2 = Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        int maxPlayers = 0;
                        if(player2 != null){
                            Player player1 = playerRepository.findPlayerByUsername(player2.getName());
                            int gameId1 = player2.getCurrentGameID();
                            if(gameId1 != -1) {
                                Game game = Controller.getGameInstanceHandler().getGame(gameId1);
                                maxPlayers = game.maxPlayersInGame;
                                Controller.getGameInstanceHandler().leaveGame(player2, gameId1);
                                player2.setCurrentGameID(-1);
                                PlayLog pl = new PlayLog();
                                pl.setLastplayed(System.currentTimeMillis());
                                pl.setPlayercount(maxPlayers);
                                pl.setScore(player2.getScore());
                                pl.setUserid(player1.getId());
                                playLogRepository.insertPlayLog(pl);
                            }
                        }
                        return null;
                    case ACTION_GAME_MOVE:
                        if(!Controller.getGameProtectionHandler().isAllowedToMove(principal.getName())) {
                            return null;
                        }
                        Controller.getGameProtectionHandler().setLastMove(principal.getName(), System.currentTimeMillis());

                        JSONObject jsonObject1 = new JSONObject(message.getJson());
                        String direction = jsonObject1.getString("direction");
                        Controller.getGameInstanceHandler().movePlayer(principal.getName(), direction);
                        return null;
                    case ACTION_GAME_ATTACK:
                        io.boxhit.logic.subject.Player player3 = Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        if(!Controller.getGameProtectionHandler().isAllowedToAttack(principal.getName())){
                            return null;
                        }
                        Controller.getGameProtectionHandler().setLastAttack(principal.getName(), System.currentTimeMillis());
                        if(player3 != null){
                            int gameId1 = player3.getCurrentGameID();
                            if(gameId1 != -1) {
                                Game game = Controller.getGameInstanceHandler().getGame(gameId1);
                                game.executePlayerAttack(player3);
                            }
                        }
                        return null;
                    case ACTION_GAME_FORCE_START:
                        //TODO only specific users can force start
                        io.boxhit.logic.subject.Player player4 = Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        if(player4 != null){
                            int gameId1 = player4.getCurrentGameID();
                            if(gameId1 != -1) Controller.getGameInstanceHandler().startGame(gameId1);
                        }
                        return null;
                }
            }
        }

        template.convertAndSendToUser(username, "/queue/reply", MessageTemplates.getError("Unexpected Error!"));
        return null;
    }


}
