package com.example.crypto.dto;

public class NewTransactionDto {
    public String MerchantId;
    public String MerchantPassword;
    public Double Amount;
    public Long MerchantOrderId;
    public String MerchantTimestamp;
    public String SuccessUrl;
    public String FailedUrl;
    public String ErrorUrl;

    public NewTransactionDto(){};
}
