package com.example.sep.models;

import jakarta.persistence.*;

//@Entity
public class Transaction {

   // @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "transactionId", unique = true, nullable = false)
    private Long transactionId;

   // @Column(name = "value")
    private Double value;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
