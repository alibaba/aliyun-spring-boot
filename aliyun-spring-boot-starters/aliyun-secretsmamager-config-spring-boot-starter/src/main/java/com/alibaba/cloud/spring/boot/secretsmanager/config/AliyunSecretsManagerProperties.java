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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@ConfigurationProperties(prefix = AliyunSecretsManagerProperties.CONFIG_PREFIX)
public class AliyunSecretsManagerProperties implements Validator {

    public static final String CONFIG_PREFIX = "aliyun.secretsmanager";

    private static final Pattern PREFIX_PATTERN = Pattern.compile("(/[a-zA-Z0-9.\\-_]+)*");

    private static final Pattern PROFILE_SEPARATOR_PATTERN = Pattern.compile("[a-zA-Z0-9.\\-_/\\\\]+");

    private String prefix = "";

    private String profileSeparator = "_";

    private boolean failFast = true;

    private String regionId;

    private String endpoint;

    private String secretName;

    private String accessKeyId;

    private String accessKeySecret;

    private boolean enabled = true;

    private String credentialsType;

    private String accessTokenId;

    private String accessToken;

    private String roleSessionName;

    private String roleArn;

    private String policy;

    private String ecsRoleName;

    @Override
    public boolean supports(Class<?> clazz) {
        return AliyunSecretsManagerProperties.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AliyunSecretsManagerProperties properties = (AliyunSecretsManagerProperties) target;

        if (!StringUtils.hasLength(properties.getProfileSeparator())) {
            errors.rejectValue("profileSeparator", "NotEmpty", "profileSeparator should not be empty or null.");
        }

        if (StringUtils.hasLength(properties.getPrefix())) {
            if (!PREFIX_PATTERN.matcher(properties.getPrefix()).matches()) {
                errors.rejectValue("prefix", "Pattern", "The prefix must have pattern of:  " + PREFIX_PATTERN.toString());
            }
        }
        if (!PROFILE_SEPARATOR_PATTERN.matcher(properties.getProfileSeparator()).matches()) {
            errors.rejectValue("profileSeparator", "Pattern",
                    "The profileSeparator must have pattern of:  " + PROFILE_SEPARATOR_PATTERN.toString());
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProfileSeparator() {
        return profileSeparator;
    }

    public void setProfileSeparator(String profileSeparator) {
        this.profileSeparator = profileSeparator;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return this.accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getCredentialsType() {
        return this.credentialsType;
    }

    public void setCredentialsType(String credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getAccessTokenId() {
        return this.accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRoleSessionName() {
        return this.roleSessionName;
    }

    public void setRoleSessionName(String roleSessionName) {
        this.roleSessionName = roleSessionName;
    }

    public String getRoleArn() {
        return this.roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getEcsRoleName() {
        return this.ecsRoleName;
    }

    public void setEcsRoleName(String ecsRoleName) {
        this.ecsRoleName = ecsRoleName;
    }
}