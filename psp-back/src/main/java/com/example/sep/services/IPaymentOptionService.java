package com.example.sep.services;

import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.PaymentOption;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IPaymentOptionService {
     List<PaymentOption> getAll();
     PaymentOption getByOptionName(String option);
     PaymentOption create (String name);
     void remove (String name);
     List<PaymentOptionDto> getAllOptions();


}
