package com.example.crypto.controller;

import com.example.crypto.dto.TransactionStatusDto;
import com.example.crypto.model.Transaction;
import com.example.crypto.service.ITransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @PostMapping("/status")
    public ResponseEntity<String> handleTransactionStatus(@RequestBody TransactionStatusDto transactionStatusDto) {

        System.out.println(transactionStatusDto.getDetails());

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8087/api/response";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"responseUrl\": \"" + transactionStatusDto.getStatus() + "\", \"orderId\": \"" + transactionStatusDto.getTransactionId() + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return ResponseEntity.ok("Transaction processed successfully");
    }
}
