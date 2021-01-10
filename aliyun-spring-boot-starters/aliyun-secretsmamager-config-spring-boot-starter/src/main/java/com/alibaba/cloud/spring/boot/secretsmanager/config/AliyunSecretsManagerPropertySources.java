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

import java.util.ArrayList;
import java.util.List;

import com.alibaba.cloud.spring.boot.secretsmanager.config.exception.AliyunSecretsManagerPropertySourceNotFoundException;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.utils.StringUtils;
import org.apache.commons.logging.Log;

public class AliyunSecretsManagerPropertySources {

    private final AliyunSecretsManagerProperties properties;

    private final Log log;

    public AliyunSecretsManagerPropertySources(AliyunSecretsManagerProperties properties, Log log) {
        this.properties = properties;
        this.log = log;
    }

    public List<String> getAutomaticSecretNames(List<String> profiles) {
        List<String> secretNames = new ArrayList<>();
        String prefix = this.properties.getPrefix();
        String secretName = this.properties.getSecretName();
        if (!StringUtils.isEmpty(prefix)) {
            String appName = this.properties.getSecretName();
            secretName = prefix + "/" + appName;
        }
        addProfiles(secretNames, secretName, profiles);
        secretNames.add(secretName);
        return secretNames;
    }

    private void addProfiles(List<String> secretNames, String baseContext, List<String> profiles) {
        for (String profile : profiles) {
            secretNames.add(baseContext + this.properties.getProfileSeparator() + profile);
        }
    }

    public AliyunSecretsManagerPropertySource createPropertySource(String context, boolean optional, IAcsClient client) {
        log.info("Loading secrets from aliyun Secret Manager secret with name: " + context + ", optional: " + optional);
        try {
            AliyunSecretsManagerPropertySource propertySource = new AliyunSecretsManagerPropertySource(context, client);
            propertySource.init();
            return propertySource;
        } catch (Exception e) {
            if (!optional) {
                log.error(String.format("load aliyun secret: %s fail", context), e);
                throw new AliyunSecretsManagerPropertySourceNotFoundException(e);
            } else {
                log.warn("Unable to load aliyun secret from " + context, e);
            }
        }
        return null;
    }


}