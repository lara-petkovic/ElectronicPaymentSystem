package com.example.bank.config;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.*;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class CreditCardWebSocketHandler extends TextWebSocketHandler {
    private WebSocketSession frontEndSession;
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
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection: " + session.getId());
        frontEndSession = session;
        frontEndSession.sendMessage(new TextMessage("Welcome to the Credit Card WebSocket!"));
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        CardDetailsDto cardDetailsDto = objectMapper.readValue(message.getPayload(), CardDetailsDto.class);

        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(cardDetailsDto.PaymentRequestId);
        Account merchantAccount = accountService.getMerchantAccount(paymentRequest);
        Transaction transaction = transactionService.getTransactionByPaymentRequestId(paymentRequest.getId());
        if(!cardDetailsDto.isValidExpirationDate()){
            emitFailedEvent(transaction);
        }
        if(merchantAccount!=null){
            if(checkPanNumber(cardDetailsDto)){
                Account issuerAccount = accountService.getIssuerAccount(cardDetailsDto);
                if(issuerAccount==null){
                    emitFailedEvent(transaction);
                    return;
                }
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.save(issuerAccount);
                    accountService.save(merchantAccount);
                    emitSuccessEvent(transaction);
                }
                else{
                    emitFailedEvent(transaction);
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
                    }
                    else if(response.getStatusCode()==HttpStatus.FORBIDDEN || response.getStatusCode()==HttpStatus.NOT_FOUND){
                        emitFailedEvent(returnedTransaction);
                    }
                    else{
                        emitErrorEvent(returnedTransaction);
                    }
                }
                catch(Exception e){
                    emitErrorEvent(transaction);
                }
            }
        }
        else{
            emitErrorEvent(transaction);
        }
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
    public void openCreditCardForm(int paymentId, double amount) throws Exception{
        frontEndSession.sendMessage(new TextMessage(paymentId + "," + amount));
    }















//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        try {
//            Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
//            String paymentOption = (String) data.get("name");
//            Integer orderid = (Integer) data.get("orderid");
//            String merchantid = (String) data.get("merchantid");
//
//            TransactionService transactionService=new TransactionService(this.transactionRepository);
//            Transaction transaction=transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantid,orderid);
//            System.out.println("Received payment opotion: " + paymentOption + ", ID: " + orderid+merchantid);
//
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "http://localhost:8086/api/payments";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("Payment", paymentOption);
//
//            // Create the JSON body
//            String body = "{ \"MerchantId\" : \"" + transaction.getMerchantId() + "\", " +
//                    "\"MerchantPassword\" : \"" + transaction.getMerchantPass() + "\", " +
//                    "\"Amount\" : \"" + transaction.getAmount() + "\", " +
//                    "\"MerchantOrderId\" : \"" + transaction.getOrderId() + "\", " +
//                    "\"MerchantTimestamp\" : \"" + transaction.getTimestamp() + "\", " +
//                    "\"SuccessUrl\" : \"http://localhost:4201/success\", " +
//                    "\"FailedUrl\" : \"http://localhost:4201/fail\", " +
//                    "\"Error\" : \"http://localhost:4201/error\" }";
//
//            // Set up the HTTP entity with headers and body
//            HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//            // Send the POST request
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//
//        } catch (Exception e) {
//            System.out.println("Parrsing error: " + e.getMessage());
//            session.sendMessage(new TextMessage("Invalid format!"));
//        }
//    }

}
