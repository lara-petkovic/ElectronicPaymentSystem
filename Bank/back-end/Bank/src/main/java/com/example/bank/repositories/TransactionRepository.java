package com.example.bank.repositories;

import com.example.bank.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<List<Transaction>> findAllByAcquirerOrderId(String acquirerOrderId);
    Optional<List<Transaction>> findAllByMerchantOrderId(String merchantOrderId);
    Optional<List<Transaction>> findAllByIssuerOrderId(String issuerOrderId);
    Optional<List<Transaction>> findAllByPaymentRequestId(int paymentRequestId);

}
