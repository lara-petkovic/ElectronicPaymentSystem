package com.example.sep.configuration;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionResponseHandler extends TextWebSocketHandler {
    private WebSocketSession sessions;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions=session;
    }
    public void broadcastMessage(String message) throws Exception{
       // for (WebSocketSession session : sessions) {
            sessions.sendMessage(new TextMessage(message));
     //   }
    }

}
