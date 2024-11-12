package com.example.bank.service.dto;

public class PaymentDto {
    public String Pan;
    public String HolderName;
    public String ExpirationDate;
    public String SecurityCode;
    public int PaymentRequestId;
    public PaymentDto(){}
    public PaymentDto(String pan, String holderName, String expirationDate, String securityCode, int paymentRequestId) {
        Pan = pan;
        HolderName = holderName;
        ExpirationDate = expirationDate;
        SecurityCode = securityCode;
        PaymentRequestId = paymentRequestId;
    }
}
