package com.example.sep.dtos;

public class AuthMessage {
    private String message;
    public AuthMessage(){}

    public AuthMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
