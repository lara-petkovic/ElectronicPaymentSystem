package com.example.bank.service.dto;

public class PaymentResponseDto {
    public String PaymentId;
    public String PaymentUrl;
    public PaymentResponseDto(String paymentId){
        PaymentId = paymentId;
        PaymentUrl = "http://localhost:4202/credit-card-input/"+paymentId.toString();
    }
}
