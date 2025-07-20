package com.sepproject.paypalback.services;

import com.sepproject.paypalback.models.Merchant;
import com.sepproject.paypalback.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService implements IMerchantService {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private EncryptionService encryptionService;

    @Override
    public Merchant getByMerchantIdAndMerchantPass(String merchantId, String merchantPass) {
        Merchant merchant = merchantRepository.getMerchantByMerchantIdAndMerchantPass(merchantId, merchantPass);

        if (merchant != null) {
            merchant.setPaypalClientId(encryptionService.decrypt(merchant.getPaypalClientId()));
        }
        return merchant;
    }

    @Override
    public Merchant getByMerchantId(String merchantId) {
        Merchant merchant = merchantRepository.getMerchantByMerchantId(merchantId);
        if (merchant != null) {
            merchant.setPaypalClientId(encryptionService.decrypt(merchant.getPaypalClientId()));
            merchant.setMerchantPass(encryptionService.decrypt(merchant.getMerchantPass()));
        }
        return merchantRepository.getMerchantByMerchantId(merchantId);
    }

    @Override
    public Merchant create(Merchant merchant) {
        if(merchantRepository.getMerchantByMerchantIdAndMerchantPass(merchant.getMerchantId(), merchant.getMerchantPass()) == null) {
            String encryptedPaypalClientId = encryptionService.encrypt(merchant.getPaypalClientId());
            String encryptedMerchantPass = encryptionService.encrypt(merchant.getMerchantPass());
            Merchant m = new Merchant(
                    merchant.getMerchantId(),
                    encryptedMerchantPass,
                    encryptedPaypalClientId);
            return  merchantRepository.save(m);
        }
        return null;
    }
}
