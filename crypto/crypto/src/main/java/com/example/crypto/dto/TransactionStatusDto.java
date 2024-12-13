package com.example.crypto.dto;

public class TransactionStatusDto {
    private String status;
    private String details;
    private String transactionId;
    private String merchantId;

    public TransactionStatusDto(){}

    public TransactionStatusDto(String status, String details, String transactionId, String merchantId) {
        this.status = status;
        this.details = details;
        this.transactionId = transactionId;
        this.merchantId = merchantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}