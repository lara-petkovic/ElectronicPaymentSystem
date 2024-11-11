package com.example.bank.repositories;

import com.example.bank.domain.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, String> {
}
