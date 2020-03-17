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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * the rds properties.
 *
 * @author theonefx
 */
@ConfigurationProperties(prefix = RDSConstants.ACTUATOR_PREFIX)
public class RdsProperties {

	/**
	 * the default regionId.
	 */
	private String defaultRegionId;

	/**
	 * all rdb regionId list, splited by an comma.
	 */
	private String allRegionIds;

	/**
	 * the performace monitor key in actuator.
	 */
	private String performanceKey;

	public String getPerformanceKey() {
		return performanceKey;
	}

	public void setPerformanceKey(String performanceKey) {
		this.performanceKey = performanceKey;
	}

	public String getDefaultRegionId() {
		return defaultRegionId;
	}

	public void setDefaultRegionId(String defaultRegionId) {
		this.defaultRegionId = defaultRegionId;
	}

	public String getAllRegionIds() {
		return allRegionIds;
	}

	public void setAllRegionIds(String allRegionIds) {
		this.allRegionIds = allRegionIds;
	}

}
