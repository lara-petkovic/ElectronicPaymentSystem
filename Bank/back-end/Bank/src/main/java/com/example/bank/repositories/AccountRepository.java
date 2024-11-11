package com.example.bank.repositories;

import com.example.bank.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByMerchantIdAndMerchantPassword(String merchantId, String merchantPassword);
}
