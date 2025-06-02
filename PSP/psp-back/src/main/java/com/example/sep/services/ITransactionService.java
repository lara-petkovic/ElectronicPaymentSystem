package com.example.sep.services;

import com.example.sep.models.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {
    Transaction SaveTransaction(Transaction transaction);
    Transaction GetTransactionByMerchantIdAndMerchantOrderId(String merchantId, Integer merchantOdrderId);
    Transaction GetTransactionByOrderId(Long orderId);
}
