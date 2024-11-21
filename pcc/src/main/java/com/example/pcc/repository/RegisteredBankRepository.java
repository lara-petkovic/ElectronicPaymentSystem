package com.example.pcc.repository;

import com.example.pcc.domain.model.RegisteredBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RegisteredBankRepository extends JpaRepository<RegisteredBank, String> {
    Optional<RegisteredBank> findById(String id);
}
