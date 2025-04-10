package com.alextim.infoname.controller;

import com.alextim.infoname.model.InfoName;
import com.alextim.infoname.service.InfoNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.alextim.ratelimiter.annotation.RateLimited;
import com.alextim.ratelimiter.exceptions.RateLimitExceededException;

import java.util.Map;

@RestController
@RequestMapping("/info/{name}")
@RequiredArgsConstructor
@Slf4j
public class InfoNameController {

    private final InfoNameService infoNameService;

    @GetMapping
    @RateLimited
    public InfoName infoName(@PathVariable String name) {
        log.info("incoming request for getting info name: {}", name);
        InfoName info = infoNameService.getInfo(name);
        log.info("answer: {}", info);
        return info;
    }

    @ExceptionHandler(RateLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Object rateLimitExceeded(RateLimitExceededException e) {
        log.error("rateLimitExceeded: ", e);

        return Map.of(
            "Ошибка", "Превышено число допустимых запросов",
            "Совет", "Попробуйте позже"
        );
    }
}
