package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import org.springframework.stereotype.Service;

@Service
public interface IClientService {
    ClientAuthenticationDataDto create(Client client, String address);
    ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto);
    Client getClientByMerchantId(String merchantId);
}
