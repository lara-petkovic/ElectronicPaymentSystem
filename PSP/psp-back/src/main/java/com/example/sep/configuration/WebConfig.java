package com.example.sep.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:8086", "https://localhost:8087", "https://localhost:4201")  // ili "*" za bilo koji izvor
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // dozvoljene HTTP metode
                .allowedHeaders("*")  // dozvoljeni zaglavlja
                .allowCredentials(true);  // dozvoljava slanje kolačića
    }
}
