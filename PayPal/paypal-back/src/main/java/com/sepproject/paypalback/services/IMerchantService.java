package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface IMerchantService {
    Merchant getByMerchantIdAndMerchantPass(String merchantId, String merchantPass);
    Merchant getByMerchantId(String merchantId);
    Merchant create(Merchant merchant);
}
