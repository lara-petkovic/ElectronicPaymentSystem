package com.example.crypto.service;

import com.example.crypto.model.Merchant;
import com.example.crypto.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService implements IMerchantService{
    @Autowired
    private MerchantRepository merchantRepository;
    @Override
    public Merchant getByMerchantIdAndMerchantPass(String merchantId, String merchantPass) {
        return merchantRepository.getMerchantByMerchantIdAndMerchantPass(merchantId, merchantPass);
    }

    @Override
    public Merchant getByMerchantId(String merchantId) {
        return merchantRepository.getMerchantByMerchantId(merchantId);
    }

    @Override
    public Merchant create(Merchant merchant) {

        if(merchantRepository.getMerchantByMerchantIdAndMerchantPass(merchant.getMerchantId(), merchant.getMerchantPass())==null){
            Merchant m=new Merchant(merchant.getMerchantId(),merchant.getWalletAddress(),merchant.getMerchantPass());
            return  merchantRepository.save(m);
        }
        return null;
    }


}
