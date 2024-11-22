/*package com.example.gatetway.demo.config;

import com.example.gatetway.demo.repo.IPaymentOptionRepo;
import com.example.gatetway.demo.service.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;


import java.net.URI;


@Configuration
public class RouteConfiguration {

    @Autowired
    IPaymentOptionRepo paymentOptionRepo;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("dynamic_route", r -> r
                        .path("/api/payments")
                        .filters(f -> f.filter((exchange, chain) -> {
                            HttpHeaders headers = exchange.getRequest().getHeaders();
                            String payment = headers.getFirst("Payment");
                            System.out.println(payment);

                            PaymentOptionService paymentOptionService=new PaymentOptionService(paymentOptionRepo);
                            String address=paymentOptionService.getAddressByOption(payment);


                            String newUrl = "http://localhost:"+address;
                            ServerWebExchange updatedExchange = exchange.mutate()
                                    .request(exchange.getRequest().mutate().uri(URI.create(newUrl)).build())
                                    .build();
                            return chain.filter(updatedExchange);

                        }))
                        .uri("http://localhost")) 
                .build();
    }

}
*/
package com.example.gatetway.demo.config;

import com.example.gatetway.demo.repo.IPaymentOptionRepo;
import com.example.gatetway.demo.service.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Configuration
public class RouteConfiguration {

    @Autowired
    private IPaymentOptionRepo paymentOptionRepo;

    @Autowired
    private PaymentOptionService paymentOptionService;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Dinamička ruta na osnovu "Payment" header-a
                .route("dynamic_route", r -> r
                        .path("/api/payments")
                        .filters(f -> f.filter((exchange, chain) -> {
                            HttpHeaders headers = exchange.getRequest().getHeaders();
                            String payment = headers.getFirst("Payment");
                            System.out.println("Received Payment Header: " + payment);

                            if (payment == null || payment.isEmpty()) {
                                System.out.println("Payment header is missing or empty");
                                return exchange.getResponse().setComplete();
                            }

                            String address = paymentOptionService.getAddressByOption(payment);
                            System.out.println("Resolved Address: " + address);

                            if (address == null || address.isEmpty()) {
                                System.out.println("Address resolution failed");
                                return exchange.getResponse().setComplete();
                            }

                            String newUrl = "http://localhost:" + address;
                            System.out.println("Redirecting to: " + newUrl);

                            URI newUri;
                            try {
                                newUri = URI.create(newUrl);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid URL: " + newUrl);
                                return exchange.getResponse().setComplete();
                            }

                            ServerWebExchange updatedExchange = exchange.mutate()
                                    .request(exchange.getRequest().mutate().uri(newUri).build())
                                    .build();
                            return chain.filter(updatedExchange);
                        }))
                        .uri("http://localhost")) // Ovo se ignoriše, jer koristimo filter
                // Staticka ruta za /api/accounts/**
                .route("static_route", r -> r
                        .path("/api/accounts/**")
                        .uri("http://localhost:8052"))
                .build();
    }
}
