package com.example.bank.service.dto;

import com.example.bank.domain.model.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class TransactionDto {
    public int Id;
    public double Amount;
    public String MerchantOrderId;
    public String MerchantTimestamp;
    public String AcquirerOrderId;
    public String AcquirerTimestamp;
    public String IssuerOrderId;
    public String IssuerTimestamp;
    public String Status;
    public String PaymentRequestId;
    public TransactionDto(){}

    public TransactionDto(int id, double amount, String merchantOrderId, String merchantTimestamp, String acquirerOrderId, String acquirerTimestamp, String issuerOrderId, String issuerTimestamp, String status, String paymentRequestId) {
        Id = id;
        Amount = amount;
        MerchantOrderId = merchantOrderId;
        MerchantTimestamp = merchantTimestamp;
        AcquirerOrderId = acquirerOrderId;
        AcquirerTimestamp = acquirerTimestamp;
        IssuerOrderId = issuerOrderId;
        IssuerTimestamp = issuerTimestamp;
        Status = status;
        PaymentRequestId = paymentRequestId;
    }
    public TransactionDto(Transaction transaction){
        Id = transaction.getId();
        Amount = transaction.getAmount();
        MerchantOrderId = transaction.getMerchantOrderId();
        MerchantTimestamp = transaction.getMerchantTimestamp();
        AcquirerOrderId = transaction.getAcquirerOrderId();
        AcquirerTimestamp = transaction.getAcquirerTimestamp();
        IssuerOrderId = transaction.getIssuerOrderId();
        IssuerTimestamp = transaction.getIssuerTimestamp();
        Status = transaction.getStatus();
        PaymentRequestId = transaction.getPaymentRequestId();
    }
    public Transaction convertToTransaction(){
        return new Transaction(
                Id,
                Amount,
                MerchantOrderId,
                MerchantTimestamp,
                AcquirerOrderId,
                AcquirerTimestamp,
                IssuerOrderId,
                IssuerTimestamp,
                Status,
                PaymentRequestId
        );
    }
}
