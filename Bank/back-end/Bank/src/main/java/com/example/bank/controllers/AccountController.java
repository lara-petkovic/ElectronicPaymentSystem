package com.example.bank.controllers;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.dto.MerchantRegistrationDto;
import com.example.bank.service.dto.PaymentRequestDto;
import com.example.bank.service.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping
    public ResponseEntity registerNewMerchant(@RequestBody MerchantRegistrationDto registrationDto) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(registrationDto.MerchantId, registrationDto.MerchantPassword);
        if(merchantAccountExists) {
            return new ResponseEntity(null, HttpStatus.NOT_ACCEPTABLE);
        }
        Boolean success = accountService.registerNewMerchant(registrationDto);
        if(success)
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
}
