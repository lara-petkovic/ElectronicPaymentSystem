package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {
    public Transaction create(Transaction transaction);
}
