package com.example.crypto.service;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private final WebClient webClient;

    public CurrencyConversionService(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                            try {
                                sslContextSpec
                                        .sslContext(
                                                SslContextBuilder.forClient()
                                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                        .build()
                                        );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        this.webClient = webClientBuilder
                .baseUrl("https://api.coingecko.com/api/v3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public double convertEurToSepoliaEth(double amount) {
        String ethPriceEndpoint = "/simple/price?ids=ethereum&vs_currencies=eur";

        Map<String, Map<String, Double>> response = webClient.get()
                .uri(ethPriceEndpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, Double>>>() {})
                .block();

        double ethPriceInEur = response.get("ethereum").get("eur");

        return amount / ethPriceInEur;
    }
}
