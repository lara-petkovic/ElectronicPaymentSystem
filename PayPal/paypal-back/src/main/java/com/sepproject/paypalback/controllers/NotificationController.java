package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.NewTransactionDto;
import com.sepproject.paypalback.dtos.PaymentRequestDto;
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
    public String sendNotification(@RequestBody PaymentRequestDto paymentRequestDto) {
        notificationService.sendNotification(paymentRequestDto);
        return "Notification sent";
    }
}
