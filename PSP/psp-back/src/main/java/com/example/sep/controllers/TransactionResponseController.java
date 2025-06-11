package com.example.sep.controllers;

import com.example.sep.configuration.TransactionResponseHandler;
import com.example.sep.dtos.TransactionResponseDto;
import com.example.sep.models.Client;
import com.example.sep.models.Transaction;
import com.example.sep.services.ClientService;
import com.example.sep.services.TransactionService;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.TextMessage;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="api/response")
public class TransactionResponseController {
    private final TransactionResponseHandler transactionResponseHandler;
    private final TransactionService transactionService;
    private final ClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionResponseController.class);

    public TransactionResponseController(TransactionResponseHandler transactionResponseHandler, TransactionService transactionService, ClientService clientService) {
        this.transactionResponseHandler = transactionResponseHandler;
        this.transactionService = transactionService;
        this.clientService = clientService;
    }

    @GetMapping("/by-merchant-order-id/{merchantOrderId}/order-id/{orderId}")
    public ResponseEntity<Transaction> getTransactionByMerchantOrderIdAndOrderId(
            @PathVariable String merchantOrderId,
            @PathVariable Integer orderId) {
        Transaction transaction = transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantOrderId, orderId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public void HandleTransactionResponse(@RequestBody TransactionResponseDto transactionResponseDto) throws Exception {
        String status = "";

        System.out.println(transactionResponseDto.responseUrl);
        transactionResponseHandler.broadcastMessage(transactionResponseDto.responseUrl);

        if(transactionResponseDto.responseUrl.contains("success")) {
            status = "success";
            logger.info("Success transaction "+transactionResponseDto.getOrderId());
        }
        if(transactionResponseDto.responseUrl.contains("fail")) {
            status = "fail";
            logger.info("Failed transaction "+transactionResponseDto.getOrderId());

        }
        if(transactionResponseDto.responseUrl.contains("error")) {
            status = "error";
            logger.info("Error during transaction "+transactionResponseDto.getOrderId());

        }

        Transaction transaction = transactionService.GetTransactionByOrderId(transactionResponseDto.orderId);
        Client client = clientService.getClientByMerchantId(transaction.getMerchantId());
        notifyWebShop(client,transaction,status);

//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://localhost:" + client.getPort() + "/api/response";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String body = "{ \"MerchantOrderId\": \"" + transaction.getOrderId() + "\", \"Status\": \"" + status + "\" }";
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private void notifyWebShop(Client client, Transaction transaction, String status){
        String body = "{ \"MerchantOrderId\": \"" + transaction.getOrderId() + "\", \"Status\": \"" + status + "\" }";
        String url = "https://localhost:" + client.getPort() + "/api/response";
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                            try {
                                sslContextSpec
                                        .sslContext(
                                                SslContextBuilder.forClient()
                                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                        .build()
                                        );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {

            String response = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Blocking call here only if you're not reactive

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.out.println("Error notifying web shop: " + e);
        }
    }
}
