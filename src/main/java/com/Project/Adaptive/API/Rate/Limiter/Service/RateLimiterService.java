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
    private  double refillRate;

    public boolean isAllowed(String userId){
        TokenBucket bucket = buckets.computeIfAbsent(
                userId,id -> new TokenBucket(id,capacity,refillRate)
        );
        return bucket.tryConsume();
    }
}
