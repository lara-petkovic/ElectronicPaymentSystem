package com.example.gatetway.demo.predicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.*;
public class CustomPredicate extends AbstractRoutePredicateFactory<CustomPredicate.Config> {
    public static class Config {
        // Configuration properties if any
    }

    public CustomPredicate() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            // Custom predicate logic
            return exchange.getRequest().getHeaders().containsKey("X-Custom-Header");
        };
    }
}
