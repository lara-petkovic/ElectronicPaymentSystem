package com.sepproject.paypalback.repositories;

import com.sepproject.paypalback.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction getTransactionById(Long id);
}
