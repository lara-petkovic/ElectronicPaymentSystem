package com.example.crypto;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoApplication {

	@Value("${MY_MASTER_PASSWORD}")
	private String password;
	public static void main(String[] args) {
		SpringApplication.run(CryptoApplication.class, args);
	}

	@PostConstruct
	public void init(){
		String trustStorePath = CryptoApplication.class.getClassLoader().getResource("keystore_crypto.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", password);
	}
}
