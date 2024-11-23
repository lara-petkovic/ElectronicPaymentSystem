package com.example.bank.service.dto;

public class TransactionResultDto {
    public String orderId;
    public String responseUrl;
    public TransactionResultDto(String orderId, String responseUrl) {
        this.orderId = orderId;
        this.responseUrl = responseUrl;
    }
    public TransactionResultDto(){}
}
