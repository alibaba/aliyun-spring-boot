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

package com.alibaba.cloud.spring.boot.schedulerx.actuate.autoconfigure;

import com.alibaba.cloud.spring.boot.context.env.EdasProperties;
import com.alibaba.cloud.spring.boot.schedulerx.env.SchedulerXProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaolongzuo
 */
@Endpoint(id = "schedulerx")
public class SchedulerXEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerXEndpoint.class);

    private SchedulerXProperties schedulerXProperties;

    private EdasProperties edasProperties;

    public SchedulerXEndpoint(EdasProperties edasProperties, SchedulerXProperties schedulerXProperties) {
        this.edasProperties = edasProperties;
        this.schedulerXProperties = schedulerXProperties;
    }

    /**
     * @return scx endpoint
     */
    @ReadOperation
    public Map<String, Object> invoke() {
        Map<String, Object> scxEndpoint = new HashMap<>();
        LOGGER.info("SCX endpoint invoke, scxProperties is {}", schedulerXProperties);
        scxEndpoint.put("namespace",
                edasProperties == null ? "" : edasProperties.getNamespace());
        scxEndpoint.put("scxProperties", schedulerXProperties);
        return scxEndpoint;
    }

}
