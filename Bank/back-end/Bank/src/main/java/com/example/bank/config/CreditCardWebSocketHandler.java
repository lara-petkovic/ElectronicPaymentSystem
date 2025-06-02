package com.example.bank.config;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.*;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.example.bank.service.dto.QRCodeInformationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class CreditCardWebSocketHandler extends TextWebSocketHandler {
    private WebSocketSession frontEndSession;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private BankIdentifierNumberService bankIdentifierService;
    @Autowired
    private PaymentExecutionService paymentExecutionService;
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
        paymentExecutionService.executePayment(cardDetailsDto);
    }
    public void openCreditCardForm(String paymentId, double amount) throws Exception{
        frontEndSession.sendMessage(new TextMessage(paymentId + "," + amount));
    }
    public void resetPage(){
        try{
            frontEndSession.sendMessage(new TextMessage(""));
        }
        catch(Exception e){}
    }
    public void openPaymentQR(String paymentId, double amount) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        String url = "https://nbs.rs/QRcode/api/qr/v1/gen?lang=sr_RS";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentRequest pr = paymentRequestService.getPaymentRequest(paymentId);
        Account ac = accountService.getMerchantAccount(pr);

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(new QRCodeInformationDto(ac.getNumber(), ac.getCardHolderName(), pr.getAmount(), pr.getId()));

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        try{
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                byte[] imageBytes = response.getBody();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                frontEndSession.sendMessage(new TextMessage("qr:"+base64Image));
                System.out.println("qr:"+base64Image);
            } else {
                System.out.println("Failed to retrieve the image.");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
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
