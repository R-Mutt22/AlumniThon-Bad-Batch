package com.bad.batch.websocket.config;

import com.bad.batch.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityConfig implements ChannelInterceptor {
    
    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authToken = accessor.getFirstNativeHeader("Authorization");
            
            // Si no hay token en Authorization header, buscar en los atributos de sesión (del handshake)
            if (authToken == null && accessor.getSessionAttributes() != null) {
                Object tokenAttr = accessor.getSessionAttributes().get("token");
                if (tokenAttr != null) {
                    authToken = "Bearer " + tokenAttr.toString();
                }
            }
            
            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7);
                
                try {
                    if (!jwtService.isExpired(token)) {
                        Long userId = jwtService.extractUserId(token);
                        String role = jwtService.extractRole(token);
                        
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                        
                        accessor.setUser(auth);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Token inválido");
                }
            }
        }
        
        return message;
    }
}
