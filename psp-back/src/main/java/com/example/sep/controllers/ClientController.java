package com.example.sep.controllers;

import com.example.sep.configuration.ClientSubscriptionWebSocketHandler;
import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.NewClientDto;
import com.example.sep.models.PaymentOption;
import com.example.sep.services.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping(path="api/subscription")

public class ClientController {
    @Autowired
    private PaymentOptionService paymentOptionService;
    private final ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler;

    @Autowired
    public ClientController(ClientSubscriptionWebSocketHandler clientSubscriptionWebSocketHandler, PaymentOptionService paymentOptionService) {
        this.clientSubscriptionWebSocketHandler = clientSubscriptionWebSocketHandler;
        this.paymentOptionService=paymentOptionService;
    }
    @PostMapping
    public NewClientDto CreateClient(@RequestBody NewClientDto newClient) throws Exception {
        List<PaymentOption> paymentOptionList=paymentOptionService.getAll();
        String message=newClient.apiKey+",";
        for(int i=0;i<paymentOptionList.size()-1;i++){
            message+=paymentOptionList.get(i).getOption()+" ";
        }
        message+=paymentOptionList.get(paymentOptionList.size()-1).getOption();
        clientSubscriptionWebSocketHandler.broadcastMessage(message);
        return  newClient;
    }
}
