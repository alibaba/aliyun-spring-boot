/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.spring.boot.examples.compileflow;

import com.alibaba.cloud.spring.boot.examples.compileflow.mock.KtvService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The bootstrap for Aliyun Compileflow Spring Boot
 *
 * @author yusu
 */
@SpringBootApplication
public class CompileflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompileflowApplication.class, args);
    }

    @Bean
    public KtvService ktvService() {
        return new KtvService();
    }

}
