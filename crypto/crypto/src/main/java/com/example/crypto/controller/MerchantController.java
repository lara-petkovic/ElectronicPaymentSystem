package com.example.crypto.controller;

import com.example.crypto.model.Merchant;
import com.example.crypto.service.IMerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private IMerchantService merchantService;
    public MerchantController(IMerchantService merchantService){
        this.merchantService=merchantService;
    }

    @PostMapping
    public ResponseEntity<Merchant> register (@RequestBody Merchant merchant){
        Merchant m=merchantService.create(merchant);
        if(m!=null) {
            logger.info("New merchant registration request for wallet address: "+merchant.getWalletAddress());
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        }
        logger.warn("Failed merchant registration for wallet address: "+merchant.getWalletAddress());

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
