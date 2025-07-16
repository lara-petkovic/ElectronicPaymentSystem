package com.sepproject.paypalback.services;

import com.sepproject.paypalback.dtos.NewTransactionDto;
import com.sepproject.paypalback.models.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {
     Transaction create(NewTransactionDto transaction);
     Transaction getById(Long id);
}
