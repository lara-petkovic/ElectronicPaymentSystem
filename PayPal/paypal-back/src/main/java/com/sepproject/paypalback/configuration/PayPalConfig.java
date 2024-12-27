package com.sepproject.paypalback.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        Map<String, String> sdkConfig = new HashMap<>();

        sdkConfig.put("mode", "sandbox"); // If it ever comes to productions this field should be "live"

        String clientId = "AYCwb67GgPRjbQc5qptxXD5PpJWuOH9ttIIz04WQBuQtVT-109VoqwkShrlF4LvofO1mh162GL608TZd"; // larapetkovicnalog@gmail.com
        String clientSecret = "EBm0iUl-oZdSpx8lmTfeun-hNUBzUOToREDXYd6W6mvdbU0Kk9Kv0hBlltu8DSaNGzaDyCGWJUzVWFKx";
        OAuthTokenCredential oAuthTokenCredential = new OAuthTokenCredential(clientId, clientSecret, sdkConfig);
        APIContext apiContext = new APIContext(oAuthTokenCredential.getAccessToken());
        apiContext.setConfigurationMap(sdkConfig);

        return apiContext;
    }
}