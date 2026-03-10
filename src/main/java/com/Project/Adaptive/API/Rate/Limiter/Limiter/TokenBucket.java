package com.Project.Adaptive.API.Rate.Limiter.Limiter;

public class TokenBucket {
    private String userId;
    private long capacity;
    private double tokens;
    private double refillRate;
    private long lastRefillTime;
    private AdaptiveMetrics adaptiveMetrics;

    public TokenBucket(String userId,long capacity,double refillRate){
        this.userId = userId;
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
        this.adaptiveMetrics = new AdaptiveMetrics(refillRate);
    }

    public synchronized boolean tryConsume(){

        if(adaptiveMetrics.isWindowExpired()){
            refillRate = adaptiveMetrics.evaluate(refillRate);
        }

        long now = System.currentTimeMillis();
        long elapsedTime = now-lastRefillTime;
        double elapsedSec = elapsedTime/1000.0;

        tokens = Math.min(capacity,tokens+(elapsedSec*refillRate));
        lastRefillTime = now;

        if(tokens>=1){
            tokens --;
            adaptiveMetrics.recordRequest(true);
            return true;
        }
        adaptiveMetrics.recordRequest(false);
        return false;
    }

}
