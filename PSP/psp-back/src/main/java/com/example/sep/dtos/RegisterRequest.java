package com.example.sep.dtos;

public class RegisterRequest {
    private String username;
    private String password;
    private String tfa;

    public String getTfa() {
        return tfa;
    }

    public void setTfa(String tfa) {
        this.tfa = tfa;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
