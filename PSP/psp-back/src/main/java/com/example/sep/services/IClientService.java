package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IClientService {
    ClientAuthenticationDataDto create(String subscription, String address, String walletAddress);
    ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto, String poirt);
    Client getClientByMerchantId(String merchantId);
    Client getClientByPort(String port);
    List<PaymentOptionDto> getOptionsForClient (String port);
    void RemoveOption(String port,String name);
    void RemoveClient(String port);
}
