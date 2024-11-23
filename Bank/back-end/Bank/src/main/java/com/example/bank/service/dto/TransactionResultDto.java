package com.example.bank.service.dto;

import com.example.bank.domain.model.Transaction;

public class TransactionResultDto {
    public String orderId;
    public String url;

    public TransactionResultDto(String orderId, String url) {
        this.orderId = orderId;
        this.url = url;
    }
    public TransactionResultDto(){}
}
