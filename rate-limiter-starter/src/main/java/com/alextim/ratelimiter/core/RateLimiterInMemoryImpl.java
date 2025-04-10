package com.alextim.ratelimiter.core;

import com.alextim.ratelimiter.config.RateLimiterProperties;
import com.alextim.ratelimiter.exceptions.RateLimitExceededException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
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
        log.info("decrement counter for {}", remoteAddress);

        Long requestCount = countByAddress.compute(
                remoteAddress,
                (k, v) -> {
                    if (v == null || v == 0) return 1L;
                    if (failOnLimitExceeded && v > attemptsLimit) {
                        throw new RateLimitExceededException(v, remoteAddress);
                    }
                    return v + 1;
                }
        );
        log.info("request count: {}", requestCount);
    }

    private void decrementCounter(String remoteAddress) {
        log.info("decrement counter for {}", remoteAddress);

        countByAddress.compute(
            remoteAddress,
            (k, v) -> v == null || v < 1 ? 0 : v - 1
        );
    }
}
