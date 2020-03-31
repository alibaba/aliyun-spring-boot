/*
 * Copyright 2013-2018 the original author or authors.
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

package com.alibaba.cloud.spring.boot.rds.actuate.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.spring.boot.context.env.AliCloudProperties;
import com.alibaba.cloud.spring.boot.rds.actuate.endpoint.RdsErrorEndpoint;
import com.alibaba.cloud.spring.boot.rds.actuate.endpoint.RdsInstancesEndpoint;
import com.alibaba.cloud.spring.boot.rds.actuate.endpoint.RdsPerformanceEndpoint;
import com.alibaba.cloud.spring.boot.rds.actuate.endpoint.RdsSlowEndpoint;
import com.alibaba.cloud.spring.boot.rds.env.RDSConstants;
import com.alibaba.cloud.spring.boot.rds.env.RdsProperties;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RdsProperties.class)
@ConditionalOnClass(Endpoint.class)
@ConditionalOnEnabledHealthIndicator(RDSConstants.MODULE_NAME)
public class RdsEndpointAutoConfiguration {

    @Autowired
    private RdsProperties rdsProperties;

    @Bean
    @ConditionalOnMissingBean(IAcsClient.class)
    public IAcsClient iAcsClient(AliCloudProperties aliCloudProperties) {
        DefaultProfile profile = DefaultProfile.getProfile(
                rdsProperties.getDefaultRegionId(), aliCloudProperties.getAccessKey(),
                aliCloudProperties.getSecretKey());
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    @Bean
    public RdsInstancesEndpoint rdsInstanceEndpoint() {
        return new RdsInstancesEndpoint();
    }

    @Bean
    public RdsSlowEndpoint rdsSlowEndpoint() {
        return new RdsSlowEndpoint();
    }

    @Bean
    public RdsPerformanceEndpoint rdsPerformanceEndpoint() {
        return new RdsPerformanceEndpoint();
    }

    @Bean
    public RdsErrorEndpoint errorEndpoint() {
        return new RdsErrorEndpoint();
    }

}
