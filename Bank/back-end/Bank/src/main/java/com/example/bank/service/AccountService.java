package com.example.bank.service;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.service.dto.PaymentDto;
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
    public Account getMerchantAccount(PaymentRequest paymentRequest){
        Optional<Account> account = repo.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), paymentRequest.getMerchantPassword());
        return account.orElse(null);
    }
    public Account getIssuerAccount(PaymentDto payment){
        Optional<Account> account = repo.findByPanAndSecurityCodeAndCardHolderNameAndExpirationDate(
                payment.Pan,
                payment.SecurityCode,
                payment.HolderName,
                payment.ExpirationDate
        );
        return account.orElse(null);
    }
    public Account save(Account account){
        return repo.save(account);
    }

}
