package com.example.bank.service.dto;

import java.time.LocalDate;

public class CardDetailsDto {
    public String Pan;
    public String HolderName;
    public String ExpirationDate;
    public String SecurityCode;
    public String PaymentRequestId;
    public CardDetailsDto(){}
    public CardDetailsDto(String pan, String holderName, String expirationDate, String securityCode, String paymentRequestId) {
        Pan = pan;
        HolderName = holderName;
        ExpirationDate = expirationDate;
        SecurityCode = securityCode;
        PaymentRequestId = paymentRequestId;
    }
    public boolean isValidExpirationDate() {
        if (!ExpirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            return false;
        }
        String[] parts = ExpirationDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear() % 100;
        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false;
        }
        return true;
    }
}
