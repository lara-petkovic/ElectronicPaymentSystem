package com.example.sep.configuration;

import com.example.sep.repositories.ClientRepository;
import com.example.sep.repositories.PaymentOptionRepository;
import com.example.sep.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientSubscriptionWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

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
            String address = (String) data.get("clientId");
            String subscription = (String) data.get("name");
            String walletAddress = (String) data.get("walletAddress");
            createClient(subscription,address, walletAddress);

            System.out.println("Received subscription: " +  ", ID: " + address+subscription);

        } catch (Exception e) {
            System.out.println("Parrsing error: " + e.getMessage());
            session.sendMessage(new TextMessage("Invalid format!"));
        }
    }

    private void createClient(String subscription, String address, String walletAddress) {
        ClientService clientService = new ClientService(clientRepository, paymentOptionRepository);
        clientService.create(subscription, address, walletAddress);
    }
}
