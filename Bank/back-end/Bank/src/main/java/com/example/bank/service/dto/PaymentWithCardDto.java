package com.example.bank.service.dto;

public class PaymentWithCardDto {
    public String Pan;
    public String HolderName;
    public String ExpirationDate;
    public String SecurityCode;
    public int PaymentRequestId;
    public PaymentWithCardDto(){}
    public PaymentWithCardDto(String pan, String holderName, String expirationDate, String securityCode, int paymentRequestId) {
        Pan = pan;
        HolderName = holderName;
        ExpirationDate = expirationDate;
        SecurityCode = securityCode;
        PaymentRequestId = paymentRequestId;
    }
}
