package com.example.crypto.service;

import com.example.crypto.model.Merchant;
import org.springframework.stereotype.Service;

@Service
public interface IMerchantService {
    Merchant getByMerchantId(String merchantId);
    Merchant create(Merchant merchant);
}
