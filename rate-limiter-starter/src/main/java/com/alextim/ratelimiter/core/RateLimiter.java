package com.alextim.ratelimiter.core;

import com.alextim.ratelimiter.exceptions.RateLimitExceededException;

public interface RateLimiter {

    void increment(String remoteAddress) throws RateLimitExceededException;

}
