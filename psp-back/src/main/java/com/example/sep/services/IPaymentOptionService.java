package com.example.sep.services;

import com.example.sep.models.PaymentOption;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IPaymentOptionService {
    public List<PaymentOption> getAll();
}
