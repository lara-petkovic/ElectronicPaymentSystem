package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private PaymentsService paymentService;

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequestDto request) {
        Payment payment = paymentService.processPayment(
                request.getOrderId(),
                request.getMerchantId(),
                request.getAmount(),
                "COMPLETED"
        );

        return ResponseEntity.ok(payment);
    }
}