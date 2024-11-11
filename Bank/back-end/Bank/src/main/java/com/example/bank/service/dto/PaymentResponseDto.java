package com.example.bank.service.dto;

public class PaymentResponseDto {
    public Integer PaymentId;
    public String PaymentUrl;
    public PaymentResponseDto(Integer paymentId){
        PaymentId = paymentId;
        PaymentUrl = "http://localhost:4202/credit-card-input/"+paymentId.toString();
    }
}
