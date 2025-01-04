package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4203")
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping("/create")
    public ResponseEntity<?> createPaypalOrder(@RequestBody PaymentRequestDto request) {
        String paypalOrderId = paymentsService.createPaypalOrder(
                request.getOrderId(),
                request.getMerchantId(),
                request.getAmount()
        );

        return ResponseEntity.ok().body(Map.of("paypalOrderId", paypalOrderId));
    }

    @PostMapping("/capture")
    public ResponseEntity<?> capturePayment(@RequestBody PaymentRequestDto request) {
        Payment payment = paymentsService.capturePayment(request.getOrderId());

        return ResponseEntity.ok().body(payment);
    }
}