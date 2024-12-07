package com.example.sep.controllers;

import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.PaymentOption;
import com.example.sep.services.PaymentOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="api/paymentoption")
public class PaymentOptionController {
    @Autowired
    private PaymentOptionService paymentOptionService;
    @Autowired
    public PaymentOptionController(PaymentOptionService paymentOptionService) {
        this.paymentOptionService=paymentOptionService;
    }
    @PostMapping
    public ResponseEntity<PaymentOption> CreatePaymentOption(@RequestBody PaymentOptionDto newPaymentOption) throws Exception {
        PaymentOption paymentOption=paymentOptionService.create(newPaymentOption.name);
        return new ResponseEntity<>(paymentOption, HttpStatus.CREATED);
    }

    @DeleteMapping
    public void RemovePaymentOption (@RequestBody PaymentOptionDto paymentOption){
        paymentOptionService.remove(paymentOption.name);
    }
    @GetMapping
    public ResponseEntity<List<PaymentOptionDto>> getAll(){
        return new ResponseEntity<>(paymentOptionService.getAllOptions(), HttpStatus.ACCEPTED);
    }
}
