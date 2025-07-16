package com.sepproject.paypalback.models;

import jakarta.persistence.*;

@Entity
@Table(name="paypal_merchant")
public class PaypalMerchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private String merchantId;
    @Column
    private String merchantPass;
    @Column
    private String paypalClientId;
    @Column
    private String paypalClientSecret; // Format: "clientId/////clientSecret"

    public PaypalMerchant() { }

    public PaypalMerchant(String merchantId, String merchantPass, String paypalClientId, String paypalClientSecret) {
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
        this.paypalClientId = paypalClientId;
        this.paypalClientSecret = paypalClientSecret;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPaypalClientId() { return paypalClientId; }

    public void setPaypalClientId(String paypalClientId) { this.paypalClientId = paypalClientId; }

    public String getPaypalClientSecret() {
        return paypalClientSecret;
    }

    public void setPaypalClientSecret(String paypalClientSecret) {
        this.paypalClientSecret = paypalClientSecret;
    }
//
//
//    public String getPaypalClientId() {
//        return paypalCredentials.split("/////")[0];
//    }
//
//    public String getPaypalClientSecret() {
//        return paypalCredentials.split("/////")[1];
//    }
}
