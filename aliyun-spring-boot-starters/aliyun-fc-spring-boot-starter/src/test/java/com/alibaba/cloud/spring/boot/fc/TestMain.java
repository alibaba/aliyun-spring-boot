package com.alibaba.cloud.spring.boot.fc;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@SpringBootApplication
public class TestMain {

    public static void main(String[] args) {
        SpringApplication.run(TestMain.class, args);
    }

    @Bean
    public Function<String, String> string2string() {
        return string -> "receive string:" + string;
    }

    @Bean
    public Function<String, User> string2object() {
        return string -> new User(0L, "receive string:" + string);
    }

}
