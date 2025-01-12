package com.example.gatetway.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.example.gatetway.demo") // Adjust package name as needed
@EnableDiscoveryClient
public class DemoApplication {

	public static void main(String[] args) {

		String trustStorePath = DemoApplication.class.getClassLoader().getResource("keystore_gw2.jks").getPath();
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", "milica");
		SpringApplication.run(DemoApplication.class, args);
	}

}
