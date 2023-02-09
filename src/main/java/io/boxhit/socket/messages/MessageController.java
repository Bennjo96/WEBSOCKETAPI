package io.boxhit.socket.messages;

import io.boxhit.socket.websocket.Message;
import io.boxhit.socket.websocket.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MessageController {

    /**
     * Chat dings, -> sends message to /topic/messages aber ihr machend des über /stream des isch des chat beispiel
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(final io.boxhit.socket.websocket.Message message) throws Exception {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    @MessageMapping("/api")
    @SendTo("/stream")
    public OutputMessage send_(final Message message) throws Exception {
        //message erhalten von user
        return new OutputMessage(message.getFrom(), message.getText(), "irgendwas");
    }

}
