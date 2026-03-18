package com.Project.Adaptive.API.Rate.Limiter.Service;

import com.Project.Adaptive.API.Rate.Limiter.Repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public String generateKey(String clientName){
        String apiKey = "rl-" + UUID.randomUUID().toString();
        apiKeyRepository.saveApiKey(apiKey,clientName);
        return apiKey;
    }

    public boolean isValidKey(String apiKey){
        return apiKeyRepository.isValidKey(apiKey);
    }

    public String getClientName(String apiKey){
        return apiKeyRepository.getClientName(apiKey);
    }

    public void revokeKey(String apiKey){
        apiKeyRepository.deleteApiKey(apiKey);
    }
}
