package com.alextim.infoname.client;

import com.alextim.infoname.util.TranslateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GenderClient {

    private final RestClient genderRestClient = RestClient.create();

    public String getGender(String name) {
        var response = genderRestClient.get()
            .uri("https://api.genderize.io?name={name}", name)
            .retrieve()
            .body(Map.class);
        log.info("api.genderize.io responce: {}", response);

        return Optional.ofNullable(response.get("gender"))
            .map(Object::toString)
            .map(TranslateUtil::translateGender)
            .orElse("Неизвестно")
            + TranslateUtil.translateToProbabilityEnding((Double) response.get("probability"));
    }
}
