package com.example.sep.dtos;

import com.example.sep.models.Transaction;

public class TransactionToAcquirerDto {
    public String merchantId;
    public String merchantPass;
    public Double amount;
    public Long merchantOrderId;

    @Override
    public String toString() {
        return "TransactionToAcquirerDto{" +
                "merchantId='" + merchantId + '\'' +
                ", merchantPass='" + merchantPass + '\'' +
                ", amount=" + amount +
                ", merchantOrderId=" + merchantOrderId +
                ", merchantTimestamp=" + merchantTimestamp +
                ", paymentOption='" + paymentOption + '\'' +
                ", successUrl='" + successUrl + '\'' +
                ", failedUrl='" + failedUrl + '\'' +
                ", errorUrl='" + errorUrl + '\'' +
                '}';
    }

    public Long merchantTimestamp;
    public String paymentOption;
    public String successUrl;
    public String failedUrl;
    public String errorUrl;
    public TransactionToAcquirerDto(){}

    public TransactionToAcquirerDto(String merchantId, String merchantPass, Double amount, Long merchantOrderId, Long merchantTimestamp, String paymentOption, String successUrl, String failedUrl, String errorUrl) {
        this.merchantId = merchantId;
        this.merchantPass = merchantPass;
        this.amount = amount;
        this.merchantOrderId = merchantOrderId;
        this.merchantTimestamp = merchantTimestamp;
        this.paymentOption = paymentOption;
        this.successUrl = successUrl;
        this.failedUrl = failedUrl;
        this.errorUrl = errorUrl;
    }

    public TransactionToAcquirerDto(Transaction transaction, String paymentOption) {
        this.paymentOption=paymentOption;
        this.merchantId=transaction.getMerchantId();
        this.merchantPass=transaction.getMerchantPass();
        this.merchantTimestamp=transaction.getTimestamp();
        this.amount=transaction.getAmount();
        this.merchantOrderId=transaction.getOrderId();
        this.successUrl="http://localhost:4201/success";
        this.failedUrl="http://localhost:4201/fail";
        this.errorUrl="http://localhost:4201/error";



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

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
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
}
