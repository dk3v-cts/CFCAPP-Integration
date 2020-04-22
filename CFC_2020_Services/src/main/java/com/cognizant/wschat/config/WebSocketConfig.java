package com.cognizant.wschat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue/", "/topic/", "/exchange/");
		//RabbitMQ
//		registry.enableStompBrokerRelay("/queue/", "/topic/", "/exchange/").setRelayHost("localhost").setRelayPort(61613).setClientLogin("guest")
	//	.setClientPasscode("guest");
		registry.setApplicationDestinationPrefixes("/app");
	}
}