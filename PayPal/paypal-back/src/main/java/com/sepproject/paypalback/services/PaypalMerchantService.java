package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.PaypalMerchant;
import com.sepproject.paypalback.repositories.PaypalMerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaypalMerchantService implements IPaypalMerchantService {
    @Autowired
    private PaypalMerchantRepository paypalMerchantRepository;

    @Override
    public PaypalMerchant getByPaypalMerchantIdAndMerchantPass(String paypalMerchantId, String paypalMerchantPass) {
        return paypalMerchantRepository.getPaypalMerchantByMerchantIdAndMerchantPass(paypalMerchantId, paypalMerchantPass);
    }

    @Override
    public PaypalMerchant getByPaypalMerchantId(String paypalMerchantId) {
        return paypalMerchantRepository.getPaypalMerchantByMerchantId(paypalMerchantId);
    }

    @Override
    public PaypalMerchant create(PaypalMerchant merchant) {

        if(paypalMerchantRepository.getPaypalMerchantByMerchantIdAndMerchantPass(merchant.getMerchantId(), merchant.getMerchantPass()) == null) {
            PaypalMerchant m = new PaypalMerchant(merchant.getMerchantId(),merchant.getMerchantPass(), merchant.getPaypalClientId(), merchant.getPaypalClientSecret());
            return paypalMerchantRepository.save(m);
        }
        return null;
    }
}
