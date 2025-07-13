package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.models.Merchant;
import com.sepproject.paypalback.models.TransactionRequestFromPsp;
import com.sepproject.paypalback.services.IMerchantService;
import com.sepproject.paypalback.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    private IMerchantService merchantService;

    @Autowired
    public NotificationController(NotificationService notificationService, IMerchantService merchantService) {
        this.notificationService = notificationService;
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<Merchant> register (@RequestBody Merchant merchant) {
        Merchant m = merchantService.create(merchant);
        if(m != null) {
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PostMapping
//    public String sendNotification(@RequestBody TransactionRequestFromPsp request) {
//        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
//        paymentRequestDto.setOrderId(request.MerchantOrderId);
//        paymentRequestDto.setMerchantId(request.MerchantId);
//        paymentRequestDto.setAmount(request.Amount);
//
//        notificationService.sendNotification(paymentRequestDto);
//        return "Notification sent";
//    }
}
