package com.sepproject.paypalback;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class PaypalBackApplication {
    @Value("${MY_MASTER_PASSWORD}")
    private  String password;

    public static void main(String[] args) {
        SpringApplication.run(PaypalBackApplication.class, args);
    }

    @PostConstruct
    public void init(){
        String trustStorePath = Objects.requireNonNull(PaypalBackApplication.class.getClassLoader().getResource("keystore.jks")).getPath();
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }
}