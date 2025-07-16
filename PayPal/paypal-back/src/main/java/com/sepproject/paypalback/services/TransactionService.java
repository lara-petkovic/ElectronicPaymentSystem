package com.sepproject.paypalback.services;

import com.sepproject.paypalback.dtos.NewTransactionDto;
import com.sepproject.paypalback.models.Merchant;
import com.sepproject.paypalback.models.Transaction;
import com.sepproject.paypalback.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private IMerchantService merchantService;
    private final SimpMessagingTemplate messagingTemplate;

    public TransactionService(SimpMessagingTemplate messagingTemplate, IMerchantService merchantService) {
        this.messagingTemplate = messagingTemplate;
        this.merchantService=merchantService;
    }

    @Override
    public Transaction create(NewTransactionDto newTransactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(newTransactionDto.getAmount());
        transaction.setTimestamp(newTransactionDto.getMerchantTimestamp());
        transaction.setMerchantId(newTransactionDto.getMerchantId());
        transaction.setMerchantOrderId(Long.valueOf(newTransactionDto.getMerchantOrderId()));
        transaction.setAmount(newTransactionDto.getAmount());

        Merchant m = merchantService.getByMerchantId(transaction.getMerchantId());
        if(m != null) {
            transaction = transactionRepository.save(transaction);
            String transactionToSend = transaction.getAmount() + "," + m.getPaypalClientId() + "," + transaction.getId() + "," + transaction.getAmount();
            messagingTemplate.convertAndSend("/topic/string-data", transactionToSend);
            return transaction;
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://localhost:8087/api/response";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{ \"responseUrl\": \"" + "fail" + "\", \"orderId\": \"" + transaction.getMerchantOrderId() + "\" }";

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return null;
        }
    }

    @Override
    public Transaction getById(Long id) {
        return transactionRepository.getTransactionById(id);
    }
}
