package com.example.bank.service.dto;

import com.example.bank.domain.model.Transaction;

public class PaymentRequestForIssuerDto {
    public String Pan;
    public String ExpirationDate;
    public String HolderName;
    public String SecurityCode;
    public Transaction Transaction;
    public PaymentRequestForIssuerDto(){}

    public PaymentRequestForIssuerDto(String pan, String expirationDate, String holderName, String securityCode, Transaction transaction) {
        Pan = pan;
        ExpirationDate = expirationDate;
        HolderName = holderName;
        SecurityCode = securityCode;
        Transaction = transaction;
    }

}
