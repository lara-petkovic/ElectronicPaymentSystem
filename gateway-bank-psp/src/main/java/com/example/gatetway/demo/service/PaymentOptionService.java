package com.example.gatetway.demo.service;

import com.example.gatetway.demo.model.PaymentOption;
import com.example.gatetway.demo.repo.IPaymentOptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentOptionService {
    @Autowired
    IPaymentOptionRepo paymentOptionRepo;

    public PaymentOptionService(IPaymentOptionRepo _paymentOptionRepo){
        this.paymentOptionRepo=_paymentOptionRepo;
    }

    public PaymentOption getAddressByOption(String option){
        PaymentOption paymentOption=paymentOptionRepo.getPaymentOptionByOption(option);
        return  paymentOption;
    }
}
