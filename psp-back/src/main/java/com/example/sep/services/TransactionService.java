package com.example.sep.services;

import com.example.sep.models.Transaction;
import com.example.sep.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }
    @Override
    public Transaction SaveTransaction(Transaction transaction) {
        Transaction t= this.transactionRepository.save(transaction);
        return t;
    }

    @Override
    public Transaction GetTransactionByMerchantIdAndMerchantOrderId(String merchantId, Integer merchantOdrderId) {
        return this.transactionRepository.getTransactionByMerchantIdAndOrderId(merchantId, Long.valueOf(merchantOdrderId));
    }
}
