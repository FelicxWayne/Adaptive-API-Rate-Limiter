package com.Project.Adaptive.API.Rate.Limiter.Controller;

import com.Project.Adaptive.API.Rate.Limiter.Service.ApiKeyService;
import com.Project.Adaptive.API.Rate.Limiter.dto.ApiKeyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping("/generate-key")
    public ApiKeyResponse generateKey(@RequestParam String clientName) {
        String apiKey = apiKeyService.generateKey(clientName);
        return new ApiKeyResponse(clientName,apiKey,"API Key generated successfully");
    }

    @PostMapping("/revoke-key")
    public ApiKeyResponse revokeKey(@RequestParam String apiKey){
        apiKeyService.revokeKey(apiKey);
        return new ApiKeyResponse(null,apiKey,"API Key revoked successfully");
    }
}
