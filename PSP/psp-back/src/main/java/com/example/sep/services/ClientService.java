package com.example.sep.services;

import com.example.sep.controllers.TransactionResponseController;
import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.Client;
import com.example.sep.models.PaymentOption;
import com.example.sep.repositories.ClientRepository;
import com.example.sep.repositories.PaymentOptionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PaymentOptionRepository paymentOptionRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository clientRepository, PaymentOptionRepository paymentOptionRepository){
        this.clientRepository=clientRepository;
        this.paymentOptionRepository=paymentOptionRepository;
    }
    @Override
    @Transactional
    public ClientAuthenticationDataDto create(String subscription, String address, String walletAddress) {

        Client client = new Client();

        String merchantId = generateRandomString();
        client.setMerchantId(merchantId);
        client.setMerchantPass(generateRandomString());
        client.setPort(address);
        client = clientRepository.save(client);



        Boolean containsBank=false;
        Boolean containsCrypto=false;

        String[] options = subscription.split(",");

        for (String s : options) {

            if(s.equals("Card") || s.equals("QR")) {
                containsBank=true;
            }
            if(s.equals("Crypto")){
                containsCrypto=true;
            }

            PaymentOption option = paymentOptionRepository.getPaymentOptionByOption(s);

            if (option != null) {
                client.addPaymentOption(option);
            }
        }

        logger.info("New client on port "+address+" with options "+ Arrays.toString(options));
        this.clientRepository.save(client);
        if(containsBank)
            SendCredentialsToBank(client);
        if(containsCrypto)
            SendWalletCredentials(client, walletAddress);
        return new ClientAuthenticationDataDto(client.getMerchantId(),client.getMerchantPass());

    }

    private void SendWalletCredentials(Client client, String walletAddress){
        RestTemplate restTemplateBank = new RestTemplate();
        String urlBank = "http://localhost:8087/api/merchant";


        HttpHeaders headersbank = new HttpHeaders();
        headersbank.setContentType(MediaType.APPLICATION_JSON);


        String bodybank = "{ \"merchantId\" : \"" + client.getMerchantId() + "\", \"merchantPass\" : \"" + client.getMerchantPass() + "\", \"walletAddress\" : \"" + walletAddress +"\" }";

        HttpEntity<String> entity= new HttpEntity<>(bodybank, headersbank);

        // Send the POST request
        ResponseEntity<String> responseBank = restTemplateBank.exchange(urlBank, HttpMethod.POST, entity, String.class);
    }

    private void SendCredentialsToBank(Client client){

        RestTemplate restTemplateBank = new RestTemplate();
        String urlBank = "http://localhost:8087/api/accounts";

        HttpHeaders headersbank = new HttpHeaders();
        headersbank.setContentType(MediaType.APPLICATION_JSON);

        String bodybank = "{ \"MerchantId\" : \"" + client.getMerchantId() + "\", \"MerchantPassword\" : \"" + client.getMerchantPass() + "\", \"HolderName\" : \"" + "WS"+client.getMerchantId() +"\" }";

        HttpEntity<String> entityBank = new HttpEntity<>(bodybank, headersbank);

        ResponseEntity<String> responseBank = restTemplateBank.exchange(urlBank, HttpMethod.POST, entityBank, String.class);

    }
    @Override
    public ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto, String port) {
        List<Client> clients=clientRepository.getClientsByPort(port);
        if (!clients.isEmpty()) {
            Client client = clients.get(clients.size() - 1);
            Set<PaymentOption> options = client.getPaymentOptions();
            String optionsString = options.stream()
                    .map(PaymentOption::toString)
                    .collect(Collectors.joining(","));
            return new ClientSubscriptionDto(optionsString, client.getMerchantId(), newTransactionDto.getMerchantOrderId());
        }
        return null;
    }

    public Client getClientByPort(String port){
        return clientRepository.getClientsByPort(port).get(clientRepository.getClientsByPort(port).size() - 1);
    }

    @Override
    public List<PaymentOptionDto> getOptionsForClient(String port) {
        Client client=clientRepository.getFirstByPort(port);
        List<PaymentOptionDto> paymentOptionDtos=new ArrayList<PaymentOptionDto>();
        for(PaymentOption po:client.getPaymentOptions()){
            PaymentOptionDto paymentOptionDto=new PaymentOptionDto();
            paymentOptionDto.setName(po.getOption());
            paymentOptionDtos.add(paymentOptionDto);
        }
        return paymentOptionDtos;
    }

    @Override
    public void RemoveOption(String port, String name) {
        Client client=clientRepository.getFirstByPort(port);
        PaymentOption paymentOption=paymentOptionRepository.getPaymentOptionByOption(name);
        if(paymentOption!=null)
            client.removePaymentOption(paymentOption);
        clientRepository.save(client);
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
