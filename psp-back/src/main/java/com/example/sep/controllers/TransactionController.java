package com.example.sep.controllers;

import com.example.sep.configuration.TradeWebSocketHandler;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.services.IClientService;
import com.example.sep.services.ITransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/transaction")

public class TransactionController {
    private final TradeWebSocketHandler tradeWebSocketHandler;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    public TransactionController(TradeWebSocketHandler tradeWebSocketHandler, IClientService clientService, ITransactionService transactionService) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
        this.clientService=clientService;
        this.transactionService=transactionService;
    }
    @PostMapping
    public NewTransactionDto CreateTransaction(@RequestBody NewTransactionDto transaction, HttpServletRequest request) throws Exception {
        String clientPort = request.getHeader("Port");
        if (clientPort != null) {
            System.out.println("Port: " + clientPort);
        } else {
            throw new IllegalArgumentException("not found port");
        }
        ClientSubscriptionDto clientSubscriptionDto = clientService.getSubscription(transaction, clientPort);
        Client c = clientService.getClientByPort(clientPort);
        if(clientSubscriptionDto != null) {
            tradeWebSocketHandler.broadcastMessage(clientSubscriptionDto.toString());
            this.transactionService.SaveTransaction(new Transaction(transaction, c.getMerchantId()));
        }
        return  transaction;
    }
}
