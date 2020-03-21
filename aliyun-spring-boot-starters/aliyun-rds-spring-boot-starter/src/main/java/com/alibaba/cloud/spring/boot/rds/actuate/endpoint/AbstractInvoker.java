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

package com.alibaba.cloud.spring.boot.rds.actuate.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.cloud.spring.boot.rds.env.RdsProperties;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.RpcAcsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public abstract class AbstractInvoker {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected IAcsClient iAcsClient;

	@Autowired
	protected RdsProperties rdsProperties;

	private static Map<String, DescribeDBInstancesResponse.DBInstance> instanceMap;

	protected <K extends AcsResponse> K invoke(RpcAcsRequest<K> param) {
		try {
			K resp = iAcsClient.getAcsResponse(param);
			return resp;
		}
		catch (ClientException e) {
			log.error("invoke iAcsClient error", e);
		}
		return null;
	}

	protected DescribeDBInstancesResponse.DBInstance getInstance(String instanceId) {
		refreshAllInstance();
		return instanceMap.get(instanceId);
	}

	protected List<DescribeDBInstancesResponse.DBInstance> getInstances() {
		refreshAllInstance();
		return instanceMap.values().stream()
				.sorted(Comparator.comparing(
						DescribeDBInstancesResponse.DBInstance::getDBInstanceId))
				.collect(Collectors.toList());
	}

	/**
	 * cache 1 hour.
	 */
	private static final long CACHE_TIME = 1L * 60 * 60 * 1000;

	private static volatile Long refreshExpire = null;

	/**
	 * try to refresh instance info.
	 */
	private void refreshAllInstance() {

		if (refreshExpire != null && System.currentTimeMillis() < refreshExpire) {
			return;
		}

		synchronized (AbstractInvoker.class) {
			if (refreshExpire != null && System.currentTimeMillis() < refreshExpire) {
				return;
			}

			DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();

			Set<String> allRegionIds = new LinkedHashSet<>();
			if (rdsProperties.getDefaultRegionId() != null
					&& rdsProperties.getDefaultRegionId().length() > 0) {
				allRegionIds.add(rdsProperties.getDefaultRegionId());
			}
			if (rdsProperties.getAllRegionIds() != null
					&& rdsProperties.getAllRegionIds().length() > 0) {
				allRegionIds.addAll(
						Arrays.asList(rdsProperties.getAllRegionIds().split(",")));
			}
			List<DescribeDBInstancesResponse.DBInstance> instances = new ArrayList<>();
			for (String regionId : allRegionIds) {
				request.setSysRegionId(regionId);
				DescribeDBInstancesResponse resp = invoke(request);
				if (resp == null) {
					continue;
				}
				instances.addAll(resp.getItems());
			}

			instanceMap = instances.stream()
					.collect(Collectors.toMap(
							DescribeDBInstancesResponse.DBInstance::getDBInstanceId,
							Function.identity()));

			refreshExpire = System.currentTimeMillis() + CACHE_TIME;
		}
	}

}
