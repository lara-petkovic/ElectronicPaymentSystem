package com.example.crypto.controller;

import com.example.crypto.model.Transaction;
import com.example.crypto.service.ITransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService){
        this.transactionService=transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> CreateTransaction(@RequestBody Transaction transaction) throws Exception {

        Transaction t=transactionService.create(transaction);
        return  new ResponseEntity<>(t, HttpStatus.CREATED);
    }
}
