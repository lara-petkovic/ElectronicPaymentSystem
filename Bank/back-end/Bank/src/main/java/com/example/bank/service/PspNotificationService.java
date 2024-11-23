package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.example.bank.service.dto.TransactionResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PspNotificationService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    private String gatewayUrl = "http://localhost:8087/api/response";
    public void sendTransactionResult(Transaction transaction){
        PaymentRequest pr = paymentRequestService.getPaymentRequest(transaction.getPaymentRequestId());
        TransactionResultDto result = new TransactionResultDto();
        result.orderId = transaction.getMerchantOrderId();
        switch (transaction.getStatus()){
            case "SUCCESS": {result.url = pr.getSuccessUrl(); break;}
            case "FAILED": {result.url = pr.getFailedUrl(); break;}
            default: {result.url = pr.getErrorUrl(); break;}
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(result);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            restTemplate.exchange(gatewayUrl, HttpMethod.POST, entity, Object.class);
        }
        catch(Exception e){
            //nista
        }
    }
}
