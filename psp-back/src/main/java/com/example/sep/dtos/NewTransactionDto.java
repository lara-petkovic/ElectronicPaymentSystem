package com.example.sep.dtos;

public class NewTransactionDto {
    public String port;
    public Double amount;
    public Long merchantOrderId;
    public Long merchantTimestamp;
    public NewTransactionDto() {}

    public NewTransactionDto(String port, Double amount, Long merchantOrderId, Long merchantTimestamp) {
        this.port = port;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
    }
    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }
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
