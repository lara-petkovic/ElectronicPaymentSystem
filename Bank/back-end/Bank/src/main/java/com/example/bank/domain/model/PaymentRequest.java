package com.example.bank.domain.model;

import jakarta.persistence.*;

@Entity
public class PaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "merchant_id", nullable = false)
    private String merchantId;
    @Column(name = "merchant_password", nullable = false)
    private String merchantPassword;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "merchant_order_id", nullable = false)
    private String merchantOrderId;
    @Column(name = "merchant_timestamp", nullable = false)
    private String merchantTimestamp;
    @Column(name = "success_url", nullable = false)
    private String successUrl;
    @Column(name = "failed_url", nullable = false)
    private String failedUrl;
    @Column(name = "error_url", nullable = false)
    private String errorUrl;

    public PaymentRequest(){}
    public PaymentRequest(String merchantId, String merchantPassword, Double amount, String merchantOrderId, String merchantTimestamp, String successUrl, String failedUrl, String errorUrl) {
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
        this.errorUrl = errorUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }
}
