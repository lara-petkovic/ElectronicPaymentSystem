package com.example.bank.service;

import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.service.dto.CardDetailsDto;
import com.example.bank.service.dto.MerchantRegistrationDto;
import com.example.bank.service.dto.PaymentRequestForIssuerDto;
import com.example.bank.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public Boolean checkIfMerchantAccountExists(String merchantId, String rawPassword) {
        Optional<Account> accountOpt = repo.findByMerchantId(merchantId);
        if (accountOpt.isEmpty()) return false;

        return passwordEncoder.matches(rawPassword, accountOpt.get().getMerchantPassword());
    }

    public Boolean registerNewMerchant(MerchantRegistrationDto dto) {
        try {
            String hashedPassword = passwordEncoder.encode(dto.MerchantPassword);
            Account merchantAccount = new Account(
                    0,
                    null,
                    null,
                    true,
                    null,
                    hashedPassword,
                    dto.MerchantId,
                    null,
                    dto.HolderName,
                    null,
                    0.0
            );
            return repo.save(merchantAccount).getId() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public Account getMerchantAccount(PaymentRequest request) {
        Optional<Account> account = repo.findByMerchantIdAndMerchantPassword(
                request.getMerchantId(),
                request.getMerchantPassword()
        );
        return account.orElse(null);
    }
    public Account getIssuerAccount(CardDetailsDto dto) {
        return findByDecryptedCardInfo(dto.Pan, dto.SecurityCode, dto.HolderName, dto.ExpirationDate);
    }

    public Account getIssuerAccount(PaymentRequestForIssuerDto dto) {
        return findByDecryptedCardInfo(dto.Pan, dto.SecurityCode, dto.HolderName, dto.ExpirationDate);
    }

    private Account findByDecryptedCardInfo(String pan, String securityCode, String holder, String exp) {
        for (Account acc : repo.findAll()) {
            try {
                if (CryptoUtil.decrypt(acc.getPan()).equals(pan) &&
                        CryptoUtil.decrypt(acc.getSecurityCode()).equals(securityCode) &&
                        acc.getCardHolderName().equals(holder) &&
                        CryptoUtil.decrypt(acc.getExpirationDate()).equals(exp)) {
                    return acc;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
    public Account save(Account account) {
        if (account.getPan() != null) account.setPan(CryptoUtil.encrypt(account.getPan()));
        if (account.getSecurityCode() != null) account.setSecurityCode(CryptoUtil.encrypt(account.getSecurityCode()));
        if (account.getExpirationDate() != null) account.setExpirationDate(CryptoUtil.encrypt(account.getExpirationDate()));
        return repo.save(account);
    }

}
