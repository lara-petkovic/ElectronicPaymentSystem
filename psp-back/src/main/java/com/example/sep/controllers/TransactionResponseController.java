package com.example.sep.controllers;

import com.example.sep.configuration.TransactionResponseHandler;
import com.example.sep.dtos.TransactionResponseDto;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.services.ClientService;
import com.example.sep.services.TransactionService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path="api/response")
public class TransactionResponseController {
    private final TransactionResponseHandler transactionResponseHandler;
    private final TransactionService transactionService;
    private final ClientService clientService;

    public TransactionResponseController(TransactionResponseHandler transactionResponseHandler, TransactionService transactionService, ClientService clientService) {
        this.transactionResponseHandler = transactionResponseHandler;
        this.transactionService = transactionService;
        this.clientService = clientService;
    }

    @PostMapping
    public void HandleTransactionResponse(@RequestBody TransactionResponseDto transactionResponseDto) throws Exception {
        System.out.println(transactionResponseDto.responseUrl);
        transactionResponseHandler.broadcastMessage(transactionResponseDto.responseUrl);
        if(transactionResponseDto.responseUrl.contains("success")) {
            Transaction transaction = transactionService.GetTransactionByOrderId(transactionResponseDto.orderId);
            Client client = clientService.getClientByMerchantId(transaction.getMerchantId());

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:" + client.getPort() + "/api/response";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{ \"MerchantOrderId\" : \"" + transaction.getOrderId() + "\" }";

            // Set up the HTTP entity with headers and body
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // Send the POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        }
    }
}
