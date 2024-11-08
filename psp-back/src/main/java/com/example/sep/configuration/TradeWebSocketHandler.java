package com.example.sep.configuration;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradeWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            TextMessage message = new TextMessage("aaa");
            session.sendMessage(message);
            Thread.sleep(1000);
            sessions.add(session);
        }
    public void broadcastMessage(String message) throws Exception{
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }
}
