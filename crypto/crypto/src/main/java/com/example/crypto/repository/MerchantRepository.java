package com.example.crypto.repository;

import com.example.crypto.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant,Long> {
    Merchant getMerchantByMerchantIdAndMerchantPass(String merchantId, String merchantPass);
    Merchant getMerchantByMerchantId(String merchantId);
}
