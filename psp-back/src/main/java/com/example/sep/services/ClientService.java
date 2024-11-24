package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import com.example.sep.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.security.SecureRandom;

@Service
public class ClientService implements IClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }
    @Override
    public ClientAuthenticationDataDto create(Client client, String address) {
        String merchantId=generateRandomString();
        client.setMerchantId(merchantId);
        client.setMerchantPass(generateRandomString());
        client.setPort(address);
        this.clientRepository.save(client);
        SendCredentials(client,address);
        return new ClientAuthenticationDataDto(client.getMerchantId(),client.getMerchantPass());
    }


    private void SendCredentials(Client client, String address){
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:5275/api/psp-subscription/credentials";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Location", address);
//
//        String body = "{ \"MerchantId\" : \"" + client.getMerchantId() + "\", \"MerchantPass\" : \"" + client.getMerchantPass() + "\" }";
//
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


        //SEND TO BANK
        RestTemplate restTemplateBank = new RestTemplate();
        String urlBank = "http://localhost:8087/api/accounts";

        HttpHeaders headersbank = new HttpHeaders();
        headersbank.setContentType(MediaType.APPLICATION_JSON);

        String bodybank = "{ \"MerchantId\" : \"" + client.getMerchantId() + "\", \"MerchantPassword\" : \"" + client.getMerchantPass() + "\", \"HolderName\" : \"" + "WS"+client.getMerchantId() +"\" }";

        HttpEntity<String> entityBank = new HttpEntity<>(bodybank, headersbank);

        // Send the POST request
        ResponseEntity<String> responseBank = restTemplateBank.exchange(urlBank, HttpMethod.POST, entityBank, String.class);

    }
    @Override
    public ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto) {
        Client client=clientRepository.getClientsByPort(newTransactionDto.port).getLast();
        if(client != null) {
            return new ClientSubscriptionDto(client.getSubscription(),client.getMerchantId(),newTransactionDto.getMerchantOrderId());
        }
        return null;
    }

    public Client getClientByPort(String port){
        return clientRepository.getClientsByPort(port).getLast();
    }
    @Override
    public Client getClientByMerchantId(String merchantId) {
        return clientRepository.getClientByMerchantId(merchantId);
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
