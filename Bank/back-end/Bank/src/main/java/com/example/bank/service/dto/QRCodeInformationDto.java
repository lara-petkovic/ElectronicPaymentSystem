package com.example.bank.service.dto;

public class QRCodeInformationDto {
    public String K;
    public String V;
    public String C;
    public String R;
    public String N;
    public String I;
    public String P;
    public String SF;
    public String S;
    public String RO;
    public QRCodeInformationDto(
            String acquirerAccount,
            String acquirerName,
            double amount,
            String paymentRequestId
    ){
        K = "PR";
        V = "01";
        C = "1";
        R = acquirerAccount;
        N = acquirerName;
        I = ("RSD"+amount).replace('.',',');
        P = "KORISNIK";
        SF = "189";
        S = "ONLINE KUPOVINA";
        RO="00"+paymentRequestId;
    }
}
