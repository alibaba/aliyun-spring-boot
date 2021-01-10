package com.alibaba.cloud.spring.boot.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class MyService {

    public ResponseEntity<String> test3rdService() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> result = template.getForEntity("http://localhost:8080/actuator/info", String.class);
        return result;
    }
}
