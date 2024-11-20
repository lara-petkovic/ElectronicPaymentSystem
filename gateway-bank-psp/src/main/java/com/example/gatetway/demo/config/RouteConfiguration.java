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
