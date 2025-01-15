package com.example.crypto.service;

import com.example.crypto.controller.TransactionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

    public CurrencyConversionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.coingecko.com/api/v3").build();
    }

    public double convertEurToSepoliaEth(double amount) {
        // Endpoint za dobijanje cene ETH u EUR
        String ethPriceEndpoint = "/simple/price?ids=ethereum&vs_currencies=eur";

        // Poziv API-ja
        Map<String, Map<String, Double>> response = webClient.get()
                .uri(ethPriceEndpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, Double>>>() {})
                .block();

        double ethPriceInEur = response.get("ethereum").get("eur");

        logger.info("Converted "+amount+" EUR to "+amount/ethPriceInEur+" ETH");
        return amount / ethPriceInEur;
    }
}
