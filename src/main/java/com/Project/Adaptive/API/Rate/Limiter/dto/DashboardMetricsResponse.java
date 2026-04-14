package com.Project.Adaptive.API.Rate.Limiter.dto;

public class DashboardMetricsResponse {
    private long totalRequests;
    private long totalAllowed;
    private long totalBlocked;
    private int currentAdaptiveLimit;

    public DashboardMetricsResponse(long totalRequests, long totalAllowed, long totalBlocked, int currentAdaptiveLimit) {
        this.totalRequests = totalRequests;
        this.totalAllowed = totalAllowed;
        this.totalBlocked = totalBlocked;
        this.currentAdaptiveLimit = currentAdaptiveLimit;
    }

    public long getTotalRequests() { return totalRequests; }
    public long getTotalAllowed() { return totalAllowed; }
    public long getTotalBlocked() { return totalBlocked; }
    public int getCurrentAdaptiveLimit() { return currentAdaptiveLimit; }
}
