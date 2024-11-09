package com.example.sep.configuration;

import com.example.sep.dtos.TransactionToAcquirerDto;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.repositories.TransactionRepository;
import com.example.sep.services.ClientService;
import com.example.sep.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            sessions.add(session);
        }
    public void broadcastMessage(String message) throws Exception{
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
            String name = (String) data.get("name");
            Integer orderid = (Integer) data.get("orderid");
            String merchantid = (String) data.get("merchantid");
            System.out.println(orderid+" "+merchantid);

            TransactionService transactionService=new TransactionService(this.transactionRepository);
            Transaction transaction=transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantid,orderid);
            TransactionToAcquirerDto transactionToAcquirerDto=new TransactionToAcquirerDto(transaction, name);
            //ovde slati za dalje


            System.out.println("Received payment opotion: " + name + ", ID: " + orderid+merchantid);

        } catch (Exception e) {
            System.out.println("Parrsing error: " + e.getMessage());
            session.sendMessage(new TextMessage("Invalid format!"));
        }
    }

}
