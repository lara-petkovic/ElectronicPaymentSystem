package com.example.bank.service.dto;

public class MerchantRegistrationDto {
    public String HolderName;
    public String MerchantId;
    public String MerchantPassword;

    public MerchantRegistrationDto(String holderName, String merchantId, String merchantPassword) {
        HolderName = holderName;
        MerchantId = merchantId;
        MerchantPassword = merchantPassword;
    }
    public MerchantRegistrationDto(){}

}
