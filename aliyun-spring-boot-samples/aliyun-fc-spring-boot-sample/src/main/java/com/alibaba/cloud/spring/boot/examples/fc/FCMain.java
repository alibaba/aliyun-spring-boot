/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.spring.boot.examples.fc;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StreamUtils;

import com.alibaba.cloud.spring.boot.fc.InputOutputFunction;
import com.aliyun.fc.runtime.Context;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@SpringBootApplication
public class FCMain {

    public static void main(String[] args) {
        SpringApplication.run(FCMain.class, args);
    }

    @Bean
    public Function<User, String> user2string(Context context) {
        return
                user -> {
                    context.getLogger().info("dasdasd");
                    return user.toString();
                };
    }

    @Bean
    public Function<String, User> string2user(Context context) {
        return
                str -> {
                    context.getLogger().info("request string is :" + str);
                    User user = new User();
                    user.setName("zhangsan");
                    return user;
                };
    }

    @Bean
    public Supplier<List<User>> getAll(Context context) {
        return () -> {
            context.getLogger().info("start to get all users");
            ArrayList<User> result = new ArrayList<>();
            result.add(new User("mock"));
            return result;
        };
    }

    @Bean
    public Function<String, String> string2string(Context context) {
        return str -> {
            context.getLogger().info("received msg :" + str + ", requestId = " + context.getRequestId());
            return "convert to Upper Case : " + str.toUpperCase();
        };
    }

    @Bean
    public Function<User, Message> user2message() {
        return user -> {
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("test", 1);
            headerMap.put("hi", "Spring Cloud Function");
            MessageHeaders headers = new MessageHeaders(headerMap);
            return MessageBuilder.createMessage("hi: " + user.getName(), headers);
        };
    }


    @Bean
    public InputOutputFunction stream() {
        return
                (in, out) -> {
                    try {
                        String input = StreamUtils.copyToString(in, Charset.forName("UTF-8"));

                        System.out.println(input);

                        OutputStreamWriter wrapper = new OutputStreamWriter(out);
                        wrapper.write("outout !!!!!! " + input);
                        wrapper.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
    }

    @Bean
    public Consumer<User> user(Context context) {
        return user -> {
            context.getLogger().info("userName = " + user.getName());
        };
    }

    public static class User {

        private String name;

        public User() {
        }

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
