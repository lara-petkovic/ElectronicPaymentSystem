package com.example.bank.config;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return super.hasError(response) && response.getStatusCode().value() != 403 && response.getStatusCode().value() != 404;
    }
}

