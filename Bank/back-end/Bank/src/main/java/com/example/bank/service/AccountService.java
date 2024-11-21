package com.example.bank.service;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.service.dto.IssuerPaymentDto;
import com.example.bank.service.dto.MerchantRegistrationDto;
import com.example.bank.service.dto.PaymentWithCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repo;
    public Boolean checkIfMerchantAccountExists(String merchantId, String merchantPassword){
        Optional<Account> account = repo.findByMerchantIdAndMerchantPassword(merchantId, merchantPassword);
        return account.isPresent();
    }
    public Boolean registerNewMerchant(MerchantRegistrationDto registrationDto){
        try {
            Account merchantAccount = new Account(
                    0,
                    null,
                    null,
                    true,
                    null,
                    registrationDto.MerchantPassword,
                    registrationDto.MerchantId,
                    null,
                    registrationDto.HolderName,
                    null,
                    0.0);
            Account savedAccount = repo.save(merchantAccount);
            return savedAccount.getId() > 0;
        }
        catch (Exception e){
            return false;
        }
    }
    public Account getMerchantAccount(PaymentRequest paymentRequest){
        Optional<Account> account = repo.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), paymentRequest.getMerchantPassword());
        return account.orElse(null);
    }
    public Account getIssuerAccount(PaymentWithCardDto payment){
        Optional<Account> account = repo.findByPanAndSecurityCodeAndCardHolderNameAndExpirationDate(
                payment.Pan,
                payment.SecurityCode,
                payment.HolderName,
                payment.ExpirationDate
        );
        return account.orElse(null);
    }

    public Account getIssuerAccount(IssuerPaymentDto payment){
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
