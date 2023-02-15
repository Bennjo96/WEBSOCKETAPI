package io.boxhit.socket.messages;

import io.boxhit.socket.websocket.Message;
import io.boxhit.socket.websocket.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Controller
public class MessageController {

    /**
     * Chat dings, -> sends message to /topic/messages aber ihr machend des über /stream des isch des chat beispiel
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/auth")
    @SendTo("/topic/messages")
    public OutputMessage send(final io.boxhit.socket.websocket.Message message) throws Exception {
        Logger.getLogger("MessageController").info("Message received " + message.getModule() + " : " + message.getHeader() + " : " + message.getJson());
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getModule(), message.getHeader(), message.getJson());
    }

}
