package com.example.crypto.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CollectionId;

@Entity
@Table(name="merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column
    private String merchantId;
    @Column
    private String merchantPass;

    @Column
    private String walletAddress;

    public Merchant(){}

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

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getMerchantPass() {
        return merchantPass;
    }

    public void setMerchantPass(String merchantPass) {
        this.merchantPass = merchantPass;
    }

    public Merchant(String merchantId, String walletAddress, String merchantPass) {
        this.merchantId = merchantId;
        this.walletAddress = walletAddress;
        this.merchantPass=merchantPass;
    }
}
