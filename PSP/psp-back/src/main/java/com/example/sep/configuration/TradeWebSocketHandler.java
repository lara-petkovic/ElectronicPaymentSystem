package com.example.sep.configuration;

import com.example.sep.EncryptionUtil;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.repositories.TransactionRepository;
import com.example.sep.services.ClientService;
import com.example.sep.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradeWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private EncryptionUtil encryptionUtil;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
            sessions.add(session);
        }
    public void broadcastMessage(String message) throws Exception {
        List<WebSocketSession> closedSessions = new ArrayList<>();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            } else {
                closedSessions.add(session);
            }
        }
        sessions.removeAll(closedSessions);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
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

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {
            Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
            String paymentOption = (String) data.get("name");
            Integer orderid = (Integer) data.get("orderid");
            String merchantid = (String) data.get("merchantid");

            Client client = clientService.getClientByMerchantId(merchantid);
            TransactionService transactionService = new TransactionService(this.transactionRepository);
            Transaction transaction = transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantid, orderid);
            System.out.println("Received payment option: " + paymentOption + ", ID: " + orderid + merchantid);

            // Build the JSON body
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("MerchantId", transaction.getMerchantId());
            bodyMap.put("MerchantPassword", encryptionUtil.decrypt(client.getMerchantPass()));
            bodyMap.put("Amount", transaction.getAmount());
            bodyMap.put("MerchantOrderId", transaction.getOrderId());
            bodyMap.put("MerchantTimestamp", transaction.getTimestamp());
            bodyMap.put("SuccessUrl", "https://localhost:4201/success");
            bodyMap.put("FailedUrl", "https://localhost:4201/fail");
            bodyMap.put("ErrorUrl", "https://localhost:4201/error");

            // Use WebClient with SSL disabled
            String response = webClient.post()
                    .uri("https://localhost:8087/api/payments")
                    .header("Content-Type", "application/json")
                    .header("Payment", paymentOption)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking call here only if you're not reactive

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.out.println("Parrsing error: " + e.getMessage());
            session.sendMessage(new TextMessage("Invalid format!"));
        }
    }

}
