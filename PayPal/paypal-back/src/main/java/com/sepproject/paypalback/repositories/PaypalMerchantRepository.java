package com.sepproject.paypalback.repositories;

import com.sepproject.paypalback.models.PaypalMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalMerchantRepository extends JpaRepository<PaypalMerchant, Long> {
    PaypalMerchant getPaypalMerchantByMerchantIdAndMerchantPass(String merchantId, String merchantPass);
    PaypalMerchant getPaypalMerchantByMerchantId(String merchantId);
}
