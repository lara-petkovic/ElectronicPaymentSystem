package com.example.sep.dtos;

public class ClientAuthenticationDataDto {
    public String merchantId;
    public String merchantPass;
    public ClientAuthenticationDataDto(){}

    public ClientAuthenticationDataDto(String merchantId, String merchantPass) {
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
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
}
