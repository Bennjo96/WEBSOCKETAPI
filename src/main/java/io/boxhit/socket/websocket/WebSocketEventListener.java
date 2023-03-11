package io.boxhit.socket.websocket;

import io.boxhit.socket.users.UserManager;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        UserManager.addUser(event.getUser());
        //System.out.println(event.getUser().getName());
        System.out.println("Received a new web socket connection "+event.getUser().getName());
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        UserManager.removeUser(event.getUser());
        System.out.println("Received disconnect from "+event.getUser().getName());
    }

    @EventListener
    public void handleSessionUnsubscribeEvent(SessionSubscribeEvent event) {

        //GenericMessage message = (GenericMessage) event.getMessage();
        //String simpDestination = (String) message.getHeaders().get("simpDestination");
        //System.out.println(simpDestination+" "+message.getPayload());
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        //GenericMessage message = (GenericMessage) event.getMessage();
        //String simpDestination = (String) message.getHeaders().get("simpDestination");
        //System.out.println(simpDestination+" "+message.getPayload());
    }

}
