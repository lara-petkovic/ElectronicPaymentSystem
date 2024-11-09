package com.example.sep.models;

import jakarta.persistence.*;

@Entity
public class Client {
    @Column(name="merchantId")
    private String merchantId;
    @Column(name="merchantPass")
    private String merchantPass;
    @Column(name="activeCard")
    private Boolean activeCard;
    @Column(name="activeQR")
    private Boolean activeQR;
    @Column(name="activePayPal")
    private Boolean activePayPal;
    @Column(name="activeBitcoin")
    private Boolean activeBitcoin;
    @Column(name="successUrl")
    private String successUrl;
    @Column(name="failedUrl")
    private String failedUrl;
    @Column(name="errorUrl")
    private String errorUrl;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

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

    public Boolean getActiveCard() {
        return activeCard;
    }

    public void setActiveCard(Boolean activeCard) {
        this.activeCard = activeCard;
    }

    public Boolean getActiveQR() {
        return activeQR;
    }

    public void setActiveQR(Boolean activeQR) {
        this.activeQR = activeQR;
    }

    public Boolean getActivePayPal() {
        return activePayPal;
    }

    public void setActivePayPal(Boolean activePayPal) {
        this.activePayPal = activePayPal;
    }

    public Boolean getActiveBitcoin() {
        return activeBitcoin;
    }

    public void setActiveBitcoin(Boolean activeBitcoin) {
        this.activeBitcoin = activeBitcoin;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
