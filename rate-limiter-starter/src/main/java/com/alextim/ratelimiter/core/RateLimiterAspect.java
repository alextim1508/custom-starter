package com.alextim.ratelimiter.core;

import com.alextim.ratelimiter.annotation.RateLimited;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RateLimiter rateLimiter;
    private final HttpServletRequest httpServletRequest;

    @Around(value = "@annotation(annotation)")
    public Object handleInRateLimiter(final ProceedingJoinPoint joinPoint, final RateLimited annotation) throws Throwable {
        rateLimiter.increment(httpServletRequest.getRemoteAddr() + ":" + httpServletRequest.getRequestURI());
        return joinPoint.proceed();
    }
}
