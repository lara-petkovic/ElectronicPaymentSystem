package com.example.sep.configuration;

import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.repositories.TransactionRepository;
import com.example.sep.services.ClientService;
import com.example.sep.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradeWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
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
        try {
            Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
            String paymentOption = (String) data.get("name");
            Integer orderid = (Integer) data.get("orderid");
            String merchantid = (String) data.get("merchantid");

            Client client = clientService.getClientByMerchantId(merchantid);

            TransactionService transactionService=new TransactionService(this.transactionRepository);
            Transaction transaction=transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantid,orderid);
            System.out.println("Received payment opotion: " + paymentOption + ", ID: " + orderid+merchantid);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8087/api/payments";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Payment", paymentOption);

            // Create the JSON body
            String body = "{ \"MerchantId\" : \"" + transaction.getMerchantId() + "\", " +
                    "\"MerchantPassword\" : \"" + client.getMerchantPass() + "\", " +
                    "\"Amount\" : \"" + transaction.getAmount() + "\", " +
                    "\"MerchantOrderId\" : \"" + transaction.getOrderId() + "\", " +
                    "\"MerchantTimestamp\" : \"" + transaction.getTimestamp() + "\", " +
                    "\"SuccessUrl\" : \"http://localhost:4201/success\", " +
                    "\"FailedUrl\" : \"http://localhost:4201/fail\", " +
                    "\"ErrorUrl\" : \"http://localhost:4201/error\" }";

            // Set up the HTTP entity with headers and body
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // Send the POST request
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        } catch (Exception e) {
            System.out.println("Parrsing error: " + e.getMessage());
            session.sendMessage(new TextMessage("Invalid format!"));
        }
    }

}
