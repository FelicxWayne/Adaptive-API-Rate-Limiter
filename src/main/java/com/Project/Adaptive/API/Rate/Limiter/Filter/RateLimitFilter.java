package com.Project.Adaptive.API.Rate.Limiter.Filter;


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

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException,ServletException{

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String apiKey = httpRequest.getHeader("X-API-Key");
        if(apiKey == null || apiKey.isBlank()){
            apiKey = "unknown";
        }
        String ip = httpRequest.getRemoteAddr();

        String userId = apiKey+":"+ip;

        boolean allowed = rateLimiterService.isAllowed(userId);

        if(!allowed){
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too Many Requests");
            return;
        }
        chain.doFilter(request,response);
    }
}
