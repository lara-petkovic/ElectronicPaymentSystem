package com.example.crypto.service;

import com.example.crypto.dto.NewTransactionDto;
import com.example.crypto.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {
     Transaction create(NewTransactionDto transaction);
     Transaction getById(Long id);

}
