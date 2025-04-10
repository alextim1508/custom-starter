package com.alextim.ratelimiter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rate-limiter")
public record RateLimiterProperties(
    Boolean enabled,
    Integer attemptsLimit,
    Integer attemptExpirationSeconds) {

}
