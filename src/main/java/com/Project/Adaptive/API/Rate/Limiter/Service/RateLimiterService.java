package com.Project.Adaptive.API.Rate.Limiter.Service;


import com.Project.Adaptive.API.Rate.Limiter.Limiter.TokenBucket;
import com.Project.Adaptive.API.Rate.Limiter.Model.RatelimitStats;
import com.Project.Adaptive.API.Rate.Limiter.Repository.RateLimiterRepository;
import com.Project.Adaptive.API.Rate.Limiter.dto.DashboardMetricsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RateLimiterService {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);
    @Autowired
    private RateLimiterRepository repository;

    @Autowired
    private TokenBucket tokenBucket;


    @Value("${rate.limiter.capacity}")
    private long capacity;

    @Value("${rate.limiter.refill-rate}")
    private double refillRate;


    public boolean isAllowed(String clientId){
        if(!repository.exists(clientId)){
            log.info("New Client | clientID : {}",clientId);
            repository.saveTokens(clientId,capacity);
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

    public RatelimitStats getStats(String clientId){
        double storedTokens = repository.getTokens(clientId);
        double refillRate = repository.getRefillRate(clientId);
        double baseRefillRate = repository.getBaseRefillRate(clientId);
        long capacity = repository.getCapacity(clientId);
        long lastRefillTime = repository.getLastRefillTime(clientId);
        long windowStartTime = repository.getWindowStartTime(clientId);
        long totalRequest = repository.getTotalRequest(clientId);
        long rejectedRequest = repository.getRejectedRequest(clientId);
        int cleanWindowCount = repository.getCleanWindowCount(clientId);

        long now  = System.currentTimeMillis();
        long elapsed = now-lastRefillTime;
        double elapsedSec = elapsed/1000.0;
        double liveTokens = Math.min(capacity,storedTokens+(refillRate*elapsedSec));

        return new RatelimitStats(
                clientId,
                liveTokens,
                refillRate,
                baseRefillRate,
                cleanWindowCount,
                windowStartTime,
                lastRefillTime,
                totalRequest,
                rejectedRequest
        );
    }

    public DashboardMetricsResponse getGlobalMetrics() {
        long globalTotal = repository.getGlobalTotal();
        long globalRejected = repository.getGlobalRejected();

        long globalAllowed = globalTotal - globalRejected;

        int avgLimitPerMin = (int)(refillRate * 60);

        return new DashboardMetricsResponse(globalTotal, globalAllowed, globalRejected, avgLimitPerMin);
    }
}
