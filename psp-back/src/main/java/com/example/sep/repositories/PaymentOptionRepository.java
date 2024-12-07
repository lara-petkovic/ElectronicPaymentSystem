package com.example.sep.repositories;

import com.example.sep.models.PaymentOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOptionRepository extends JpaRepository<PaymentOption,Long> {
    List<PaymentOption> findAll();
    PaymentOption getPaymentOptionByOption (String option);
}
