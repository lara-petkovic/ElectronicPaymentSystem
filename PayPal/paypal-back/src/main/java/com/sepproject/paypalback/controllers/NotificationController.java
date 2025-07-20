package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.dtos.TransactionStatusDto;
import com.sepproject.paypalback.models.Merchant;
import com.sepproject.paypalback.models.PaypalMerchant;
import com.sepproject.paypalback.models.Transaction;
import com.sepproject.paypalback.models.TransactionRequestFromPsp;
import com.sepproject.paypalback.services.IMerchantService;
import com.sepproject.paypalback.services.IPaypalMerchantService;
import com.sepproject.paypalback.services.ITransactionService;
import com.sepproject.paypalback.services.NotificationService;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private IPaypalMerchantService paypalMerchantService;
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    public NotificationController(
            NotificationService notificationService,
            IMerchantService merchantService,
            ITransactionService transactionService
    ) {
        this.notificationService = notificationService;
        this.merchantService = merchantService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Merchant> register (@RequestBody Merchant merchant) {
        Merchant m = merchantService.create(merchant);

        if(m != null) {
            PaypalMerchant paypalMerchant = new PaypalMerchant(
                    m.getMerchantId(),
                    merchant.getMerchantPass(),
                    merchant.getPaypalClientId().split("/////")[0],
                    merchant.getPaypalClientId().split("/////")[1]
            );
            paypalMerchantService.create(paypalMerchant);
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{merchantId}/paypal-client-id")
    public ResponseEntity<String> getMerchantPayPalClientId(@PathVariable String merchantId) {
        PaypalMerchant paypalMerchant = paypalMerchantService.getByPaypalMerchantId(merchantId);
        return ResponseEntity.ok(paypalMerchant.getPaypalClientId());
    }

    @PostMapping("/pay")
    public String sendNotification(@RequestBody TransactionRequestFromPsp request) {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setOrderId(request.MerchantOrderId);
        paymentRequestDto.setMerchantId(request.MerchantId);
        paymentRequestDto.setAmount(request.Amount);

        notificationService.sendNotification(paymentRequestDto);
        return "Notification sent";
    }

    @PostMapping("/status")
    public ResponseEntity<String> handleTransactionStatus(@RequestParam String status, @RequestParam Long orderId) {
        try {
            String body = "{ \"responseUrl\": \"" + "https://localhost:4201/" + status + "\", \"orderId\": \"" + orderId + "\" }";

            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> {
                        try {
                            sslContextSpec.sslContext(
                                    SslContextBuilder.forClient()
                                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                            .build()
                            );
                        } catch (SSLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            WebClient webClient = WebClient.builder()
                    .baseUrl("https://localhost:8087")
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

            String response = webClient.post()
                    .uri("/api/response")
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Response: " + response);
            return ResponseEntity.ok("Transaction processed successfully");
        } catch (Exception e) {
            System.out.println("Error reaching PSP: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
