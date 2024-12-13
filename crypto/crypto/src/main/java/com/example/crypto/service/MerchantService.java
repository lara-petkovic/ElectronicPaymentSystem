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
    public Merchant getByMerchantId(String merchantId) {
        return merchantRepository.getMerchantByMerchantId(merchantId);
    }

    @Override
    public Merchant create(Merchant merchant) {

        if(merchantRepository.getMerchantByMerchantId(merchant.getMerchantId())==null){
            Merchant m=new Merchant(merchant.getMerchantId(),merchant.getWalletAddress());
            return  merchantRepository.save(m);
        }
        return null;
    }


}
