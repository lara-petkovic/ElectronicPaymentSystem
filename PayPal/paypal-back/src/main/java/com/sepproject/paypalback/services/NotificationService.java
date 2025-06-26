package com.sepproject.paypalback.services;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(PaymentRequestDto paymentRequestDto) {
        System.out.println("Sending notification");
        messagingTemplate.convertAndSend("/topic/notification", paymentRequestDto);
    }
}
