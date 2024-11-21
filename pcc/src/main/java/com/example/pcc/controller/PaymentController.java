package com.example.pcc.controller;

import com.example.pcc.domain.model.RegisteredBank;
import com.example.pcc.service.RegisteredBankService;
import com.example.pcc.service.dto.PaymentRequestDto;
import com.example.pcc.service.dto.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private RegisteredBankService service;

    @PostMapping
    public ResponseEntity<TransactionDto> executePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        RegisteredBank bank = service.getByPan(paymentRequestDto.Pan);
        if(bank==null)
            return new ResponseEntity<>(paymentRequestDto.Transaction, HttpStatus.NOT_FOUND);
        RestTemplate restTemplate = new RestTemplate();
        String url = bank.getUrl();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(paymentRequestDto);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<TransactionDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, TransactionDto.class);
            return response;
        }
        catch (Exception e){
            return new ResponseEntity<>(paymentRequestDto.Transaction, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
