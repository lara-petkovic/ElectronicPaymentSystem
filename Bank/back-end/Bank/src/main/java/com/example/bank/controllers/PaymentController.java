package com.example.bank.controllers;

import com.example.bank.service.AccountService;
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
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPaymentRequest(@RequestBody PaymentRequestDto paymentRequest) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest);
        if(merchantAccountExists)
            return new ResponseEntity<>(new PaymentResponseDto(1), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

