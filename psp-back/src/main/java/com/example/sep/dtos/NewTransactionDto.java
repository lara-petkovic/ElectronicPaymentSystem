package com.example.sep.dtos;

public class NewTransactionDto {
    public String merchantId;
    public String merchantPass;
    public Double amount;
    public Long merchantOrderId;
    public Long merchantTimestamp;
    public NewTransactionDto(){}

    public NewTransactionDto(String merchantId, String merchantPass, Double amount, Long merchantOrderId, Long merchantTimestamp) {
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(Long merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Long getMerchantTimestamp() {
        return merchantTimestamp;
    }

    public void setMerchantTimestamp(Long merchantTimestamp) {
        this.merchantTimestamp = merchantTimestamp;
    }
}
