package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import com.example.sep.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ClientService implements IClientService{
    @Autowired
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }
    @Override
    public ClientAuthenticationDataDto create(Client client) {
        String merchantId=generateRandomString();
        ///KAD SE BUDE VRACALO ONDA client.getMerchantId+ovaj izgenerisan iovde vec mora da mu salje
        client.setMerchantId(merchantId);
        client.setMerchantPass(generateRandomString());
        this.clientRepository.save(client);
        return new ClientAuthenticationDataDto(client.getMerchantId(),client.getMerchantPass());
    }

    @Override
    public ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto) {
        Client client=clientRepository.getClientByMerchantId(newTransactionDto.merchantId);
        if(client!=null && client.getMerchantPass().equals(newTransactionDto.getMerchantPass())){
            return new ClientSubscriptionDto(client.getSubscription(),client.getMerchantId(),newTransactionDto.getMerchantOrderId());
        }
        return null;
    }


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
