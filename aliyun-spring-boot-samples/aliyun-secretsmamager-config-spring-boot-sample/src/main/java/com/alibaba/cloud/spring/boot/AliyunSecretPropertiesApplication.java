package com.alibaba.cloud.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AliyunSecretPropertiesApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(AliyunSecretPropertiesApplication.class)
                .logStartupInfo(false)
                .run(args);
    }

}
