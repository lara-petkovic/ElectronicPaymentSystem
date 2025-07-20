package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.PaypalMerchant;
import com.sepproject.paypalback.repositories.PaypalMerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaypalMerchantService implements IPaypalMerchantService {
    @Autowired
    private PaypalMerchantRepository paypalMerchantRepository;
    @Autowired
    private EncryptionService encryptionService;

    @Override
    public PaypalMerchant getByPaypalMerchantIdAndMerchantPass(String paypalMerchantId, String paypalMerchantPass) {
        PaypalMerchant merchant = paypalMerchantRepository.getPaypalMerchantByMerchantIdAndMerchantPass(
                paypalMerchantId, paypalMerchantPass
        );

        if (merchant != null) {
            merchant.setPaypalClientId(encryptionService.decrypt(merchant.getPaypalClientId()));
            merchant.setPaypalClientSecret(encryptionService.decrypt(merchant.getPaypalClientSecret()));
        }
        return merchant;
    }


    @Override
    public PaypalMerchant getByPaypalMerchantId(String paypalMerchantId) {
        PaypalMerchant merchant = paypalMerchantRepository.getPaypalMerchantByMerchantId(paypalMerchantId);
        if (merchant != null) {
            merchant.setPaypalClientId(encryptionService.decrypt(merchant.getPaypalClientId()));
            merchant.setPaypalClientSecret(encryptionService.decrypt(merchant.getPaypalClientSecret()));
            merchant.setMerchantPass(encryptionService.decrypt(merchant.getMerchantPass()));
        }
        return merchant;
    }


    @Override
    public PaypalMerchant create(PaypalMerchant merchant) {
        if (paypalMerchantRepository.getPaypalMerchantByMerchantIdAndMerchantPass(
                merchant.getMerchantId(), merchant.getMerchantPass()) == null) {

            String encryptedClientId = encryptionService.encrypt(merchant.getPaypalClientId());
            String encryptedClientSecret = encryptionService.encrypt(merchant.getPaypalClientSecret());
            String encryptedMerchantPass = encryptionService.encrypt(merchant.getMerchantPass());

            PaypalMerchant m = new PaypalMerchant(
                    merchant.getMerchantId(),
                    encryptedMerchantPass,
                    encryptedClientId,
                    encryptedClientSecret
            );
            return paypalMerchantRepository.save(m);
        }
        return null;
    }
}
