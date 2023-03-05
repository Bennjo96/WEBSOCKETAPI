package io.boxhit.socket.messages;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

public class MessageSender {

    public static MessageController messageController = null;

    public void send(OutputMessage message, String user){
        messageController.template.convertAndSendToUser(user, "/queue/reply", message);
    }

}
