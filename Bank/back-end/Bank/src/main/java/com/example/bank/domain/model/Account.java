package com.example.bank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "accounts")
public class Account {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "pan", nullable = false)
    private String pan;
    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;
    @Column(name = "balance", nullable = false)
    private Double balance;

    public Account(){}
    public Account(String id, String pan, String expirationDate, String cardHolderName, Double balance) {
        this.id = id;
        this.pan = pan;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
