package com.example.bank.service;

import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.repositories.PaymentRequestRepository;
import com.example.bank.service.dto.PaymentRequestForAcquirerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Random;

@Service
public class PaymentRequestService {
    @Autowired
    private PaymentRequestRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public PaymentRequest addPaymentRequest(PaymentRequestForAcquirerDto paymentRequestForAcquirerDto){
        PaymentRequest newPaymentRequest = new PaymentRequest(
                paymentRequestForAcquirerDto.MerchantId,
                passwordEncoder.encode(paymentRequestForAcquirerDto.MerchantPassword),
                paymentRequestForAcquirerDto.Amount,
                paymentRequestForAcquirerDto.MerchantOrderId,
                paymentRequestForAcquirerDto.MerchantTimestamp,
                paymentRequestForAcquirerDto.SuccessUrl,
                paymentRequestForAcquirerDto.FailedUrl,
                paymentRequestForAcquirerDto.ErrorUrl
        );
        newPaymentRequest.setId(generateRandomId());
        return repo.save(newPaymentRequest);
    }
    public PaymentRequest getPaymentRequest(String id){
        for(PaymentRequest pr: repo.findAll()){
            if(pr.getId().equals(id)){
                return pr;
            }
        }
        return null;
    }
    private String generateRandomId() {
        Random random = new Random();

        StringBuilder osnovniBroj = new StringBuilder();
        for (int i = 0; i < 19; i++) {
            osnovniBroj.append(random.nextInt(10));
        }
        String brojBezKontrole = osnovniBroj.toString();
        BigInteger broj = new BigInteger(brojBezKontrole);
        int ostatak = broj.mod(BigInteger.valueOf(97)).intValue();
        int kontrolniBroj = (ostatak == 0) ? 0 : 97 - ostatak;

        return brojBezKontrole + String.format("%02d", kontrolniBroj);
    }
}
