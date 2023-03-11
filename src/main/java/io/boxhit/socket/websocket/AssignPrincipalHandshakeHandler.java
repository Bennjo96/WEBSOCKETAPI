package io.boxhit.socket.websocket;

import io.boxhit.socket.users.SessionToken;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class AssignPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private static final String ATTR_PRINCIPAL = "__principal__";

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String name;
        if (!attributes.containsKey(ATTR_PRINCIPAL)) {
            name = generateRandomUsername();
            attributes.put(ATTR_PRINCIPAL, name);
        } else {
            name = (String) attributes.get(ATTR_PRINCIPAL);
        }
        return new Principal() {
            @Override
            public String getName() {
                return name;
            }
        };
    }

    private String generateRandomUsername() {
        //username created from random 32 character string
        return SessionToken.generateToken(32);
    }
}