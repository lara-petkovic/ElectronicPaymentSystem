package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.dto.TransactionResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Service
public class PspNotificationService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    private String gatewayUrl = "https://localhost:8087/api/response";
    public void sendTransactionResult(Transaction transaction){
        PaymentRequest pr = paymentRequestService.getPaymentRequest(transaction.getPaymentRequestId());
        TransactionResultDto result = new TransactionResultDto();
        result.orderId = transaction.getMerchantOrderId();
        switch (transaction.getStatus()){
            case "SUCCESS": {result.responseUrl = pr.getSuccessUrl(); break;}
            case "FAILED": {result.responseUrl = pr.getFailedUrl(); break;}
            default: {result.responseUrl = pr.getErrorUrl(); break;}
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(result);
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
            Transaction response = webClient.post()
                    .uri(gatewayUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Transaction.class)
                    .block(); // Blocking call here only if you're not reactive
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("Error reaching psp: " + e);
        }
    }
}
