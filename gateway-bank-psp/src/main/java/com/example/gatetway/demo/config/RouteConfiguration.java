package com.example.gatetway.demo.config;

import com.example.gatetway.demo.filter.RequestFilter;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Configuration
public class RouteConfiguration {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("dynamic_route", r -> r
                        .path("/api/payments")  // Match the incoming path
                        .filters(f -> f.filter((exchange, chain) -> {
                            HttpHeaders headers = exchange.getRequest().getHeaders();
                            String payment = headers.getFirst("Payment");
                            System.out.println(payment);

                            if (payment == "Card") {
                                String newUrl = "http://localhost:8052/api/payments";
                                ServerWebExchange updatedExchange = exchange.mutate()
                                        .request(exchange.getRequest().mutate().uri(URI.create(newUrl)).build())
                                        .build();
                                return chain.filter(updatedExchange);
                            }
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost")) 
                .build();
    }

}
