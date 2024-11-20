package com.example.bank.controllers;

import com.example.bank.config.CreditCardWebSocketHandler;
import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.PaymentRequestService;
import com.example.bank.service.dto.PaymentDto;
import com.example.bank.service.dto.PaymentRequestDto;
import com.example.bank.service.dto.PaymentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final CreditCardWebSocketHandler creditCardWebSocket;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    public PaymentController(CreditCardWebSocketHandler handler){
        creditCardWebSocket = handler;
    }
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPaymentRequest(@RequestBody PaymentRequestDto paymentRequest) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest.MerchantId,paymentRequest.MerchantPassword);
        if(merchantAccountExists) {
            PaymentRequest newPR = paymentRequestService.addPaymentRequest(paymentRequest);
            try{
                creditCardWebSocket.openCreditCardForm(newPR.getId(), newPR.getAmount());
                return new ResponseEntity<>(new PaymentResponseDto(newPR.getId()), HttpStatus.CREATED);
            }
            catch (Exception e){
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

