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

package com.alibaba.cloud.examples.schedulerx.job.annotation;

import com.alibaba.schedulerx.common.domain.ExecuteMode;
import com.alibaba.schedulerx.scheduling.annotation.SchedulerX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring scheduled job
 * @author yaohui
 * @create 2022/10/17 下午2:51
 **/
@Component
public class SpringJob {

    /**
     * log4j2/logback配置schedulerxLogAppender，可以进行日志采集
     */
    private static final Logger logger = LoggerFactory.getLogger("schedulerx");

    /**
     * An spring scheduled job
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void simpleJob() {
        logger.info("hello schedulerx! this's an spring simple job.");
    }

    /**
     * An spring scheduled job
     * Can use @SchedulerX annotation set job config when open sync(true)
     */
    @SchedulerX(name = "simpleJobWithAnnotation", cron = "0 0/1 * * * ?", model = ExecuteMode.STANDALONE)
    public void simpleJobWithAnnotation() {
        logger.info("hello schedulerx! this's an spring simple job with SchedulerX annotation.");
    }
}
