package com.example.bank.controllers;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.PaymentRequestService;
import com.example.bank.service.dto.PaymentRequestDto;
import com.example.bank.service.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPaymentRequest(@RequestBody PaymentRequestDto paymentRequest) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest);
        if(merchantAccountExists) {
            PaymentRequest newPR = paymentRequestService.addPaymentRequest(paymentRequest);
            return new ResponseEntity<>(new PaymentResponseDto(newPR.getId()), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

