package com.example.sep.controllers;

import com.example.sep.configuration.TradeWebSocketHandler;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.services.IClientService;
import com.example.sep.services.ITransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/transaction")

public class TransactionController {
    private final TradeWebSocketHandler tradeWebSocketHandler;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ITransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);


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
        if(clientPort.length()>4)
            clientPort=clientPort.substring(0, 4);

        if(clientService.getClientByPort(clientPort)!=null) {
            ClientSubscriptionDto clientSubscriptionDto = clientService.getSubscription(transaction, clientPort);
            Client c = clientService.getClientByPort(clientPort);
            if (c == null) {
                logger.warn("Invalid client");
            }
            if (clientSubscriptionDto != null) {
                logger.info("New transaction request for merchant with id: " + c.getMerchantId() + ", amount: " + transaction.getAmount());
                tradeWebSocketHandler.broadcastMessage(clientSubscriptionDto.toString());
                this.transactionService.SaveTransaction(new Transaction(transaction, c.getMerchantId()));
            }
            return transaction;
        }else{
            logger.warn("Client not found, port:"+clientPort);
            return null;
        }
    }
}
