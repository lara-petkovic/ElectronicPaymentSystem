package com.example.pcc.service.dto;

public class PaymentRequestDto {
    public String Pan;
    public String ExpirationDate;
    public String HolderName;
    public String SecurityCode;
    public TransactionDto Transaction;
    public PaymentRequestDto(){}

    public PaymentRequestDto(String pan, String expirationDate, String holderName, String securityCode, TransactionDto transaction) {
        Pan = pan;
        ExpirationDate = expirationDate;
        HolderName = holderName;
        SecurityCode = securityCode;
        Transaction = transaction;
    }

}
