package com.example.sep.services;

import com.example.sep.models.Transaction;
import com.example.sep.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public Transaction SaveTransaction(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }
}
