package com.alextim.ratelimiter;

import com.alextim.ratelimiter.config.RateLimiterProperties;
import com.alextim.ratelimiter.core.RateLimiter;
import com.alextim.ratelimiter.core.RateLimiterAspect;
import com.alextim.ratelimiter.core.RateLimiterInMemoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnProperty(
    value = "RateLimiterProperties",
    havingValue = "true",
    matchIfMissing = true
)
@EnableConfigurationProperties(RateLimiterProperties.class)
public class RateLimiterAutoConfiguration {

    @Bean
    public RateLimiterAspect rateLimiterAspect(RateLimiter rateLimiter, HttpServletRequest httpServletRequest) {
        return new RateLimiterAspect(rateLimiter, httpServletRequest);
    }

    @Bean
    public RateLimiter rateLimiter(RateLimiterProperties rateLimiterProperties) {
        return new RateLimiterInMemoryImpl(rateLimiterProperties);
    }
}
