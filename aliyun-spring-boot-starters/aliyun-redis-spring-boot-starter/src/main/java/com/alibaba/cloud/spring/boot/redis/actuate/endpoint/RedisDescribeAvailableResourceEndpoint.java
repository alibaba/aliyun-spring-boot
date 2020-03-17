/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.spring.boot.redis.actuate.endpoint;

import com.alibaba.cloud.spring.boot.context.env.AliCloudProperties;
import com.alibaba.cloud.spring.boot.redis.env.RedisProperties;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.lang.Nullable;

/**
 * The {@link Endpoint} for Alibaba Cloud Redis's <a href=
 * "https://help.aliyun.com/document_detail/120580.html">DescribeAvailableResource</a>
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Endpoint(id = "redisDescribeAvailableResource")
public class RedisDescribeAvailableResourceEndpoint extends AbstractRedisEndpoint {

	public RedisDescribeAvailableResourceEndpoint(AliCloudProperties aliCloudProperties,
												  RedisProperties redisProperties) {
		super(aliCloudProperties, redisProperties);
	}

	@ReadOperation
	public Object describeAvailableResourceWithDefaultRegionId() {
		return describeAvailableResource(getDefaultRegionID());
	}

	@ReadOperation
	public Object describeAvailableResource(@Selector @Nullable String regionId) {
		return execute(() -> {
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			request.setSysRegionId(regionId);
			return createIAcsClient(regionId).getAcsResponse(request);
		});
	}

}
