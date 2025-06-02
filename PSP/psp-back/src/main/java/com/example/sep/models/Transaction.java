package com.example.sep.models;

import com.example.sep.dtos.NewTransactionDto;
import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId", unique = true, nullable = false)
    private Long transactionId;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "merchantId")
    private String merchantId;

    @Column(name = "orderId")
    private Long orderId;
    @Column(name = "timestamp")
    private String timestamp;

    public Transaction(NewTransactionDto newTransactionDto, String merchantId){
        this.amount=newTransactionDto.getAmount();
        this.orderId=newTransactionDto.getMerchantOrderId();
        this.merchantId= merchantId;
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



    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction(){}

}
