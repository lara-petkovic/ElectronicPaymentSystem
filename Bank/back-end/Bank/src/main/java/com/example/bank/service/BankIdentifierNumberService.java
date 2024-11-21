package com.example.bank.service;

import com.example.bank.domain.model.BankIdentifierNumber;
import com.example.bank.repositories.BankIdentifierNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankIdentifierNumberService {
    @Autowired
    private BankIdentifierNumberRepository repo;
    public String getId(){
        Optional<BankIdentifierNumber> bankNumber = repo.getFirst();
        return bankNumber.map(BankIdentifierNumber::getId).orElse(null);
    }
}
