package com.example.sep.dtos;

public class LoginRequest {
    private String username;
    private String password;
    private String tfacode;

    public String getTfacode() {
        return tfacode;
    }

    public void setTfacode(String tfacode) {
        this.tfacode = tfacode;
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
