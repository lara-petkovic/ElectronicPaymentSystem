package com.example.bank.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(creditCardWebSocketHandler(), "/creditCards").setAllowedOrigins("https://localhost:4202");
    }
    @Bean
    public CreditCardWebSocketHandler creditCardWebSocketHandler() {
        return new CreditCardWebSocketHandler();
    }
}
