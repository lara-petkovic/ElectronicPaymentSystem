package com.example.bank.config;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.BankIdentifierNumberService;
import com.example.bank.service.PaymentExecutionService;
import com.example.bank.service.PaymentRequestService;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.QRCodeInformationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
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
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            CardDetailsDto cardDetailsDto = objectMapper.readValue(message.getPayload(), CardDetailsDto.class);
            paymentExecutionService.executePayment(cardDetailsDto);
        }
        catch (Exception e){
            System.out.println("Invalid message from ws: "+message.getPayload());
        }
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
        //restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        String url = "https://nbs.rs/QRcode/api/qr/v1/gen?lang=sr_RS";

        PaymentRequest pr = paymentRequestService.getPaymentRequest(paymentId);
        Account ac = accountService.getMerchantAccount(pr);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new QRCodeInformationDto(ac.getNumber(), ac.getCardHolderName(), pr.getAmount(), pr.getId()));

        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                            try {
                                sslContextSpec
                                        .sslContext(
                                                SslContextBuilder.forClient()
                                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                        .build()
                                        );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {
            ResponseEntity<byte[]> responseEntity = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(json)
                    .retrieve()
                    .toEntity(byte[].class)
                    .block();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                byte[] imageBytes = responseEntity.getBody();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                frontEndSession.sendMessage(new TextMessage("qr:" + base64Image));
            } else {
                System.out.println("Failed to retrieve the image.");
            }
        } catch (Exception e) {
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
