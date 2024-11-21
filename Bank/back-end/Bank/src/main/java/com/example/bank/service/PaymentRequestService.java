package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.PaymentRequestRepository;
import com.example.bank.service.dto.PaymentRequestForAcquirerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestService {
    @Autowired
    private PaymentRequestRepository repo;
    public PaymentRequest addPaymentRequest(PaymentRequestForAcquirerDto paymentRequestForAcquirerDto){
        PaymentRequest newPaymentRequest = new PaymentRequest(
                paymentRequestForAcquirerDto.MerchantId,
                paymentRequestForAcquirerDto.MerchantPassword,
                paymentRequestForAcquirerDto.Amount,
                paymentRequestForAcquirerDto.MerchantOrderId,
                paymentRequestForAcquirerDto.MerchantTimestamp,
                paymentRequestForAcquirerDto.SuccessUrl,
                paymentRequestForAcquirerDto.FailedUrl,
                paymentRequestForAcquirerDto.ErrorUrl
        );
        return repo.save(newPaymentRequest);
    }
    public PaymentRequest getPaymentRequest(int id){
        return repo.findById(id).orElse(null);
    }
}
