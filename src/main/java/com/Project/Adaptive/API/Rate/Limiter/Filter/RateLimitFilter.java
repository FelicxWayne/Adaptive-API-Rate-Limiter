package com.Project.Adaptive.API.Rate.Limiter.Filter;


import com.Project.Adaptive.API.Rate.Limiter.Repository.ApiKeyRepository;
import com.Project.Adaptive.API.Rate.Limiter.Service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.Project.Adaptive.API.Rate.Limiter.Service.RateLimiterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class RateLimitFilter implements Filter {
    private final RateLimiterService rateLimiterService;

    public RateLimitFilter(RateLimiterService rateLimiterService){
        this.rateLimiterService = rateLimiterService;
    }

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException,ServletException{
        System.out.println("Filter triggered!");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String apiKey = httpRequest.getHeader("X-API-Key");
        String ip = httpRequest.getRemoteAddr();

        String clientId;

        if(apiKey != null && !apiKey.isBlank() && apiKeyService.isValidKey(apiKey)){
            clientId = apiKey;
        }
        else{
            clientId = ip;
        }

        boolean allowed = rateLimiterService.isAllowed(clientId);

        if(!allowed){
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too Many Requests");
            return;
        }
        chain.doFilter(request,response);
    }
}
