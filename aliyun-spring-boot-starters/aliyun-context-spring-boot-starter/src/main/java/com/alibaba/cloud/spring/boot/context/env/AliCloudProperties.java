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

package com.alibaba.cloud.spring.boot.context.env;

import com.alibaba.cloud.context.AliCloudConfiguration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import static com.alibaba.cloud.spring.boot.context.env.AliCloudProperties.PROPERTY_PREFIX;

/**
 * The {@link ConfigurationProperties Properties} class for Alibaba Cloud
 *
 * @author xiaolongzuo
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@ConfigurationProperties(PROPERTY_PREFIX)
public class AliCloudProperties implements AliCloudConfiguration {

    /**
     * The prefix of the property of {@link AliCloudProperties}.
     */
    public static final String PROPERTY_PREFIX = "alibaba.cloud";

    /**
     * The the property of {@link #getAccessKey()}.
     */
    public static final String ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".access-key";

    /**
     * The prefix of the property of {@link #getSecretKey()}.
     */
    public static final String SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".secret-key";

    /**
     * oss access_key
     */
    public static final String OSS_ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".oss.access-key";

    /**
     * oss secret-key
     */
    public static final String OSS_SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".oss-secret-key";

    /**
     * rds access_key
     */
    public static final String RDS_ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".rds-access-key";

    /**
     * rds secret-key
     */
    public static final String RDS_SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".rds-secret-key";


    /**
     * redis access_key
     */
    public static final String REDIS_ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".redis-access-key";

    /**
     * redis secret-key
     */
    public static final String REDIS_SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".redis-secret-key";

    /**
     * sms access_key
     */
    public static final String SMS_ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".sms-access-key";

    /**
     * sms secret-key
     */
    public static final String SMS_SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".sms-secret-key";

    /**
     * edas access_key
     */
    public static final String EDAS_ACCESS_KEY_PROPERTY = PROPERTY_PREFIX + ".edas-access-key";

    /**
     * edas secret-key
     */
    public static final String EDAS_SECRET_KEY_PROPERTY = PROPERTY_PREFIX + ".edas-secret-key";

    /**
     * alibaba cloud access key.
     */
    private String accessKey;

    /**
     * alibaba cloud secret key.
     */
    private String secretKey;

    /**
     * alibaba cloud OSS access key.
     */

    private String ossAccessKey;

    /**
     * alibaba cloud OSS secret key.
     */
    private String ossSecretKey;

    /**
     * alibaba cloud RDS access key.
     */
    private String rdsAccessKey;

    /**
     * alibaba cloud RDS secret key.
     */
    private String rdsSecretKey;

    /**
     * alibaba cloud Redis access key.
     */
    private String redisAccessKey;

    /**
     * alibaba cloud Redis secret key.
     */
    private String redisSecretKey;

    /**
     * alibaba cloud SMS access key.
     */
//    @Value(SMS_ACCESS_KEY_PROPERTY)
    private String smsAccessKey;

    /**
     * alibaba cloud SMS secret key.
     */
//    @Value(SMS_SECRET_KEY_PROPERTY)
    private String smsSecretKey;


    /**
     * alibaba cloud Edas access key.
     */
    private String edasAccessKey;

    /**
     * alibaba cloud Edas secret key.
     */
    private String edasSecretKey;


    @Override
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public String getOssAccessKey() {
        return StringUtils.isEmpty(ossAccessKey)? getAccessKey():ossAccessKey;
    }

    public void setOssAccessKey(String ossAccessKey) {
        this.ossAccessKey = ossAccessKey;
    }

    public String getOssSecretKey() {
        return StringUtils.isEmpty(ossSecretKey)? getSecretKey():ossSecretKey;
    }

    public void setOssSecretKey(String ossSecretKey) {
        this.ossSecretKey = ossSecretKey;
    }

    public String getRdsAccessKey() {
        return StringUtils.isEmpty(rdsAccessKey)? getAccessKey():rdsAccessKey;
    }

    public void setRdsAccessKey(String rdsAccessKey) {
        this.rdsAccessKey = rdsAccessKey;
    }

    public String getRdsSecretKey() {
        return StringUtils.isEmpty(rdsSecretKey)? getSecretKey():rdsSecretKey;
    }

    public void setRdsSecretKey(String rdsSecretKey) {
        this.rdsSecretKey = rdsSecretKey;
    }

    public String getRedisAccessKey() {
        return StringUtils.isEmpty(redisAccessKey)? getAccessKey():redisAccessKey;
    }

    public void setRedisAccessKey(String redisAccessKey) {
        this.redisAccessKey = redisAccessKey;
    }

    public String getRedisSecretKey() {
        return StringUtils.isEmpty(redisSecretKey)? getSecretKey():redisSecretKey;
    }

    public void setRedisSecretKey(String redisSecretKey) {
        this.redisSecretKey = redisSecretKey;
    }

    public String getSmsAccessKey() {
        return StringUtils.isEmpty(smsAccessKey)? getAccessKey():smsAccessKey;
    }

    public void setSmsAccessKey(String smsAccessKey) {
        this.smsAccessKey = smsAccessKey;
    }

    public String getSmsSecretKey() {
        return StringUtils.isEmpty(smsSecretKey)? getSecretKey():smsSecretKey;
    }

    public void setSmsSecretKey(String smsSecretKey) {
        this.smsSecretKey = smsSecretKey;
    }

    public String getEdasAccessKey() {
        return StringUtils.isEmpty(edasAccessKey)? getAccessKey():edasAccessKey;
    }

    public void setEdasAccessKey(String edasAccessKey) {
        this.edasAccessKey = edasAccessKey;
    }

    public String getEdasSecretKey() {
        return StringUtils.isEmpty(edasSecretKey)? getSecretKey():edasSecretKey;
    }

    public void setEdasSecretKey(String edasSecretKey) {
        this.edasSecretKey = edasSecretKey;
    }

}
