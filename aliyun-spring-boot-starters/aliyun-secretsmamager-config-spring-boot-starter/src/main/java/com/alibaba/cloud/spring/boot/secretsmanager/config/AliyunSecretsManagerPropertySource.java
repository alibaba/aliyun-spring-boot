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

package com.alibaba.cloud.spring.boot.secretsmanager.config;

import com.alibaba.cloud.spring.boot.secretsmanager.config.utils.BackOffUtils;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.kms.model.v20160120.GetSecretValueRequest;
import com.aliyuncs.kms.model.v20160120.GetSecretValueResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.core.env.EnumerablePropertySource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AliyunSecretsManagerPropertySource extends EnumerablePropertySource<IAcsClient> {

    private final String secretName;

    private final Map<String, Object> properties = new LinkedHashMap<>();

    public AliyunSecretsManagerPropertySource(String secretName, IAcsClient acsClient) {
        super(secretName, acsClient);
        this.secretName = secretName;
    }

    public void init() {
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest();
        getSecretValueRequest.setSecretName(secretName);
        readSecretValue(getSecretValueRequest);
    }

    @Override
    public String[] getPropertyNames() {
        Set<String> strings = properties.keySet();
        return strings.toArray(new String[strings.size()]);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    private void readSecretValue(GetSecretValueRequest secretValueRequest) {
        for (int i = 0; i < BackOffUtils.BACKOFF_DEFAULT_RETRY_TIMES; i++) {
            try {
                GetSecretValueResponse secretValueResponse = source.getAcsResponse(secretValueRequest);
                JsonObject jsonObject = new JsonParser().parse(secretValueResponse.getSecretData()).getAsJsonObject();
                for (Map.Entry<String, JsonElement> secretEntry : jsonObject.entrySet()) {
                    properties.put(secretEntry.getKey(), secretEntry.getValue());
                }
                break;
            } catch (ClientException e) {
                if (!BackOffUtils.judgeNeedRetry(e)) {
                    throw new RuntimeException(e);
                } else {
                    long waitTimeExponential = BackOffUtils.getWaitTimeExponential(i);
                    try {
                        logger.info(String.format("GetSecretValue  retry {waitTimeExponential: %s ,secretName: %s}", waitTimeExponential, secretValueRequest.getSecretName()));
                        Thread.sleep(waitTimeExponential);
                    } catch (InterruptedException ignore) {
                    }
                }
            }

        }
    }


}