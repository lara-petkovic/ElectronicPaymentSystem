package com.example.bank.service;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Objects;

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
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(PaymentExecutionService.class);
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
            if(checkIfPanNumberBelongsToAcquirersBank(cardDetailsDto)){
                Account issuerAccount = accountService.getIssuerAccount(cardDetailsDto);
                if(issuerAccount==null){
                    emitFailedEvent(transaction);
                    return false;
                }
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.update(issuerAccount);
                    accountService.update(merchantAccount);
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
                String url = "https://pcc:8053/api/payments";

                PaymentRequestForIssuerDto issuerRequest = new PaymentRequestForIssuerDto(
                        cardDetailsDto.Pan,
                        cardDetailsDto.ExpirationDate,
                        cardDetailsDto.HolderName,
                        cardDetailsDto.SecurityCode,
                        transaction
                );
                String body = objectMapper.writeValueAsString(issuerRequest);
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
                        .baseUrl("https://api.coingecko.com/api/v3")
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .build();
                try {
                    Transaction response = webClient.post()
                            .uri(url)
                            .header("Content-Type", "application/json")
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Transaction.class)
                            .block(); // Blocking call here only if you're not reactive
                    System.out.println("Response: " + response);
                    if(response!=null && Objects.equals(response.getStatus(), "ISSUER_PAID")) {
                        merchantAccount.setBalance(merchantAccount.getBalance() + paymentRequest.getAmount());
                        accountService.update(merchantAccount);
                        emitSuccessEvent(response);
                        return true;
                    }
                    else {
                        emitFailedEvent(response);
                        return false;
                    }
                } catch (WebClientResponseException e) {
                    if(e.getStatusCode()== HttpStatusCode.valueOf(404) || e.getStatusCode()== HttpStatusCode.valueOf(403)){
                        emitFailedEvent(transaction);
                    }
                    else{
                        System.out.println("Error reaching pcc: " + e);
                        emitErrorEvent(transaction);
                    }
                    return false;
                }
                catch (Exception e){
                    System.out.println("Error reaching pcc: " + e);
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
        logger.error("Transaction with id "+transaction.getId()+" already paid");
        pspNotificationService.sendTransactionResult(transaction);
    }
    public void emitErrorEvent(Transaction transaction){
        logger.error("Error in transaction with id "+transaction.getId());
        pspNotificationService.sendTransactionResult(transactionService.errorTransaction(transaction));
    }
    public void emitSuccessEvent(Transaction transaction){
        logger.info("Successful transaction with id "+transaction.getId());
        pspNotificationService.sendTransactionResult(transactionService.successTransaction(transaction));
    }
    public void emitFailedEvent(Transaction transaction){
        logger.error("Failed transaction with id "+transaction.getId());
        pspNotificationService.sendTransactionResult(transactionService.failTransaction(transaction));
    }
    private boolean checkIfPanNumberBelongsToAcquirersBank(CardDetailsDto cardDetailsDto){
        String bankIdentifier = bankIdentifierService.getId();
        return cardDetailsDto.Pan.substring(0,4).equals(bankIdentifier);
    }
}
