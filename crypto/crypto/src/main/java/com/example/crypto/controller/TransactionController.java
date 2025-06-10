package com.example.crypto.controller;

import com.example.crypto.dto.NewTransactionDto;
import com.example.crypto.dto.TransactionStatusDto;
import com.example.crypto.model.Transaction;
import com.example.crypto.service.ITransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(ITransactionService transactionService){
        this.transactionService=transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> CreateTransaction(@RequestBody NewTransactionDto transaction) throws Exception {
        Transaction t=transactionService.create(transaction);
        logger.info("New transaction for merchant: "+transaction.MerchantId+" , amount: "+transaction.Amount);
        return  new ResponseEntity<>(t, HttpStatus.CREATED);
    }

    @PostMapping("/status")
    public ResponseEntity<String> handleTransactionStatus(@RequestBody TransactionStatusDto transactionStatusDto) {

        System.out.println(transactionStatusDto.getTransactionId());
        System.out.println(Long.parseLong(transactionStatusDto.getTransactionId()));
        Transaction t=transactionService.getById(Long.parseLong(transactionStatusDto.getTransactionId()));

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://localhost:8087/api/response";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"responseUrl\": \"" + "https://localhost:4201/"+transactionStatusDto.getStatus() + "\", \"orderId\": \"" + t.getMerchantOrderId() + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        logger.info("Transaction" +transactionStatusDto.getTransactionId()+"  processed "+transactionStatusDto.getStatus());

        return ResponseEntity.ok("Transaction processed successfully");
    }
}
