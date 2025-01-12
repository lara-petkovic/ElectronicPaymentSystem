package com.example.sep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SepApplication {

	public static void main(String[] args) {
		String trustStorePath = SepApplication.class.getClassLoader().getResource("keystore.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", "milica");
		SpringApplication.run(SepApplication.class, args);
	}

}
