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

package com.alibaba.cloud.spring.boot.secretsmanager.config.utils;

import com.aliyuncs.exceptions.ClientException;

public class BackOffUtils {


    /**
     * 规避策略默认最大时间间隔
     */
    private final static long BACKOFF_DEFAULT_CAPACITY = 10000L;

    /**
     * 规避策略时间间隔
     */
    private final static long BACKOFF_DEFAULT_RETRY_INTERVAL = 1000L;

    /**
     * KMS限流返回错误码
     */
    private final static String REJECTED_THROTTLING = "Rejected.Throttling";
    /**
     * KMS服务不可用返回错误码
     */
    private final static String SERVICE_UNAVAILABLE_TEMPORARY = "ServiceUnavailableTemporary";
    /**
     * KMS服务内部错误返回错误码
     */
    private final static String INTERNAL_FAILURE = "InternalFailure";

    /**
     * 规避策略重试次数
     */
    public final static int BACKOFF_DEFAULT_RETRY_TIMES = 5;


    public static boolean judgeNeedRetry(ClientException e) {
        return BackOffUtils.REJECTED_THROTTLING.equals(e.getErrCode()) || BackOffUtils.SERVICE_UNAVAILABLE_TEMPORARY.equals(e.getErrCode()) || BackOffUtils.INTERNAL_FAILURE.equals(e.getErrCode());
    }


    public static long getWaitTimeExponential(int retryTimes) {
        return Math.min(BACKOFF_DEFAULT_CAPACITY, (long) (Math.pow(2, retryTimes) * BACKOFF_DEFAULT_RETRY_INTERVAL));
    }

}
