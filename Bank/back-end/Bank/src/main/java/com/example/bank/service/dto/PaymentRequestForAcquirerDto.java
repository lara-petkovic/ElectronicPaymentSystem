package com.example.bank.service.dto;

public class PaymentRequestForAcquirerDto {
    public String MerchantId;
    public String MerchantPassword;
    public Double Amount;
    public String MerchantOrderId;
    public String MerchantTimestamp;
    public String SuccessUrl;
    public String FailedUrl;
    public String ErrorUrl;
    public PaymentRequestForAcquirerDto(){}

    public PaymentRequestForAcquirerDto(String merchantId, String merchantPassword, Double amount, String merchantOrderId, String merchantTimestamp, String successUrl, String failedUrl, String errorUrl) {
        MerchantId = merchantId;
        MerchantPassword = merchantPassword;
        Amount = amount;
        MerchantOrderId = merchantOrderId;
        MerchantTimestamp = merchantTimestamp;
        SuccessUrl = successUrl;
        FailedUrl = failedUrl;
        ErrorUrl = errorUrl;
    }
}
