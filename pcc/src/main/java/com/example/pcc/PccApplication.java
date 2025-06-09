package com.example.pcc;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PccApplication {

    @Value("${MY_MASTER_PASSWORD}")
    private  String password;
    public static void main(String[] args) {
        SpringApplication.run(PccApplication.class, args);
    }
    @PostConstruct
    public void init(){
        String trustStorePath = PccApplication.class.getClassLoader().getResource("pccKeystore.jks").getPath();
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }
}
