package com.example.bank.repositories;

import com.example.bank.domain.model.BankIdentifierNumber;
import com.example.bank.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface BankIdentifierNumberRepository extends JpaRepository<BankIdentifierNumber, String> {
    @Query(value = "SELECT * FROM bank_identifier_number LIMIT 1", nativeQuery = true)
    Optional<BankIdentifierNumber> getFirst();
}
