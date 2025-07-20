package com.sepproject.paypalback.models;

import jakarta.persistence.*;

@Entity
@Table(name="merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(length = 512)
    private String merchantId;
    @Column(length = 512)
    private String merchantPass;
    @Column(length = 512)
    private String paypalClientId;

    public Merchant() { }

    public Merchant(String merchantId, String merchantPass, String paypalClientId) {
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
        this.paypalClientId = paypalClientId;
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
}
