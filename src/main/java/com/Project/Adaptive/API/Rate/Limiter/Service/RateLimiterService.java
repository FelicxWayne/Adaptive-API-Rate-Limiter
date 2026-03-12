package com.Project.Adaptive.API.Rate.Limiter.Service;


import com.Project.Adaptive.API.Rate.Limiter.Limiter.TokenBucket;
import com.Project.Adaptive.API.Rate.Limiter.Repository.RateLimiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    @Autowired
    private RateLimiterRepository repository;

    @Autowired
    private TokenBucket tokenBucket;

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    @Value("${rate.limiter.capacity}")
    private long capacity;

    @Value("${rate.limiter.refill-rate}")
    private double refillRate;

    @Value("${rate.limiter.window-duration}")
    private long windowDuration;

    @Value("${rate.limiter.clean-windows-required}")
    private int cleanWindowsRequired;

    @Value("${rate.limiter.recovery-factor}")
    private double recoveryFactor;

    @Value("${rate.limiter.floor-percent}")
    private double floorPercent;

    public boolean isAllowed(String clientId){
        if(!repository.exists(clientId)){
            repository.saveTokens(clientId,capacity);
            repository.saveRefillRate(clientId,refillRate);
            repository.saveBaseRefillRate(clientId,refillRate);
            repository.saveTotalRequest(clientId,0);
            repository.saveRejectedRequest(clientId,0);
            repository.saveCleanWindowCount(clientId,0);
            repository.saveLastRefillTime(clientId,System.currentTimeMillis());
            repository.saveWindowStartTime(clientId,System.currentTimeMillis());
        }
        return tokenBucket.tryConsume(clientId);
    }
}
