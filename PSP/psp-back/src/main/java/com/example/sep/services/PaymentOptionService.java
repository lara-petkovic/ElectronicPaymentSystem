package com.example.sep.services;

import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.PaymentOption;
import com.example.sep.repositories.PaymentOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public PaymentOption getByOptionName(String option) {
        return paymentOptionRepository.getPaymentOptionByOption(option);
    }

    @Override
    public PaymentOption create(String name) {
        PaymentOption paymentOption=new PaymentOption();
        paymentOption.setOption(name);
        return paymentOptionRepository.save(paymentOption);
    }

    @Override
    public void remove(String name) {
        if(paymentOptionRepository.findAll().size()>=2) {
            PaymentOption paymentOption = paymentOptionRepository.getPaymentOptionByOption(name);
            paymentOptionRepository.delete(paymentOption);
        }
    }

    @Override
    public List<PaymentOptionDto> getAllOptions() {
        List<PaymentOption> options=paymentOptionRepository.findAll();
        List<PaymentOptionDto> paymentOptionCreationDtos=new ArrayList<PaymentOptionDto>();
        for(PaymentOption po:options){
            PaymentOptionDto paymentOptionCreationDto=new PaymentOptionDto();
            paymentOptionCreationDto.setName(po.getOption());
            paymentOptionCreationDtos.add(paymentOptionCreationDto);
        }
        return paymentOptionCreationDtos;
    }

}
