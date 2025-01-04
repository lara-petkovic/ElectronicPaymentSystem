package com.example.bank.controllers;

import com.example.bank.config.CreditCardWebSocketHandler;
import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.*;
import com.example.bank.service.dto.*;
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
    private PaymentExecutionService paymentExecutionService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private BankIdentifierNumberService bankIdentifierNumberService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    public PaymentController(CreditCardWebSocketHandler handler){
        creditCardWebSocket = handler;
    }
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPaymentRequest(@RequestBody PaymentRequestForAcquirerDto paymentRequest) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest.MerchantId,paymentRequest.MerchantPassword);
        if(merchantAccountExists) {
            PaymentRequest newPR = paymentRequestService.addPaymentRequest(paymentRequest);
            try{
                creditCardWebSocket.openCreditCardForm(newPR.getId(), newPR.getAmount());
                transactionService.addTransaction_Acquirer(newPR);
                return new ResponseEntity<>(new PaymentResponseDto(newPR.getId()), HttpStatus.CREATED);
            }
            catch (Exception e){
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/qr")
    public ResponseEntity<PaymentResponseDto> processPaymentRequestQR(@RequestBody PaymentRequestForAcquirerDto paymentRequest) {
        Boolean merchantAccountExists = accountService.checkIfMerchantAccountExists(paymentRequest.MerchantId,paymentRequest.MerchantPassword);
        if(merchantAccountExists) {
            PaymentRequest newPR = paymentRequestService.addPaymentRequest(paymentRequest);
            try{
                creditCardWebSocket.openPaymentQR(newPR.getId(), newPR.getAmount());
                transactionService.addTransaction_Acquirer(newPR);
                return new ResponseEntity<>(new PaymentResponseDto(newPR.getId()), HttpStatus.CREATED);
            }
            catch (Exception e){
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/pay")
    public ResponseEntity<Transaction> pay(@RequestBody PaymentRequestForIssuerDto paymentRequestForIssuerDto) {
        if(checkPanNumber(paymentRequestForIssuerDto)){
            Account issuerAccount = accountService.getIssuerAccount(paymentRequestForIssuerDto);
            if(issuerAccount==null){
                return new ResponseEntity<>(paymentRequestForIssuerDto.Transaction, HttpStatus.NOT_FOUND);
            }
            if(issuerAccount.getBalance()< paymentRequestForIssuerDto.Transaction.getAmount()) {
                paymentRequestForIssuerDto.Transaction = transactionService.addFailedTransaction_Issuer(paymentRequestForIssuerDto);
                return new ResponseEntity<>(paymentRequestForIssuerDto.Transaction, HttpStatus.FORBIDDEN);
            }
            paymentRequestForIssuerDto.Transaction = transactionService.addTransaction_Issuer(paymentRequestForIssuerDto);
            issuerAccount.setBalance(issuerAccount.getBalance()- paymentRequestForIssuerDto.Transaction.getAmount());
            accountService.save(issuerAccount);
            return new ResponseEntity<>(paymentRequestForIssuerDto.Transaction, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(paymentRequestForIssuerDto.Transaction, HttpStatus.FORBIDDEN);
        }
    }
    @PostMapping("/payWithQr")
    public ResponseEntity<Boolean> payWithQr(@RequestBody CardDetailsDto cardDetails) {
        try{
            Boolean result = paymentExecutionService.executePayment(cardDetails);
            if(result)
                return new ResponseEntity<>(result, HttpStatus.OK);
            else
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
    }
    private boolean checkPanNumber(PaymentRequestForIssuerDto paymentRequestForIssuerDto){
        String bankIdentifier = bankIdentifierNumberService.getId();
        return paymentRequestForIssuerDto.Pan.substring(0,4).equals(bankIdentifier);
    }
}

