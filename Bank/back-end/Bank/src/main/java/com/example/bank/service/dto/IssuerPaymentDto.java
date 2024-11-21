package com.example.bank.service.dto;

public class IssuerPaymentDto {
    public String Pan;
    public String ExpirationDate;
    public String HolderName;
    public String SecurityCode;
    public String Acquirer;
    public double Amount;
    public String AcquirerAccountNumber;
    public IssuerPaymentDto(){}

    public IssuerPaymentDto(String pan, String expirationDate, String holderName, String securityCode, String acquirer, double amount, String acquirerAccountNumber) {
        Pan = pan;
        ExpirationDate = expirationDate;
        HolderName = holderName;
        SecurityCode = securityCode;
        Acquirer = acquirer;
        Amount = amount;
        AcquirerAccountNumber = acquirerAccountNumber;
    }

}
