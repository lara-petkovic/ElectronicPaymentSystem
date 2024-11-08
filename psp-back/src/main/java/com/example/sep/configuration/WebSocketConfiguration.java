package com.example.sep.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(tradeWebSocketHandler(), "/transactions").setAllowedOrigins("*");
    }

    @Bean
    public TradeWebSocketHandler tradeWebSocketHandler() {
        return new TradeWebSocketHandler();
    }
}
