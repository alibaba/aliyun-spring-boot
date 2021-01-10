/*
 * Copyright 2013-2021 the original author or authors.
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

package com.alibaba.cloud.spring.boot.secretsmanager.config.autoconfigure;

import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerProperties;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerPropertySourceLocator;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.*;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AliyunSecretsManagerProperties.class)
@ConditionalOnClass({IAcsClient.class, AliyunSecretsManagerPropertySourceLocator.class})
@ConditionalOnProperty(prefix = AliyunSecretsManagerProperties.CONFIG_PREFIX, name = "enabled", matchIfMissing = true)
public class AliyunSecretsManagerBootstrapConfiguration {

    private final Environment environment;

    public AliyunSecretsManagerBootstrapConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    AliyunSecretsManagerPropertySourceLocator AliyunSecretsManagerPropertySourceLocator(IAcsClient smClient,
                                                                                        AliyunSecretsManagerProperties properties) {
        if (!StringUtils.hasLength(properties.getSecretName())) {
            properties.setSecretName(this.environment.getProperty("spring.application.name"));
        }
        return new AliyunSecretsManagerPropertySourceLocator(smClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    IAcsClient acsClient(AliyunSecretsManagerProperties properties) {
        return createSecretsManagerClient(properties);
    }

    public static IAcsClient createSecretsManagerClient(AliyunSecretsManagerProperties properties) {
        IClientProfile profile;
        if (StringUtils.hasLength(properties.getRegionId())) {
            profile = DefaultProfile.getProfile(properties.getRegionId());
        } else {
            throw new IllegalArgumentException("Missing param [regionId]");
        }
        if (StringUtils.hasLength(properties.getEndpoint())) {
            DefaultProfile.addEndpoint(properties.getRegionId(), "kms", properties.getEndpoint());
        }
        HttpClientConfig clientConfig = HttpClientConfig.getDefault();
        clientConfig.setIgnoreSSLCerts(true);
        profile.setHttpClientConfig(clientConfig);
        DefaultAcsClient acsClient = new DefaultAcsClient(profile, getCredentialProvider(properties));
        acsClient.appendUserAgent("HTTPClient", "aliyun-spring-boot-config");
        return acsClient;
    }

    private static AlibabaCloudCredentialsProvider getCredentialProvider(AliyunSecretsManagerProperties properties) {

        if (!StringUtils.hasLength(properties.getCredentialsType())) {
            throw new IllegalArgumentException("Missing param [credentialsType]");
        }
        AlibabaCloudCredentialsProvider provider;
        switch (properties.getCredentialsType()) {
            case "ak":
                if (!StringUtils.hasLength(properties.getAccessKeyId())) {
                    throw new IllegalArgumentException("Missing param [accessKeyId]");
                }
                if (!StringUtils.hasLength(properties.getAccessKeySecret())) {
                    throw new IllegalArgumentException("Missing param [accessKeySecret]");
                }
                provider = new StaticCredentialsProvider(new BasicCredentials(properties.getAccessKeyId(), properties.getAccessKeySecret()));
                break;
            case "token":
                if (!StringUtils.hasLength(properties.getAccessTokenId())) {
                    throw new IllegalArgumentException("Missing param [accessTokenId]");
                }
                if (!StringUtils.hasLength(properties.getAccessToken())) {
                    throw new IllegalArgumentException("Missing param [accessToken]");
                }
                provider = new StaticCredentialsProvider(new BasicCredentials(properties.getAccessTokenId(), properties.getAccessToken()));
                break;
            case "sts":
            case "ram_role":
                if (!StringUtils.hasLength(properties.getAccessKeyId())) {
                    throw new IllegalArgumentException("Missing param [accessKeyId]");
                }
                if (!StringUtils.hasLength(properties.getAccessKeySecret())) {
                    throw new IllegalArgumentException("Missing param [accessKeySecret]");
                }
                if (!StringUtils.hasLength(properties.getRoleSessionName())) {
                    throw new IllegalArgumentException("Missing param [roleSessionName]");
                }
                if (!StringUtils.hasLength(properties.getRoleArn())) {
                    throw new IllegalArgumentException("Missing param [roleArn]");
                }
                provider = new STSAssumeRoleSessionCredentialsProvider(properties.getAccessKeyId(), properties.getAccessKeySecret(), properties.getRoleSessionName(), properties.getRoleArn(), properties.getRegionId(), properties.getPolicy());
                break;
            case "ecs_ram_role":
                if (!StringUtils.hasLength(properties.getEcsRoleName())) {
                    throw new IllegalArgumentException("Missing param [ecsRoleName]");
                }
                provider = new InstanceProfileCredentialsProvider(properties.getEcsRoleName());
                break;
            default:
                throw new IllegalArgumentException(String.format("param[%s] is illegal", properties.getCredentialsType()));
        }
        return provider;
    }

}
