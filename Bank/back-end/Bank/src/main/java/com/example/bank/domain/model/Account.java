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
    @Column(name = "number", nullable = false)
    private String number;
    @Column(name = "merchant_account", nullable = false)
    private Boolean merchantAccount;
    @Column(name = "bank_identifier_code", nullable = false)
    private String bankIdentifierCode;
    @Column(name = "merchant_password")
    private String merchantPassword;
    @Column(name = "merchant_id", unique = true)
    private String merchantId;
    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;
    @Column(name = "security_code", nullable = false)
    private String securityCode;
    @Column(name = "balance", nullable = false)
    private Double balance;

    public Account(){}

    public Account(String id, String pan, String number, Boolean merchantAccount, String bankIdentifierCode, String merchantPassword, String merchantId, String expirationDate, String cardHolderName, String securityCode, Double balance) {
        this.id = id;
        this.pan = pan;
        this.number = number;
        this.merchantAccount = merchantAccount;
        this.bankIdentifierCode = bankIdentifierCode;
        this.merchantPassword = merchantPassword;
        this.merchantId = merchantId;
        this.expirationDate = expirationDate;
        this.cardHolderName = cardHolderName;
        this.securityCode = securityCode;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean isMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(Boolean merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public String getBankIdentifierCode() {
        return bankIdentifierCode;
    }

    public void setBankIdentifierCode(String bankIdentifierCode) {
        this.bankIdentifierCode = bankIdentifierCode;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
