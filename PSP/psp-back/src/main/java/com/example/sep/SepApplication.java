package com.example.sep;

import jakarta.annotation.PostConstruct;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SepApplication {

	@Value("${MY_MASTER_PASSWORD}")
	private  String password;

	public static void main(String[] args)  {

		SpringApplication.run(SepApplication.class, args);

	}

	@PostConstruct
	public void init(){
		String trustStorePath = SepApplication.class.getClassLoader().getResource("keystore.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", password);
	}

}
