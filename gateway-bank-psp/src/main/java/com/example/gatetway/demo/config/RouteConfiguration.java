package com.example.gatetway.demo.config;

import com.example.gatetway.demo.model.PaymentOption;
import com.example.gatetway.demo.repo.IPaymentOptionRepo;
import com.example.gatetway.demo.service.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class RouteConfiguration {

    @Autowired
    private IPaymentOptionRepo paymentOptionRepo;
    private String address="";
    private Integer port=0;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("dynamic_route", r -> {
                    try {
                        return r
                                .path("/api/payments")
                                .filters(f -> f.filter((exchange, chain) -> {
                                    HttpHeaders headers = exchange.getRequest().getHeaders();
                                    String payment = headers.getFirst("Payment");

                                    if (payment != null && !payment.isEmpty()) {
                                        PaymentOptionService paymentOptionService = new PaymentOptionService(paymentOptionRepo);
                                        PaymentOption paymentOption = paymentOptionService.getAddressByOption(payment);
                                        address=paymentOption.getAddress();
                                        port=paymentOption.getPort();
                                        if (address != null && !address.isEmpty()) {
                                            String newUrl = "http://localhost:" + address;
                                            System.out.println("Preusmeravanje na: " + newUrl);

                                            try {
                                                return chain.filter(exchange.mutate()
                                                        .request(exchange.getRequest().mutate().uri(new URI("http", null, "localhost", port, address, null, null)).build())
                                                        .build());
                                            } catch (URISyntaxException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }

                                    return chain.filter(exchange);
                                }))
                                .uri(new URI("http", null, "localhost", port, address, null, null));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }
}
