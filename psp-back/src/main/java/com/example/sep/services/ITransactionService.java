package com.example.sep.services;

import com.example.sep.models.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {
    public Transaction SaveTransaction(Transaction transaction);
}
