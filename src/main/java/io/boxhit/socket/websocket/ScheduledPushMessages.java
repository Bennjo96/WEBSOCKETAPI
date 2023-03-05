package io.boxhit.socket.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduledPushMessages {

    private final SimpMessagingTemplate template;

    public ScheduledPushMessages(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * das lässt ihr mal noch so, da kommt dann der ping her
     */
    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        //template.convertAndSend("/topic/pushmessages", new OutputMessageOld("Server", "Ping!", time));
    }

}
