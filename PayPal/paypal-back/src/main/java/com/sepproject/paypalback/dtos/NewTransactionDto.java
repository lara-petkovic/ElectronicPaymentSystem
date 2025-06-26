package com.sepproject.paypalback.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTransactionDto {
    @JsonProperty("MerchantId")
    private String MerchantId;

    @JsonProperty("MerchantPassword")
    private String MerchantPassword;

    @JsonProperty("Amount")
    private double Amount;

    @JsonProperty("MerchantTimestamp")
    private String MerchantTimestamp;

    @JsonProperty("SuccessUrl")
    private String SuccessUrl;

    @JsonProperty("ErrorUrl")
    private String ErrorUrl;

    @JsonProperty("FailedUrl")
    private String FailedUrl;

    @JsonProperty("MerchantOrderId")
    private String MerchantOrderId;

    public NewTransactionDto() {}

    public NewTransactionDto(String merchantId, String merchantPassword, double amount, String merchantTimestamp, String successUrl, String errorUrl, String failedUrl, String merchantOrderId) {
        MerchantId = merchantId;
        MerchantPassword = merchantPassword;
        Amount = amount;
        MerchantTimestamp = merchantTimestamp;
        SuccessUrl = successUrl;
        ErrorUrl = errorUrl;
        FailedUrl = failedUrl;
        MerchantOrderId = merchantOrderId;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantPassword() {
        return MerchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        MerchantPassword = merchantPassword;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getMerchantTimestamp() {
        return MerchantTimestamp;
    }

    public void setMerchantTimestamp(String merchantTimestamp) {
        MerchantTimestamp = merchantTimestamp;
    }

    public String getSuccessUrl() {
        return SuccessUrl;
    }

    public void setSuccessUrl(String successUrl) {
        SuccessUrl = successUrl;
    }

    public String getErrorUrl() {
        return ErrorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        ErrorUrl = errorUrl;
    }

    public String getFailedUrl() {
        return FailedUrl;
    }

    public void setFailedUrl(String failedUrl) {
        FailedUrl = failedUrl;
    }

    public String getMerchantOrderId() {
        return MerchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        MerchantOrderId = merchantOrderId;
    }
}
