package com.example.sep.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Definišite destinaciju za klijente
        config.setApplicationDestinationPrefixes("/app"); // Definišite prefiks za poruke poslate na server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Definišite WebSocket endpoint
                .setAllowedOrigins("http://localhost:4200") // Dozvolite pristup sa Angular aplikacije
                .withSockJS(); // Omogućite SockJS podršku za bolje kompatibilnosti
    }
}
