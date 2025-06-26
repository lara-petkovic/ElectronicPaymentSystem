package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.repositories.PaymentsRepository;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentsService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Autowired
    private PaymentsRepository paymentsRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PAYPAL_API_BASE_URL = "https://api.sandbox.paypal.com";
    private static final String PAYPAL_CREATE_ORDER_URL = PAYPAL_API_BASE_URL + "/v2/checkout/orders";
    private static final String PAYPAL_CAPTURE_ORDER_URL = PAYPAL_API_BASE_URL + "/v2/checkout/orders/{orderId}/capture";

    private final WebClient webClient;

    public PaymentsService() {
        this.webClient = createUnsafeWebClient();
    }

    private WebClient createUnsafeWebClient() {
        try {
            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> {
                        try {
                            sslContextSpec.sslContext(
                                    SslContextBuilder.forClient()
                                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                            .build()
                            );
                        } catch (SSLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            return WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create WebClient", e);
        }
    }
//
//    private String getAccessToken() {
//        String authUrl = PAYPAL_API_BASE_URL + "/v1/oauth2/token";
//
//        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic " + credentials);
//        headers.add("Content-Type", "application/x-www-form-urlencoded");
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "client_credentials");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, Map.class);
//
//        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
//    }

    private String getAccessToken() {
        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        Map response = webClient.post()
                .uri(PAYPAL_API_BASE_URL + "/v1/oauth2/token")
                .header("Authorization", "Basic " + credentials)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) Objects.requireNonNull(response).get("access_token");
    }

    public String createPaypalOrder(String orderId, String merchantId, Double amount) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalArgumentException("Merchant ID cannot be null or empty");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        try {
            String accessToken = getAccessToken();
            if (accessToken == null || accessToken.isBlank()) {
                throw new RuntimeException("Failed to obtain valid PayPal access token");
            }

            Map<String, Object> amountMap = new HashMap<>();
            amountMap.put("currency_code", "USD");
            amountMap.put("value", String.format("%.2f", amount));

            Map<String, Object> purchaseUnit = new HashMap<>();
            purchaseUnit.put("description", "Order ID: " + orderId);
            purchaseUnit.put("amount", amountMap);
            purchaseUnit.put("reference_id", merchantId);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("intent", "CAPTURE");
            requestBody.put("purchase_units", Collections.singletonList(purchaseUnit));

            // Make the API call
            Map response = webClient.post()
                    .uri(PAYPAL_CREATE_ORDER_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("PayPal API error: " + body))
                    )
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("id") == null) {
                throw new RuntimeException("Invalid response from PayPal API - missing order ID");
            }

            return (String) response.get("id");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create PayPal order: " + e.getMessage(), e);
        }
    }

//    public String createPaypalOrder(String orderId, String merchantId, Double amount) {
//        String accessToken = getAccessToken();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-Type", "application/json");
//
//        Map<String, Object> purchaseUnit = new HashMap<>();
//        purchaseUnit.put("description", "Order ID: " + orderId + ", Merchant ID: " + merchantId);
//        purchaseUnit.put("amount", Map.of("currency_code", "USD", "value", amount));
//        purchaseUnit.put("reference_id", merchantId);
//
//        Map<String, Object> requestBody = Map.of("intent", "CAPTURE", "purchase_units", new Object[]{purchaseUnit});
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<Map> response = restTemplate.postForEntity(PAYPAL_CREATE_ORDER_URL, request, Map.class);
//
//        return (String) Objects.requireNonNull(response.getBody()).get("id");
//    }

    public Payment capturePayment(String paypalOrderId) {
        String accessToken = getAccessToken();

        Map response = webClient.post()
                .uri(PAYPAL_CAPTURE_ORDER_URL, paypalOrderId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Payment capture failed!");
        }

        String merchantId = extractMerchantId(response);
        String paypalAccountId = extractPaypalAccountId(response);
        Double amount = extractCapturedAmount(response);

        return savePayment(paypalOrderId, merchantId, paypalAccountId, amount);
    }

    private ResponseEntity<Map> executeCaptureRequest(String paypalOrderId, String accessToken) {
        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                PAYPAL_CAPTURE_ORDER_URL,
                HttpMethod.POST,
                request,
                Map.class,
                paypalOrderId
        );
    }

    private Map<String, Object> getResponseBody(ResponseEntity<Map> response) {
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Response body is null!");
        }
        return responseBody;
    }

    private String extractMerchantId(Map<String, Object> responseBody) {
        List<Map<String, Object>> purchaseUnits = getPurchaseUnits(responseBody);
        Map<String, Object> firstPurchaseUnit = purchaseUnits.get(0);

        String paypalAccountId = (String) firstPurchaseUnit.get("reference_id");
        if (paypalAccountId == null) {
            throw new RuntimeException("Merchant ID is missing in purchase unit!");
        }
        return paypalAccountId;
    }

    private String extractPaypalAccountId(Map<String, Object> responseBody) {
        Map<String, Object> paymentSource = (Map<String, Object>) responseBody.get("payment_source");
        if (paymentSource != null) {
            Map<String, Object> paypalDetails = (Map<String, Object>) paymentSource.get("paypal");
            if (paypalDetails != null) {
                String paypalAccountId = (String) paypalDetails.get("account_id");
                if (paypalAccountId != null) {
                    return paypalAccountId;
                }
            }
        }

        System.out.println("Warning: PayPal Account ID is missing in response.");
        return "Unknown PayPal Account";
    }

    private Double extractCapturedAmount(Map<String, Object> responseBody) {
        List<Map<String, Object>> purchaseUnits = getPurchaseUnits(responseBody);
        Map<String, Object> firstPurchaseUnit = purchaseUnits.get(0);

        List<Map<String, Object>> captures = getCaptures(firstPurchaseUnit);
        Map<String, Object> capture = captures.get(0);

        Map<String, Object> amountMap = (Map<String, Object>) capture.get("amount");
        if (amountMap == null) {
            throw new RuntimeException("Amount information is missing in capture!");
        }

        return Double.parseDouble((String) amountMap.get("value"));
    }

    private List<Map<String, Object>> getPurchaseUnits(Map<String, Object> responseBody) {
        List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) responseBody.get("purchase_units");
        if (purchaseUnits == null || purchaseUnits.isEmpty()) {
            throw new RuntimeException("No purchase units found in response!");
        }
        return purchaseUnits;
    }

    private List<Map<String, Object>> getCaptures(Map<String, Object> purchaseUnit) {
        Map<String, Object> payments = (Map<String, Object>) purchaseUnit.get("payments");
        List<Map<String, Object>> captures = (payments != null) ? (List<Map<String, Object>>) payments.get("captures") : null;

        if (captures == null || captures.isEmpty()) {
            throw new RuntimeException("No captures found in payments!");
        }
        return captures;
    }

    private Payment savePayment(String paypalOrderId, String merchantId, String paypalAccountId, Double amount) {
        Payment payment = new Payment();
        payment.setMerchantId(merchantId);
        payment.setPaypalAccountId(paypalAccountId);
        payment.setAmount(amount);
        payment.setOrderId(paypalOrderId);
        payment.setStatus("COMPLETED");
        payment.setTimestamp(LocalDateTime.now());

        paymentsRepository.save(payment);
        return payment;
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        return headers;
    }
}