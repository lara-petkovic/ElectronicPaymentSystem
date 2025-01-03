package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDto request) {
        Payment payment = paymentsService.processPayment(
                request.getOrderId(),
                request.getMerchantId(),
                request.getAmount(),
                "COMPLETED"
        );

        return ResponseEntity.status(201).body(payment);
    }
}