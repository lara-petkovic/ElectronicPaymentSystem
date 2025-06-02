package com.sepproject.paypalback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String merchantId;
    private String paypalAccountId;
    private Double amount;
    private String status;
    private LocalDateTime timestamp;

    public Payment() {}

    public Payment(String orderId, String merchantId, String paypalAccountId, Double amount, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.paypalAccountId = paypalAccountId;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getPaypalAccountId() { return paypalAccountId; }

    public void setPaypalAccountId(String paypalAccountId) { this.paypalAccountId = paypalAccountId; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
