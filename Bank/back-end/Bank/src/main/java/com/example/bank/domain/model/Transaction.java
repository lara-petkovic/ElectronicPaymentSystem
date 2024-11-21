package com.example.bank.domain.model;

import jakarta.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="amount")
    private double amount;
    @Column(name="merchant_order_id")
    private String merchantOrderId;
    @Column(name="merchant_timestamp")
    private String merchantTimestamp;
    @Column(name="acquirer_order_id")
    private String acquirerOrderId;
    @Column(name="acquirer_timestamp")
    private String acquirerTimestamp;
    @Column(name="issuer_order_id")
    private String issuerOrderId;
    @Column(name="issuer_timestamp")
    private String issuerTimestamp;
    @Column(name="status")
    private String status;
    @Column(name="payment_request_id")
    private int paymentRequestId;

    public Transaction(){}

    public Transaction(double amount, String merchantOrderId, String merchantTimestamp, String acquirerOrderId, String acquirerTimestamp, String issuerOrderId, String issuerTimestamp, String status, int paymentRequestId) {
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
    public Transaction(int id, double amount, String merchantOrderId, String merchantTimestamp, String acquirerOrderId, String acquirerTimestamp, String issuerOrderId, String issuerTimestamp, String status, int paymentRequestId) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getMerchantTimestamp() {
        return merchantTimestamp;
    }

    public void setMerchantTimestamp(String merchantTimestamp) {
        this.merchantTimestamp = merchantTimestamp;
    }

    public String getAcquirerOrderId() {
        return acquirerOrderId;
    }

    public void setAcquirerOrderId(String acquirerOrderId) {
        this.acquirerOrderId = acquirerOrderId;
    }

    public String getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setAcquirerTimestamp(String acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public String getIssuerOrderId() {
        return issuerOrderId;
    }

    public void setIssuerOrderId(String issuerOrderId) {
        this.issuerOrderId = issuerOrderId;
    }

    public String getIssuerTimestamp() {
        return issuerTimestamp;
    }

    public void setIssuerTimestamp(String issuerTimestamp) {
        this.issuerTimestamp = issuerTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(int paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }
}
