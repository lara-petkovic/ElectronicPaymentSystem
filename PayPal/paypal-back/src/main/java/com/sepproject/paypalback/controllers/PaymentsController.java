package com.sepproject.paypalback.controllers;

import com.sepproject.paypalback.dtos.PaymentRequestDto;
import com.sepproject.paypalback.mappers.PaymentRequestMapper;
import com.sepproject.paypalback.models.Payment;
import com.sepproject.paypalback.services.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "https://localhost:4203")
@RequestMapping("/api/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private PaymentRequestMapper requestMapper;

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
    public ResponseEntity<?> capturePayment(@RequestParam String orderId) {
        try {
            Payment payment = paymentsService.capturePayment(orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to capture payment");
        }
    }
}