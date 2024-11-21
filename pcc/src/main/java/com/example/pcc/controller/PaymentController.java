package com.example.pcc.controller;

import com.example.pcc.domain.model.RegisteredBank;
import com.example.pcc.service.RegisteredBankService;
import com.example.pcc.service.dto.PaymentRequestDto;
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
    public ResponseEntity<String> executePayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        RegisteredBank bank = service.getByPan(paymentRequestDto.Pan);
        if(bank==null)
            return new ResponseEntity<>("account does not exist", HttpStatus.OK);
        RestTemplate restTemplate = new RestTemplate();
        String url = bank.getUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"Pan\" : \"" + paymentRequestDto.Pan + "\", " +
                "\"ExpirationDate\" : \"" + paymentRequestDto.ExpirationDate + "\", " +
                "\"HolderName\" : \"" + paymentRequestDto.HolderName + "\", " +
                "\"SecurityCode\" : \"" + paymentRequestDto.SecurityCode + "\", " +
                "\"Acquirer\" : \"" + paymentRequestDto.Acquirer + "\", " +
                "\"AcquirerAccountNumber\" : \"" + paymentRequestDto.AcquirerAccountNumber + "\", " +
                "\"Amount\" : \"" + paymentRequestDto.Amount +"\" }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response;
    }
}
