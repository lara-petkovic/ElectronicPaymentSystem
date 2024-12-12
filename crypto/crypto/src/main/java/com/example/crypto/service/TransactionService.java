package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import com.example.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CurrencyConversionService currencyConversionService;
    @Override
    public Transaction create(Transaction transaction) {
        double amount=currencyConversionService.convertEurToSepoliaEth(transaction.getAmount());
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }
}
