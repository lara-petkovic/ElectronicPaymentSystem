package com.example.sep.controllers;

import com.example.sep.configuration.TradeWebSocketHandler;
import com.example.sep.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/transaction")

public class TransactionController {
    private final TradeWebSocketHandler tradeWebSocketHandler;

    @Autowired
    public TransactionController(TradeWebSocketHandler tradeWebSocketHandler) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
    }
    @PostMapping
    public Transaction CreateTransaction(@RequestBody Transaction transaction) throws Exception {
        tradeWebSocketHandler.broadcastMessage("New transaction created: " + transaction);

        return  transaction;
    }

}
