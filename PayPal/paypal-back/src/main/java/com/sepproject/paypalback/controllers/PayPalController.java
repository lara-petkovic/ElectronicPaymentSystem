package com.sepproject.paypalback.controllers;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sepproject.paypalback.services.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/payment")
    public String createPayment(@RequestParam double total, @RequestParam String currency) {
        try {
            Payment payment = payPalService.createPayment(total, currency);
            return payment.toJSON();
        } catch (PayPalRESTException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/cancel")
    public String cancelPayment() {
        return "Payment was canceled by the user.";
    }

    @GetMapping("/success")
    public String successPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            return "Payment successful! Details: " + payment.toJSON();
        } catch (PayPalRESTException e) {
            return "Error occurred: " + e.getMessage();
        }
    }
}