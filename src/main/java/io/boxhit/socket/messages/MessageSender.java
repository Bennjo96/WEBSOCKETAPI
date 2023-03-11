package io.boxhit.socket.messages;

public class MessageSender {

    public static MessageController messageController = null;

    public void send(OutputMessage message, String user){
        messageController.template.convertAndSendToUser(user, "/queue/reply", message);
    }

}
