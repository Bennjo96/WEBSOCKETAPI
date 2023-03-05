package io.boxhit.socket.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String SUBSCRIBE_USER_PREFIX = "/private";
    public static final String SUBSCRIBE_USER_REPLY = "/reply";
    public static final String SUBSCRIBE_QUEUE = "/queue";

    /**
     * das sind die channels wo es hinsendet, bei euch müsst ihr es zu /stream ändern
     *
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.enableSimpleBroker(SUBSCRIBE_QUEUE, SUBSCRIBE_USER_REPLY);
    }

    /**
     * Endpunkt für den Client wo er den socket connecten muss, bei euch müsst ihr es zu /api ändern
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/protocol/v1")
                .setHandshakeHandler(new AssignPrincipalHandshakeHandler())
                .setAllowedOrigins("*");
        registry.addEndpoint("/protocol/v1").withSockJS();

    }
}