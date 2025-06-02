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
        registry.addHandler(clientSubscriptionWebSocketHandler(), "/clients").setAllowedOrigins("*");
        registry.addHandler(transactionResponseHandler(), "/responses").setAllowedOrigins("*");
    }
    @Bean
    public ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler() {
        return new ClientSubscriptionWebSocketHandler();
    }
    @Bean
    public TradeWebSocketHandler tradeWebSocketHandler() {
        return new TradeWebSocketHandler();
    }
    @Bean
    public TransactionResponseHandler transactionResponseHandler() {
        return new TransactionResponseHandler();
    }
}
