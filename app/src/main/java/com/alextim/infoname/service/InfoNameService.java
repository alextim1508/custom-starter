package com.alextim.infoname.service;

import com.alextim.infoname.client.AgeClient;
import com.alextim.infoname.client.GenderClient;
import com.alextim.infoname.model.InfoName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoNameService {

    private final GenderClient genderClient;
    private final AgeClient ageClient;

    @Cacheable("names-cache")
    public InfoName getInfo(String name) {
        String gender = genderClient.getGender(name);
        log.info("gender {}", gender);

        String age = ageClient.getAge(name);
        log.info("age {}", age);

        return new InfoName(name, gender, age);
    }
}
