package com.example.pcc.service;

import com.example.pcc.domain.model.RegisteredBank;
import com.example.pcc.repository.RegisteredBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisteredBankService {
    @Autowired
    private RegisteredBankRepository repo;
    public RegisteredBank getByPan(String pan){
        return repo.findById(pan.substring(0,4)).orElse(null);
    }
}
