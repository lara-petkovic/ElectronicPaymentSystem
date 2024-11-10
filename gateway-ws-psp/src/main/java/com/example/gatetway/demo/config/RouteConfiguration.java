package com.example.gatetway.demo.config;

import com.example.gatetway.demo.filter.RequestFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/service-https/**")
                        .uri("http://localhost:8085/api/subscription"))  // Putanja za preusmeravanje
                .build();
    }
}
