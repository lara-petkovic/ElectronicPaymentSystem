package com.example.sep.services;

import com.example.sep.EncryptionUtil;
import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.dtos.PaymentOptionDto;
import com.example.sep.models.Client;
import com.example.sep.models.PaymentOption;
import com.example.sep.repositories.ClientRepository;
import com.example.sep.repositories.PaymentOptionRepository;
import com.example.sep.repositories.TransactionRepository;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
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
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private EncryptionUtil encryptionUtil;
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    public ClientService(ClientRepository clientRepository, PaymentOptionRepository paymentOptionRepository, EncryptionUtil encryptionUtil){
        this.clientRepository=clientRepository;
        this.paymentOptionRepository=paymentOptionRepository;
        this.encryptionUtil=encryptionUtil;

    }
    @Override
   // @Transactional
    public ClientAuthenticationDataDto create(String subscription, String address, String walletAddress, String paypalClientId) throws Exception {

        Client client = new Client();
        String merchantId = generateRandomString();
        client.setMerchantId(merchantId);
        String merchantPassword = generateRandomString();
        client.setMerchantPass(encryptionUtil.encrypt(merchantPassword));
        client.setPort(address);

        boolean containsBank = false;
        boolean containsCrypto = false;
        boolean containsPayPal = false;

        String[] options = subscription.split(",");

        for (String s : options) {

            if(s.equals("Card") || s.equals("QR")) {
                containsBank = true;
            }
            if(s.equals("Crypto")){
                containsCrypto = true;
            }
            if(s.equals("PayPal")) {
                containsPayPal = true;
            }

            PaymentOption option = paymentOptionRepository.getPaymentOptionByOption(s);

            if (option != null) {
                client.addPaymentOption(option);
            }
        }

        logger.info("New client on port {} with options {}", address, Arrays.toString(options));
        this.clientRepository.save(client);
        if(containsBank)
            SendCredentialsToBank(client.getMerchantId(), merchantPassword);
        if(containsCrypto)
            SendWalletCredentials(client.getMerchantId(), merchantPassword, walletAddress);
        if(containsPayPal) {
            SendPaypalCredentials(client.getMerchantId(), merchantPassword, paypalClientId);
        }
        return new ClientAuthenticationDataDto(client.getMerchantId(),merchantPassword);
    }

    private void SendWalletCredentials(String merchantId, String merchantPass, String walletAddress) {
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                            try {
                                sslContextSpec
                                        .sslContext(
                                                SslContextBuilder.forClient()
                                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                        .build()
                                        );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {

            String bodybank = "{ \"merchantId\" : \"" + merchantId + "\", \"merchantPass\" : \"" + merchantPass + "\", \"walletAddress\" : \"" + walletAddress +"\" }";

            String response = webClient.post()
                    .uri("https://localhost:8087/api/merchant")
                    .header("Content-Type", "application/json")
                    .bodyValue(bodybank)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.out.println("Parsing error: " + e.getMessage());
        }
    }

    private void SendCredentialsToBank(String merchantId, String merchantPass){
        String urlBank = "https://localhost:8087/api/accounts";
        String bodybank = "{ \"MerchantId\" : \"" + merchantId + "\", \"MerchantPassword\" : \"" + merchantPass + "\", \"HolderName\" : \"" + "WS"+ merchantId +"\" }";
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                            try {
                                sslContextSpec
                                        .sslContext(
                                                SslContextBuilder.forClient()
                                                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                        .build()
                                        );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {
            String response = webClient.post()
                    .uri(urlBank)
                    .header("Content-Type", "application/json")
                    .bodyValue(bodybank)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.out.println("Parsing error: " + e.getMessage());
        }
    }

    private void SendPaypalCredentials(String merchantId, String merchantPass, String paypalClientId) {
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec
                                .sslContext(
                                        SslContextBuilder.forClient()
                                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                                .build()
                                );
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });

        WebClient webClient = WebClient.builder()
                .baseUrl("https://localhost:8087") //.baseUrl("https://localhost:8087/api/notification")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        try {
            String body = "{ \"merchantId\" : \"" + merchantId +
                    "\", \"merchantPass\" : \"" + merchantPass +
                    "\", \"paypalClientId\" : \"" + paypalClientId + "\" }";

            System.out.println("LARA: " + body);

            String response = webClient.post()
                    .uri("/api/notification")
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("PayPal registration response: " + response);

        } catch (Exception e) {
            System.out.println("PayPal registration error: " + e.getMessage());
        }
    }

    @Override
    public ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto, String port) {
        Client client=getClientByPort(port);
        if (client!=null) {
            Set<PaymentOption> options = client.getPaymentOptions();
            String optionsString = options.stream()
                    .map(PaymentOption::toString)
                    .collect(Collectors.joining(","));
            return new ClientSubscriptionDto(optionsString, client.getMerchantId(), newTransactionDto.getMerchantOrderId());
        }
        logger.warn("Client not found, port: "+port);
        return null;
    }

    public Client getClientByPort(String port) {
        List<Client> clients =clientRepository.getClientsByPort(port);
        if(!clients.isEmpty())
            return clients.getLast();
        return null;
    }

    @Override
    public List<PaymentOptionDto> getOptionsForClient(String port) {
        Client client=clientRepository.getFirstByPort(port);
        List<PaymentOptionDto> paymentOptionDtos = new ArrayList<>();
        for(PaymentOption po:client.getPaymentOptions()){
            PaymentOptionDto paymentOptionDto=new PaymentOptionDto();
            paymentOptionDto.setName(po.getOption());
            paymentOptionDtos.add(paymentOptionDto);
        }
        return paymentOptionDtos;
    }

    @Override
    public void RemoveOption(String port, String name) {
        Client client = clientRepository.getFirstByPort(port);
        PaymentOption paymentOption=paymentOptionRepository.getPaymentOptionByOption(name);
        if(paymentOption != null)
            client.removePaymentOption(paymentOption);
        clientRepository.save(client);
    }

    @Override
    public void RemoveClient(String port) {
        Client c = getClientByPort(port);
        transactionRepository.deleteAll(transactionRepository.getTransactionsByMerchantId(c.getMerchantId()));
        clientRepository.delete(c);
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
