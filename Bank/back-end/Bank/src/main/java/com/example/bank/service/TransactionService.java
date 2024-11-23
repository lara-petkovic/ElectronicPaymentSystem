package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.domain.model.Transaction;
import com.example.bank.repositories.TransactionRepository;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repo;
    public Transaction getTransactionByAcquirerOrderId(String acquirerOrderId){
        return repo.findAllByAcquirerOrderId(acquirerOrderId)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(list.size() - 1))
                .orElse(null);
    }
    public Transaction getTransactionByMerchantOrderId(String merchantOrderId){
        return repo.findAllByMerchantOrderId(merchantOrderId)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(list.size() - 1))
                .orElse(null);
    }
    public Transaction getTransactionByPaymentRequestId(int paymentRequestId){
        return repo.findAllByPaymentRequestId(paymentRequestId)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(list.size() - 1))
                .orElse(null);
    }
    public Transaction getTransactionByIssuerOrderId(String issuerOrderId){
        return repo.findAllByIssuerOrderId(issuerOrderId)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(list.size() - 1))
                .orElse(null);
    }
    public Transaction addTransaction_Acquirer(PaymentRequest paymentRequest){
        Transaction transaction = new Transaction(
                paymentRequest.getAmount(),
                paymentRequest.getMerchantOrderId(),
                paymentRequest.getMerchantTimestamp(),
                generateRandomOrderId(),
                Instant.now().toString(),
                null,
                null,
                "CREATED",
                paymentRequest.getId()
        );
        return repo.save(transaction);
    }
    public Transaction addTransaction_Issuer(PaymentRequestForIssuerDto paymentRequest){
        Transaction transaction = new Transaction(
                paymentRequest.Transaction.getAmount(),
                paymentRequest.Transaction.getMerchantOrderId(),
                paymentRequest.Transaction.getMerchantTimestamp(),
                paymentRequest.Transaction.getAcquirerOrderId(),
                paymentRequest.Transaction.getAcquirerTimestamp(),
                generateRandomOrderId(),
                Instant.now().toString(),
                "ISSUER_PAID",
                paymentRequest.Transaction.getPaymentRequestId()
        );
        return repo.save(transaction);
    }
    public Transaction addFailedTransaction_Issuer(PaymentRequestForIssuerDto paymentRequest){
        Transaction transaction = new Transaction(
                paymentRequest.Transaction.getAmount(),
                paymentRequest.Transaction.getMerchantOrderId(),
                paymentRequest.Transaction.getMerchantTimestamp(),
                paymentRequest.Transaction.getAcquirerOrderId(),
                paymentRequest.Transaction.getAcquirerTimestamp(),
                generateRandomOrderId(),
                Instant.now().toString(),
                "FAILED",
                paymentRequest.Transaction.getPaymentRequestId()
        );
        return repo.save(transaction);
    }
    public Transaction successTransaction(Transaction transaction){
        Transaction existingTransaction = getTransactionByAcquirerOrderId(transaction.getAcquirerOrderId());
        existingTransaction.setStatus("SUCCESS");
        existingTransaction.setIssuerOrderId(transaction.getIssuerOrderId());
        existingTransaction.setIssuerTimestamp(transaction.getIssuerTimestamp());
        return repo.save(existingTransaction);
    }
    public Transaction failTransaction(Transaction transaction){
        Transaction existingTransaction = getTransactionByAcquirerOrderId(transaction.getAcquirerOrderId());
        existingTransaction.setStatus("FAILED");
        existingTransaction.setIssuerOrderId(transaction.getIssuerOrderId());
        existingTransaction.setIssuerTimestamp(transaction.getIssuerTimestamp());
        return repo.save(existingTransaction);
    }
    public Transaction errorTransaction(Transaction transaction){
        Transaction existingTransaction = getTransactionByAcquirerOrderId(transaction.getAcquirerOrderId());
        existingTransaction.setStatus("ERROR");
        return repo.save(existingTransaction);
    }
    private String generateRandomOrderId() {
        int length = 16;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
