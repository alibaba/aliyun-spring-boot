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

package com.alibaba.cloud.spring.boot.rds.env;

/**
 * the rds config contants.
 *
 * @author theonefx
 */
public final class RDSConstants {

    /**
     * Prefix of RDSConfigurationProperties.
     */
    public static final String PROPERTY_PREFIX = "alibaba.cloud.rds";

    /**
     * Enable RDS.
     */
    public static final String ENABLE = PROPERTY_PREFIX + ".enable";

    /**
     * Prefix of Rds actuator.
     */
    public static final String ACTUATOR_PREFIX = PROPERTY_PREFIX + ".actuator";

    /**
     * Enable RDS actuator.
     */
    public static final String ENABLE_ACTUATOR = ACTUATOR_PREFIX + ".enable";

    private RDSConstants() {
        throw new AssertionError("Must not instantiate constant utility class");
    }

}
