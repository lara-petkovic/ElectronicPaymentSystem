package com.example.bank.service;

import com.example.bank.domain.model.Account;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.service.dto.PaymentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repo;
    public Boolean checkIfMerchantAccountExists(PaymentRequestDto paymentRequest){
        Optional<Account> account = repo.findByMerchantIdAndMerchantPassword(paymentRequest.MerchantId, paymentRequest.MerchantPassword);
        return account.isPresent();
    }
}
