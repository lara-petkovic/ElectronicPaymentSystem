package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.PaypalMerchant;
import org.springframework.stereotype.Service;

@Service
public interface IPaypalMerchantService {
    PaypalMerchant getByPaypalMerchantIdAndMerchantPass(String paypalMerchantId, String paypalMerchantPass);
    PaypalMerchant getByPaypalMerchantId(String paypalMerchantId);
    PaypalMerchant create(PaypalMerchant merchant);
}
