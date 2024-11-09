package com.example.sep.dtos;

public class NewClientDto {
    public String apiKey;

    public NewClientDto(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
