package com.Project.Adaptive.API.Rate.Limiter.Limiter;

public class AdaptiveMetrics {
    private long totalRequests;
    private long rejectedRequests;
    private long windowStartTime;
    private double baseRefillRate;
    private int cleanWindowCount;

    private static final long windowDuration = 60000;
    private static final int cleanWindowsRequired = 5;
    private static final double floorPercent = 0.10;
    private static final double recoveryFactor = 1.1;

    public AdaptiveMetrics(double baseRefillRate){
        this.baseRefillRate = baseRefillRate;
        this.totalRequests = 0;
        this.rejectedRequests = 0;
        this.cleanWindowCount =0;
        this.windowStartTime = System.currentTimeMillis();
    }

    public void recordRequest(boolean allowed){
        totalRequests++;
        if(!allowed){
            rejectedRequests++;
        }
    }

    public boolean isWindowExpired(){
        long elapsed = System.currentTimeMillis()-windowStartTime;
        return elapsed >= windowDuration;
    }

    public double evaluate(double currentRefillRate){
        double floor = floorPercent*baseRefillRate;

        if(totalRequests == 0){
            resetWindow();
            return currentRefillRate;
        }

        double rejectRatio = (double) rejectedRequests/totalRequests;

        if(rejectRatio > 0.0){
            cleanWindowCount = 0;
            resetWindow();
            return Math.max(floor,currentRefillRate*(1-rejectRatio));
        }

        cleanWindowCount++;
        resetWindow();

        if(cleanWindowCount >= cleanWindowsRequired){
            return Math.min(baseRefillRate,currentRefillRate*recoveryFactor);
        }
        return currentRefillRate;
    }
    private void resetWindow(){
        totalRequests = 0;
        rejectedRequests = 0;
        windowStartTime = System.currentTimeMillis();
    }
}
