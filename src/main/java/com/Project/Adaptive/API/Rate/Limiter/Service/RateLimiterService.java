package com.Project.Adaptive.API.Rate.Limiter.Service;


import com.Project.Adaptive.API.Rate.Limiter.Limiter.TokenBucket;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final long capacity = 10;
    private final double refillRate = 5;

    public boolean isAllowed(String userId){
        TokenBucket bucket = buckets.computeIfAbsent(
                userId,id -> new TokenBucket(capacity,refillRate)
        );
        return bucket.tryConsume();
    }
}
