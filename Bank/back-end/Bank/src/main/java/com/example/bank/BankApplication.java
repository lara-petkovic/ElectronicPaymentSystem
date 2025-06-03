package com.example.bank;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.bank.domain.model")
public class BankApplication {
    @Value("${MY_MASTER_PASSWORD}")
    private  String password;
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
//        String url = "https://pcc:8053/api/payments";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try{
//            String json = objectMapper.writeValueAsString(new PaymentRequestForIssuerDto("66666666666664", "ac.getCardHolderName()", "100", "pr.getId()", null));
//
//            HttpEntity<String> entity = new HttpEntity<>(json, headers);
//            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);
//            System.out.println(response.getStatusCode());
//        }
//        catch(Exception e){
//            System.out.println(e);
//        }
    }
    @PostConstruct
    public void init(){
        String trustStorePath = BankApplication.class.getClassLoader().getResource("keystoreBank.jks").getPath();
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }
}
