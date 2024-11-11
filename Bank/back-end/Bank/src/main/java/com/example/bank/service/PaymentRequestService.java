package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.PaymentRequestRepository;
import com.example.bank.service.dto.PaymentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestService {
    @Autowired
    private PaymentRequestRepository repo;
    public PaymentRequest addPaymentRequest(PaymentRequestDto paymentRequestDto){
        PaymentRequest newPaymentRequest = new PaymentRequest(
                paymentRequestDto.MerchantId,
                paymentRequestDto.MerchantPassword,
                paymentRequestDto.Amount,
                paymentRequestDto.MerchantOrderId,
                paymentRequestDto.MerchantTimestamp,
                paymentRequestDto.SuccessUrl,
                paymentRequestDto.FailedUrl,
                paymentRequestDto.ErrorUrl
        );
        return repo.save(newPaymentRequest);
    }
}
