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
import reactor.core.publisher.Flux;


import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class RouteConfiguration {

    @Autowired
    private IPaymentOptionRepo paymentOptionRepo;
    private String address="";
    private int port=0;
    private String option="8088";



/*
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
                                        option=paymentOption.getPort();
                                        port=Integer.parseInt(option);

                                        if (address != null && !address.isEmpty()) {
                                            String newUrl = "http://localhost:" + address;
                                            System.out.println("Preusmeravanje na: " + newUrl);

                                            try {
                                                return chain.filter(exchange.mutate()
                                                        .request(exchange.getRequest().mutate().uri(new URI("http", null, "localhost", Integer.parseInt(option), address, null, null)).build())
                                                        .build());
                                            } catch (URISyntaxException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }

                                    return chain.filter(exchange);
                                }))
                                .uri(new URI("http", null, "localhost",port
                                        , address, null, null));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }*/
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

                                // Provera da li postoji "Payment" header
                                if (payment != null && !payment.isEmpty()) {
                                    PaymentOptionService paymentOptionService = new PaymentOptionService(paymentOptionRepo);
                                    PaymentOption paymentOption = paymentOptionService.getAddressByOption(payment);

                                    // Postavljanje adrese i porta iz baze
                                    address = paymentOption.getAddress();
                                    port = Integer.parseInt(paymentOption.getPort());

                                    System.out.println("Dinamička adresa: " + address + ", Port: " + port);

                                    // Provera da li su adresa i port validni
                                    if (address != null && !address.isEmpty() && port > 0 && port <= 65535) {
                                        try {
                                            URI newUri = new URI("http",null, "localhost", port, address, null, null );
                                            System.out.println("Redirekcija na: " + newUri);

                                            // Postavljanje novog URI-ja
                                            return chain.filter(exchange.mutate()
                                                    .request(exchange.getRequest().mutate().uri(newUri).build())
                                                    .build());
                                        } catch (Exception e) {
                                            System.err.println("Greška pri kreiranju URI-ja: " + e.getMessage());
                                        }
                                    }
                                }

                                System.out.println("Koristi podrazumevani URI jer je dinamička modifikacija neuspešna.");
                                return chain.filter(exchange); // Nastavlja sa originalnim zahtevom
                            }))
                            .uri(new URI("http",null, "localhost", 8088, address, null, null ));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }) // Placeholder URI ako modifikacija ne uspe
            .build();
}

}
