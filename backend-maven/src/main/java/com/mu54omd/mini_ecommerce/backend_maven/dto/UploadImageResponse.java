package com.mu54omd.mini_ecommerce.backend_maven.dto;

public class UploadImageResponse {
    private String response;

    public UploadImageResponse() {}

    public UploadImageResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
