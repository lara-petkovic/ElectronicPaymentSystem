package com.example.bank.service;

import com.example.bank.config.CustomResponseErrorHandler;
import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class PaymentExecutionService {
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BankIdentifierNumberService bankIdentifierService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PspNotificationService pspNotificationService;
    public boolean executePayment(CardDetailsDto cardDetailsDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(cardDetailsDto.PaymentRequestId);
        Account merchantAccount = accountService.getMerchantAccount(paymentRequest);
        Transaction transaction = transactionService.getTransactionByPaymentRequestId(paymentRequest.getId());
        if(transaction.getStatus().equals("SUCCESS")){
            emitAlreadyPaidEvent(transaction);
            return false;
        }
        if(!cardDetailsDto.isValidExpirationDate()){
            emitFailedEvent(transaction);
            return false;
        }
        if(merchantAccount!=null){
            if(checkPanNumber(cardDetailsDto)){
                Account issuerAccount = accountService.getIssuerAccount(cardDetailsDto);
                if(issuerAccount==null){
                    emitFailedEvent(transaction);
                    return false;
                }
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.save(issuerAccount);
                    accountService.save(merchantAccount);
                    emitSuccessEvent(transaction);
                    return true;
                }
                else{
                    emitFailedEvent(transaction);
                    return false;
                }
            }
            else{
                //call issuers bank via pcc
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new CustomResponseErrorHandler());
                String url = "http://localhost:8053/api/payments";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                PaymentRequestForIssuerDto issuerRequest = new PaymentRequestForIssuerDto(
                        cardDetailsDto.Pan,
                        cardDetailsDto.ExpirationDate,
                        cardDetailsDto.HolderName,
                        cardDetailsDto.SecurityCode,
                        transaction
                );

                String body = objectMapper.writeValueAsString(issuerRequest);

                HttpEntity<String> entity = new HttpEntity<>(body, headers);
                try{
                    ResponseEntity<Transaction> response = restTemplate.exchange(url, HttpMethod.POST, entity, Transaction.class);
                    Transaction returnedTransaction = response.getBody();
                    if(response.getStatusCode()==HttpStatus.OK){
                        merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                        accountService.save(merchantAccount);
                        emitSuccessEvent(returnedTransaction);
                        return true;
                    }
                    else if(response.getStatusCode()==HttpStatus.FORBIDDEN || response.getStatusCode()==HttpStatus.NOT_FOUND){
                        emitFailedEvent(returnedTransaction);
                        return false;
                    }
                    else{
                        emitErrorEvent(returnedTransaction);
                        return false;
                    }
                }
                catch(Exception e){
                    emitErrorEvent(transaction);
                    return false;
                }
            }
        }
        else{
            emitErrorEvent(transaction);
            return false;
        }
    }
    public void emitAlreadyPaidEvent(Transaction transaction){
        pspNotificationService.sendTransactionResult(transaction);
    }
    public void emitErrorEvent(Transaction transaction){
        pspNotificationService.sendTransactionResult(transactionService.errorTransaction(transaction));
    }
    public void emitSuccessEvent(Transaction transaction){
        pspNotificationService.sendTransactionResult(transactionService.successTransaction(transaction));
    }
    public void emitFailedEvent(Transaction transaction){
        pspNotificationService.sendTransactionResult(transactionService.failTransaction(transaction));
    }
    private boolean checkPanNumber(CardDetailsDto cardDetailsDto){
        String bankIdentifier = bankIdentifierService.getId();
        return cardDetailsDto.Pan.substring(0,4).equals(bankIdentifier);
    }
}
