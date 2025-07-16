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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
                    m.getMerchantPass(),
                    m.getPaypalClientId().split("/////")[0],
                    m.getPaypalClientId().split("/////")[1]
            );
            PaypalMerchant pm = paypalMerchantService.create(paypalMerchant);
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

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://localhost:8087/api/response";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"responseUrl\": \"" + "https://localhost:4201/" + status + "\", \"orderId\": \"" + orderId + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        //logger.info("Transaction" +transactionStatusDto.getTransactionId()+"  processed "+transactionStatusDto.getStatus());

        return ResponseEntity.ok("Transaction processed successfully");
    }
}
