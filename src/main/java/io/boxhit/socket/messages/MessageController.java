package io.boxhit.socket.messages;

import io.boxhit.logic.GameInstanceHandler;
import io.boxhit.logic.PlayerInstanceHandler;
import io.boxhit.socket.database.players.Player;
import io.boxhit.socket.database.players.PlayerRepository;
import io.boxhit.socket.users.UserManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    @Autowired
    private PlayerRepository playerRepository;

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
                        //get new Token from the UserManager - SessionToken for Websocket connection
                        //String token = "{\"sessionToken\":\""+UserManager.getUserToken(principal)+"\"}";
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("sessionToken", UserManager.getUserToken(principal));
                        outputMessage.setJson(jsonObject.toString());
                        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
                        template.convertAndSendToUser(principal.getName(), "/queue/reply", outputMessage);
                        //template.convertAndSend("/topic/messages", outputMessage);

                        //create Player in Backend - GamePlayer
                        io.boxhit.logic.Controller.getPlayerInstanceHandler().prepareRegisterPlayer(principal.getName(), player.getUsername(), player.getColor());

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
        System.out.println("Incoming: "+message.toString());
        //parse message.getHeader to json
        String header = message.getHeader();
        JSONObject object = new JSONObject(header);
        String token = object.getString("sessionToken");
        String username = UserManager.getUserNameByToken(token);
        //System.out.println("Header: " + header);
        //System.out.println("Token: " + token);
        //System.out.println("Username: " + username);
        if (username != null) {
            MessageModule mm = MessageModule.fromText(message.getModule().replaceAll("\"", ""));
            if (mm != null) {
                switch(mm) {
                    case SERVER_LIST:
                        //send message to user
                        OutputMessage outputMessage = new OutputMessage().setModule(MessageModule.SERVER_LIST.getModule());
                        outputMessage.setJson(io.boxhit.logic.Controller.getGameInstanceHandler().getGameListJson());
                        System.out.println("Outgoing: "+outputMessage.toString());
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage);
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
        //parse message.getHeader to json
        String header = message.getHeader();
        JSONObject object = new JSONObject(header);
        String token = object.getString("sessionToken");
        String username = UserManager.getUserNameByToken(token);
        //System.out.println("Header: " + header);
        //System.out.println("Token: " + token);
        //System.out.println("Username: " + username);
        if (username != null) {
            MessageModule mm = MessageModule.fromText(message.getModule().replaceAll("\"", ""));
            if (mm != null) {
                switch(mm) {
                    case REQUEST_JOIN_GAME:
                        //requesting to join a game
                        JSONObject jsonObject = new JSONObject(message.getJson());
                        int gameId = jsonObject.getInt("gameId");
                        boolean success = io.boxhit.logic.Controller.getGameInstanceHandler().requestPlayerJoinGame(principal.getName(), gameId);

                        OutputMessage outputMessage;
                        if(success){
                            outputMessage = new OutputMessage().setModule(MessageModule.ACTION_JOIN_GAME.getModule());
                        }else{
                            outputMessage = new OutputMessage().setModule(MessageModule.ACTION_JOIN_GAME_DENIED.getModule());
                        }
                        outputMessage.setHeader(MessageTemplates.getDefaultHeader());
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage);
                        return null;
                    case REQUEST_GAME_DATA:
                        //requesting game data
                        io.boxhit.logic.subject.Player player = io.boxhit.logic.Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        OutputMessage outputMessage2 = new OutputMessage().setModule(MessageModule.ACTION_GAME_DATA.getModule());
                        outputMessage2.setHeader(MessageTemplates.getDefaultHeader());
                        if(player != null){
                            String data = io.boxhit.logic.Controller.getGameInstanceHandler().playerRetrieveGameData(player, player.getCurrentGameID());
                            outputMessage2.setJson(data);
                        }
                        template.convertAndSendToUser(username, "/queue/reply", outputMessage2);
                        break;
                    case ACTION_LEAVE_GAME:
                        //player leaves game
                        io.boxhit.logic.subject.Player player2 = io.boxhit.logic.Controller.getPlayerInstanceHandler().getPlayer(principal.getName());
                        if(player2.getCurrentGameID() != -1){
                            //io.boxhit.logic.Controller.getGameInstanceHandler().playerLeaveGame(player2, player2.getCurrentGameID());
                        }
                        io.boxhit.logic.Controller.getPlayerInstanceHandler().prepareUnregisterPlayer(player2.getPlayerID());
                        break;
                }
            }
        }

        template.convertAndSendToUser(username, "/queue/reply", MessageTemplates.getError("Unexpected Error!"));
        return null;
    }


}
