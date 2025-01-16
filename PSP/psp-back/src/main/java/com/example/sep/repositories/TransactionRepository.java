package com.example.sep.repositories;

import com.example.sep.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction getTransactionByMerchantIdAndOrderId (String merchantId, Long orderId);
    Transaction getTransactionByOrderId (Long orderId);

    List<Transaction> getTransactionsByMerchantId(String merchantId);
}
