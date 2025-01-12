package com.example.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoApplication {

	public static void main(String[] args) {
		String trustStorePath = CryptoApplication.class.getClassLoader().getResource("keystore_crypto.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", "milica");
		SpringApplication.run(CryptoApplication.class, args);
	}

}
