package com.Project.Adaptive.API.Rate.Limiter.Controller;

import com.Project.Adaptive.API.Rate.Limiter.Service.ApiKeyService;
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
    public Map<String, String> generateKey(@RequestParam String clientName) {
        String apiKey = apiKeyService.generateKey(clientName);
        Map<String, String> response = new HashMap<>();
        response.put("clientName", clientName);
        response.put("apiKey", apiKey);
        return response;
    }

    @PostMapping("/revoke-key")
    public Map<String,String> revokeKey(@RequestParam String apiKey){
        apiKeyService.revokeKey(apiKey);
        Map<String, String> response = new HashMap<>();
        response.put("status", "revoked");
        response.put("apiKey", apiKey);
        return response;
    }
}
