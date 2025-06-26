package com.sepproject.paypalback.mappers;

import com.sepproject.paypalback.dtos.NewTransactionDto;
import com.sepproject.paypalback.dtos.PaymentRequestDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestMapper {
    public PaymentRequestDto mapToPaypalRequest(NewTransactionDto gatewayRequest) {
        PaymentRequestDto paypalRequest = new PaymentRequestDto();

        paypalRequest.setOrderId(gatewayRequest.getMerchantOrderId());
        paypalRequest.setMerchantId(gatewayRequest.getMerchantId());
        paypalRequest.setAmount(gatewayRequest.getAmount());

        return paypalRequest;
    }
}
