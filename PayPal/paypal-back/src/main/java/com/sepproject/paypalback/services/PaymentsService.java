package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.repositories.PaymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PaymentsService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Autowired
    private PaymentsRepository paymentsRepository;

    private static final String PAYPAL_API_URL = "https://api.sandbox.paypal.com/v2/checkout/orders";

    public Payment processPayment(String orderId, String merchantId, Double amount, String status) {
        try {
            String accessToken = getAccessToken();
            String paypalOrderId = createPaypalOrder(amount, accessToken);

            Payment payment = new Payment(orderId, merchantId, amount, status, LocalDateTime.now());
            payment.setPaypalOrderId(paypalOrderId);
            paymentsRepository.save(payment);

            return payment;
        } catch (Exception e) {
            throw new RuntimeException("Error during PayPal payment processing", e);
        }
    }

    public String capturePayment(String paypalOrderId, String accessToken) {
        String captureUrl = "https://api.sandbox.paypal.com/v2/checkout/orders/" + paypalOrderId + "/capture";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(captureUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) Objects.requireNonNull(response.getBody()).get("status");
        } else {
            throw new RuntimeException("Failed to capture payment from PayPal: " + response.getBody());
        }
    }

    private String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.sandbox.paypal.com/v1/oauth2/token";

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = new String(Base64.getEncoder().encode(auth.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) Objects.requireNonNull(response.getBody()).get("access_token");
        } else {
            throw new RuntimeException("Failed to retrieve access token from PayPal");
        }
    }

    private String createPaypalOrder(Double amount, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> purchaseUnit = new HashMap<>();
        purchaseUnit.put("amount", Map.of("value", amount, "currency_code", "USD"));

        requestBody.put("intent", "CAPTURE");
        requestBody.put("purchase_units", new Object[]{purchaseUnit});

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(PAYPAL_API_URL, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Objects.requireNonNull(response.getBody()).get("id").toString();
        } else {
            throw new RuntimeException("Failed to create PayPal order");
        }
    }
}