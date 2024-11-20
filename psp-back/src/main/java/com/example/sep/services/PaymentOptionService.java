package com.example.sep.services;

import com.example.sep.models.PaymentOption;
import com.example.sep.repositories.PaymentOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentOptionService implements  IPaymentOptionService{
    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

    public PaymentOptionService(PaymentOptionRepository paymentOptionRepository){
        this.paymentOptionRepository=paymentOptionRepository;
    }
    @Override
    public List<PaymentOption> getAll() {
        return paymentOptionRepository.findAll();
    }
}
