package io.boxhit.socket.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //@Override
    //public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    //    registry.addHandler(webSocketHandler(), "/websocket");
    //}
//
    //@Bean
    //public WebSocketHandler webSocketHandler(){
    //    return new ServerWebSocketHandler();
    //}

    /**
     * das sind die channels wo es hinsendet, bei euch müsst ihr es zu /stream ändern
     *
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Endpunkt für den Client wo er den socket connecten muss, bei euch müsst ihr es zu /api ändern
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").withSockJS();
    }





}
