package com.example.sep.controllers;

import com.example.sep.configuration.ClientSubscriptionWebSocketHandler;
import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.NewClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/subscription")

public class ClientController {
    private final ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler;

    @Autowired
    public ClientController(ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler) {
        this.clientSubscriptionWebSocketHandler = clientSubscriptionWebSocketHandler;
    }
    @PostMapping
    public NewClientDto CreateClient(@RequestBody NewClientDto newClient) throws Exception {
        clientSubscriptionWebSocketHandler.broadcastMessage("Client: " + newClient.apiKey);
        return  newClient;
    }
}