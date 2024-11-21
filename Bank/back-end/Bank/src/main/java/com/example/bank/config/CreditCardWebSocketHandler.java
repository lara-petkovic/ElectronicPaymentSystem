package com.example.bank.config;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.BankIdentifierNumberService;
import com.example.bank.service.PaymentRequestService;
import com.example.bank.service.dto.PaymentWithCardDto;
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
        PaymentWithCardDto paymentWithCardDto = objectMapper.readValue(message.getPayload(), PaymentWithCardDto.class);
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(paymentWithCardDto.PaymentRequestId);
        Account merchantAccount = accountService.getMerchantAccount(paymentRequest);

        if(merchantAccount!=null){
            if(checkPanNumber(paymentWithCardDto)){
                Account issuerAccount = accountService.getIssuerAccount(paymentWithCardDto);
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.save(issuerAccount);
                    accountService.save(merchantAccount);
                    emitSuccessEvent(paymentRequest);
                }
                else{
                    emitFailedEvent(paymentRequest);
                }
            }
            else{
                //call issuers bank via pcc
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8053/api/payments";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                String body = "{ \"Pan\" : \"" + paymentWithCardDto.Pan + "\", " +
                        "\"ExpirationDate\" : \"" + paymentWithCardDto.ExpirationDate + "\", " +
                        "\"HolderName\" : \"" + paymentWithCardDto.HolderName + "\", " +
                        "\"SecurityCode\" : \"" + paymentWithCardDto.SecurityCode + "\", " +
                        "\"Acquirer\" : \"" + merchantAccount.getCardHolderName() + "\", " +
                        "\"AcquirerAccountNumber\" : \"" + merchantAccount.getNumber() + "\", " +
                        "\"Amount\" : \"" + paymentRequest.getAmount() +"\" }";

                HttpEntity<String> entity = new HttpEntity<>(body, headers);
                try{
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                    if(response.getBody().equals("success")){
                        merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                        accountService.save(merchantAccount);
                        emitSuccessEvent(paymentRequest);
                    }
                    else if(response.getBody().equals("not enough money")){
                        emitFailedEvent(paymentRequest);
                    }
                    else{
                        emitErrorEvent(paymentRequest);
                    }
                }
                catch(Exception e){
                    emitErrorEvent(paymentRequest);
                }
            }
        }
        else{
            emitErrorEvent(paymentRequest);
        }
    }
    public void emitErrorEvent(PaymentRequest paymentRequest){

    }
    public void emitSuccessEvent(PaymentRequest paymentRequest){

    }
    public void emitFailedEvent(PaymentRequest paymentRequest){

    }
    private boolean checkPanNumber(PaymentWithCardDto paymentWithCardDto){
        String bankIdentifier = bankIdentifierService.getId();
        return paymentWithCardDto.Pan.substring(0,4).equals(bankIdentifier);
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
