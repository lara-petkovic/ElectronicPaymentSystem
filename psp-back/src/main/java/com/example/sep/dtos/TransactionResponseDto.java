package com.example.sep.dtos;

public class TransactionResponseDto {
    public String responseUrl;
    public long orderId;
    public TransactionResponseDto() {

    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
