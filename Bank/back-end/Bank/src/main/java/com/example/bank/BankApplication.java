package com.example.bank;

import com.example.bank.domain.model.Account;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = "com.example.bank.domain.model")
public class BankApplication {
    @Autowired
    private PasswordEncoder passwordEncoder;
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
        String trustStorePath = BankApplication.class.getClassLoader().getResource("bankKeystore.jks").getPath();
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);

        Account ac1 = new Account(-1, "6666666666666664", "'1234567890'", true, "'BIC001'", null, null, "11/26", "Dusan Sudjic", "456", 10000.0);
        Account ac2 = new Account(-2, "6666678901234567", "2345678901", false, "BIC002", null, null, "11/26", "Dusan Sudjic", "456", 1500.0);
        Account ac3 = new Account(-3, "6666789012345678", "3456789012", false, "BIC003", null, null, "10/24", "Alice Brown Acquirer", "789", 1200.0);
        ac1.encrypt(passwordEncoder);
        ac2.encrypt(passwordEncoder);
        ac3.encrypt(passwordEncoder);
        System.out.println(ac1);
        System.out.println(ac2);
        System.out.println(ac3);
    }
}
