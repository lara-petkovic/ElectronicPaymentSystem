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

    private static final String PAYPAL_API_BASE_URL = "https://api.sandbox.paypal.com";
    private static final String PAYPAL_CREATE_ORDER_URL = PAYPAL_API_BASE_URL + "/v2/checkout/orders";
    private static final String PAYPAL_CAPTURE_ORDER_URL = PAYPAL_API_BASE_URL + "/v2/checkout/orders/{orderId}/capture";

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() {
        String authUrl = PAYPAL_API_BASE_URL + "/v1/oauth2/token";

        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + credentials);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, Map.class);

        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
    }

    public String createPaypalOrder(String orderId, String merchantId, Double amount) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        Map<String, Object> purchaseUnit = new HashMap<>();
        purchaseUnit.put("description", "Order ID: " + orderId + ", Merchant ID: " + merchantId);
        purchaseUnit.put("amount", Map.of("currency_code", "USD", "value", amount));

        Map<String, Object> requestBody = Map.of("intent", "CAPTURE", "purchase_units", new Object[]{purchaseUnit});

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(PAYPAL_CREATE_ORDER_URL, request, Map.class);

        return (String) Objects.requireNonNull(response.getBody()).get("id");
    }

    public Payment capturePayment(String paypalOrderId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                PAYPAL_CAPTURE_ORDER_URL,
                HttpMethod.POST,
                request,
                Map.class,
                paypalOrderId
        );

        boolean isCaptured = response.getStatusCode().is2xxSuccessful();
        if (isCaptured) {
            Payment payment = new Payment();
            payment.setOrderId(paypalOrderId);
            payment.setStatus("COMPLETED");
            payment.setTimestamp(LocalDateTime.now());
            paymentsRepository.save(payment);

            return payment;
        }

        throw new RuntimeException("Payment capture failed!");
    }
}