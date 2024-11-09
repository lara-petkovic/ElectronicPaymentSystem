package com.example.sep.models;

import com.example.sep.dtos.NewTransactionDto;
import jakarta.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", unique = true, nullable = false)
    private Long transactionId;

    @Column(name = "amount")
    private Double amount;
    @Column(name = "merchantId")
    private String merchantId;
    @Column(name = "merchantPass")
    private String merchantPass;
    @Column(name = "orderId")
    private Long orderId;
    @Column(name = "timestamp")
    private Long timestamp;

    public Transaction(NewTransactionDto newTransactionDto){
        this.amount=newTransactionDto.getAmount();
        this.orderId=newTransactionDto.getMerchantOrderId();
        this.merchantId= newTransactionDto.getMerchantId();
        this.merchantPass= newTransactionDto.getMerchantPass();
        this.timestamp=newTransactionDto.getMerchantTimestamp();

    }
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantPass() {
        return merchantPass;
    }

    public void setMerchantPass(String merchantPass) {
        this.merchantPass = merchantPass;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction(){}
    public Transaction(Long transactionId, Double amount, String merchantId, String merchantPass, Long orderId, Long timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
        this.orderId = orderId;
        this.timestamp = timestamp;
    }
}
