package com.bad.batch.websocket;


import com.bad.batch.websocket.config.WebSocketSecurityConfig;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final WebSocketSecurityConfig webSocketSecurityConfig;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// Prefijos para suscripciones
		config.enableSimpleBroker("/topic", "/queue");
		// Prefijo para mensajes del cliente
		config.setApplicationDestinationPrefixes("/app");
		// Prefijo para mensajes de usuario específico
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			    .setAllowedOriginPatterns("*")
				.addInterceptors(new TokenHandshakeInterceptor())
				.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(webSocketSecurityConfig);
	}
	
	// Interceptor para capturar el token del query parameter
	public static class TokenHandshakeInterceptor implements HandshakeInterceptor {
		
		@Override
		public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
				WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
			
			String query = request.getURI().getQuery();
			if (query != null && query.contains("token=")) {
				String token = extractTokenFromQuery(query);
				if (token != null) {
					attributes.put("token", token);
				}
			}
			
			return true;
		}
		
		@Override
		public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
				WebSocketHandler wsHandler, Exception exception) {
		}
		
		private String extractTokenFromQuery(String query) {
			String[] params = query.split("&");
			for (String param : params) {
				if (param.startsWith("token=")) {
					return param.substring(6); // "token=".length()
				}
			}
			return null;
		}
	}

}