package com.mu54omd.mini_ecommerce.backend_maven.dto;

public class LoginResponse {
    private String response;

    public LoginResponse() {}

    public LoginResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
