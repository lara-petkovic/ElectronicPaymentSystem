package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
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
        client.setSuccessUrl("http://localhost:4201/success");
        client.setFailedUrl("http://localhost:4201/fail");
        client.setErrorUrl("http://localhost:4201/error");
        client.setMerchantId(client.getMerchantId()+generateRandomString());
        client.setMerchantPass(generateRandomString());
        this.clientRepository.save(client);
        return new ClientAuthenticationDataDto(client.getMerchantId(),client.getMerchantPass());
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
