package com.example.sep.dtos;

public class NewTransactionDto {
    public Double amount;
    public Long merchantOrderId;
    public String merchantTimestamp;
    public NewTransactionDto() {}

    public NewTransactionDto(Double amount, Long merchantOrderId, String merchantTimestamp) {
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
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

    public String getMerchantTimestamp() {
        return merchantTimestamp;
    }

    public void setMerchantTimestamp(String merchantTimestamp) {
        this.merchantTimestamp = merchantTimestamp;
    }
}
