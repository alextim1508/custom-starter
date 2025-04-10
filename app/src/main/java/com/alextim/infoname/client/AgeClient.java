package com.alextim.infoname.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AgeClient {

    private final RestClient ageRestClient = RestClient.create();

    public String getAge(String name) {
        var response = ageRestClient.get()
            .uri("https://api.agify.io?name={name}", name)
            .retrieve()
            .body(Map.class);
        log.info("api.agify.io responce: {}", response);

        return Optional.ofNullable(response.get("age"))
            .map(it -> it + " лет (средний)")
            .orElse("Нет данных");
    }

}
