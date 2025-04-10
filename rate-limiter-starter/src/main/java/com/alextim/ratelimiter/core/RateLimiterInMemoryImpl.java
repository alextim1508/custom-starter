package com.alextim.ratelimiter.core;

import com.alextim.ratelimiter.config.RateLimiterProperties;
import com.alextim.ratelimiter.exceptions.RateLimitExceededException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateLimiterInMemoryImpl implements RateLimiter {

    private final int attemptsLimit;
    private final int attemptExpirationTimeSeconds;

    private final Map<String, Long> countByAddress = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public RateLimiterInMemoryImpl(RateLimiterProperties rateLimiterProperties) {
        attemptsLimit = rateLimiterProperties.attemptsLimit();
        attemptExpirationTimeSeconds = rateLimiterProperties.attemptExpirationSeconds();
    }

    @Override
    public void increment(String remoteAddress) throws RateLimitExceededException {
        incrementCounter(remoteAddress, true);

        scheduledExecutorService.schedule(() ->
                decrementCounter(remoteAddress), attemptExpirationTimeSeconds, TimeUnit.SECONDS);
    }

    private void incrementCounter(String remoteAddress, boolean failOnLimitExceeded) throws RateLimitExceededException {
        countByAddress.compute(
            remoteAddress,
            (k, v) -> {
                if (v == null || v == 0) return 1L;
                if (failOnLimitExceeded && v > attemptsLimit){
                    throw new RateLimitExceededException(v, remoteAddress);
                }
                return v + 1;
            }
        );
    }

    private void decrementCounter(String remoteAddress) {
        countByAddress.compute(
            remoteAddress,
            (k, v) -> v == null || v < 1 ? 0 : v - 1
        );
    }
}
