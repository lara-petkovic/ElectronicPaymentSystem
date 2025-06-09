package com.example.gatetway.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.example.gatetway.demo") // Adjust package name as needed
@EnableDiscoveryClient
public class DemoApplication {

	@Value("${MY_MASTER_PASSWORD}")
	private String password;
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@PostConstruct
	public void init(){
		String trustStorePath = DemoApplication.class.getClassLoader().getResource("gw2Keystore.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", password);
	}

}
