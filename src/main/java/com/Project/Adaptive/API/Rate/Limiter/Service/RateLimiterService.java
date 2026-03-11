package com.Project.Adaptive.API.Rate.Limiter.Service;


import com.Project.Adaptive.API.Rate.Limiter.Limiter.TokenBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

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

    public boolean isAllowed(String userId){
        TokenBucket bucket = buckets.computeIfAbsent(
                userId,id -> new TokenBucket(id,capacity,refillRate,windowDuration,cleanWindowsRequired,recoveryFactor,floorPercent)
        );
        return bucket.tryConsume();
    }
}
