package com.Project.Adaptive.API.Rate.Limiter.Limiter;

public class TokenBucket {
    private long capacity;
    private double tokens;
    private double refillRate;
    private long lastRefillTime;

    public TokenBucket(long capacity,double refillRate){
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public boolean tryConsume(){
        long now = System.currentTimeMillis();
        long elapsedTime = now-lastRefillTime;
        double elapsedSec = elapsedTime/1000.0;

        tokens = Math.min(capacity,tokens+(elapsedSec*refillRate));
        lastRefillTime = now;

        if(tokens>=1){
            tokens --;
            return true;
        }
        return false;
    }

}
