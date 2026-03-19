package com.Project.Adaptive.API.Rate.Limiter.dto;

public class ApiKeyResponse {
    private String clientId;
    private String apiKey;
    private String message;

    public ApiKeyResponse(String clientId,String apiKey,String message){
        this.clientId = clientId;
        this.apiKey = apiKey;
        this.message = message;
    }
}
