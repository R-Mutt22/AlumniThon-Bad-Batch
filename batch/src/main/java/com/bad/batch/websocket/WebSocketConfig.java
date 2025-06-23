package com.bad.batch.websocket;


import com.bad.batch.websocket.config.WebSocketSecurityConfig;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

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
				.withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(webSocketSecurityConfig);
	}

}