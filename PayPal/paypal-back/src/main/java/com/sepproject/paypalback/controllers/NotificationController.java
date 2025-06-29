package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.models.TransactionRequestFromPsp;
import com.sepproject.paypalback.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public String sendNotification(@RequestBody TransactionRequestFromPsp request) {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setOrderId(request.MerchantOrderId);
        paymentRequestDto.setMerchantId(request.MerchantId);
        paymentRequestDto.setAmount(request.Amount);

        notificationService.sendNotification(paymentRequestDto);
        return "Notification sent";
    }
}
