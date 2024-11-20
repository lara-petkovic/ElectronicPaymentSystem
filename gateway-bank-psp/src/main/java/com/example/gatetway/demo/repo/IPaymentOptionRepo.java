package com.example.gatetway.demo.repo;

import com.example.gatetway.demo.model.PaymentOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentOptionRepo extends JpaRepository<PaymentOption, Long> {
    PaymentOption getPaymentOptionByOption(String option);
}
