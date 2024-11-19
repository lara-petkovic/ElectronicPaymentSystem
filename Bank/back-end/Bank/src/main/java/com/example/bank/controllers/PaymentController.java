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
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest);
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
    @PostMapping(path = "/pay-with-credit-card")
    public ResponseEntity executePaymentWithCardData(@RequestBody PaymentDto paymentDto){
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(paymentDto.PaymentRequestId);
        Account merchantAccount = accountService.getMerchantAccount(paymentRequest);
        Account issuerAccount = accountService.getIssuerAccount(paymentDto);
        if(merchantAccount!=null){
            if(issuerAccount!=null){
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.save(issuerAccount);
                    accountService.save(merchantAccount);
                    return new ResponseEntity(HttpStatus.OK);
                }
                else{
                    return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else{
                return null;//call issuers bank via pcc
            }
        }
        else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}

