package com.example.pcc.service.dto;

public class TransactionDto {
    public int id;
    public double amount;
    public String merchantOrderId;
    public String merchantTimestamp;
    public String acquirerOrderId;
    public String acquirerTimestamp;
    public String issuerOrderId;
    public String issuerTimestamp;
    public String status;
    public String paymentRequestId;
    public TransactionDto(){}

    public TransactionDto(int id, double amount, String merchantOrderId, String merchantTimestamp, String acquirerOrderId, String acquirerTimestamp, String issuerOrderId, String issuerTimestamp, String status, String paymentRequestId) {
        this.id = id;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.acquirerOrderId = acquirerOrderId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.issuerOrderId = issuerOrderId;
        this.issuerTimestamp = issuerTimestamp;
        this.status = status;
        this.paymentRequestId = paymentRequestId;
    }
}
