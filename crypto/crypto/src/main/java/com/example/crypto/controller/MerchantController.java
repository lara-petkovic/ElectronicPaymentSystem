package com.example.crypto.controller;

import com.example.crypto.model.Merchant;
import com.example.crypto.service.IMerchantService;
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

    @Autowired
    private IMerchantService merchantService;
    public MerchantController(IMerchantService merchantService){
        this.merchantService=merchantService;
    }

    @PostMapping
    public ResponseEntity<Merchant> register (@RequestBody Merchant merchant){
        Merchant m=merchantService.create(merchant);
        if(m!=null)
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
