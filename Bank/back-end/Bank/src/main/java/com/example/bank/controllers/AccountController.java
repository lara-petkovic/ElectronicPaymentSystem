package com.example.bank.controllers;

import com.example.bank.service.AccountService;
import com.example.bank.service.dto.MerchantRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    @PostMapping
    public ResponseEntity registerNewMerchant(@RequestBody MerchantRegistrationDto registrationDto) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(registrationDto.MerchantId, registrationDto.MerchantPassword);
        if(merchantAccountExists) {
            return new ResponseEntity(null, HttpStatus.NOT_ACCEPTABLE);
        }
        Boolean success = accountService.registerNewMerchant(registrationDto);
        if(success) {
            logger.info("New merchant with id "+registrationDto.MerchantId+ " added.");
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
        else{
            logger.error("Failed to register merchant with id "+registrationDto.MerchantId);
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
