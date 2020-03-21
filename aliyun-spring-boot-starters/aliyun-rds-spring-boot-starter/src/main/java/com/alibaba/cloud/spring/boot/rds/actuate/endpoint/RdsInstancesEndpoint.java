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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeResourceUsageRequest;
import com.aliyuncs.rds.model.v20140815.DescribeResourceUsageResponse;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Endpoint(id = "rdsInstances")
public class RdsInstancesEndpoint extends AbstractInvoker {

	@ReadOperation
	public Map<String, Map<String, Object>> baseInfo() {
		Map<String, Map<String, Object>> map = new HashMap<>();

		List<DescribeDBInstancesResponse.DBInstance> instances = getInstances();
		instances.forEach(instance -> {
			Map<String, Object> dataOfInstance = instanceBaseInfo(instance);

			dataOfInstance.put("database", getDatabases(instance));
			dataOfInstance.put("usage", getUsageInfo(instance));

			map.put(instance.getDBInstanceId(), dataOfInstance);
		});

		appendProperties(map, instances);

		return map;
	}

	private Map<String, Object> instanceBaseInfo(
			DescribeDBInstancesResponse.DBInstance instance) {
		Map<String, Object> data = new LinkedHashMap<>();

		data.put("ID", instance.getDBInstanceId());
		data.put("Description", instance.getDBInstanceDescription());
		data.put("PayType", instance.getPayType());
		data.put("DBInstanceType", instance.getDBInstanceType());
		data.put("DBInstanceNetType", instance.getDBInstanceNetType());
		data.put("InstanceNetworkType", instance.getInstanceNetworkType());
		data.put("ConnectionMode", instance.getConnectionMode());
		data.put("RegionId", instance.getRegionId());
		data.put("ZoneId", instance.getZoneId());
		data.put("Engine", instance.getEngine());
		data.put("EngineVersion", instance.getEngineVersion());
		return data;
	}

	private Map<String, Object> getUsageInfo(
			DescribeDBInstancesResponse.DBInstance instance) {
		Map<String, Object> data = new LinkedHashMap<>();
		DescribeResourceUsageRequest usageReq = new DescribeResourceUsageRequest();
		usageReq.setDBInstanceId(instance.getDBInstanceId());
		usageReq.setSysRegionId(instance.getRegionId());
		DescribeResourceUsageResponse resp = invoke(usageReq);

		data.put("DiskUsed", resp.getDiskUsed());
		data.put("DataSize", resp.getDataSize());
		data.put("LogSize", resp.getLogSize());
		data.put("BackupSize", resp.getBackupSize());
		data.put("BackupOssDataSize", resp.getBackupOssDataSize());
		data.put("BackupOssLogSize", resp.getBackupOssLogSize());
		data.put("SQLSize", resp.getSQLSize());
		data.put("ColdBackupSize", resp.getColdBackupSize());
		return data;
	}

	private List<Map<String, Object>> getDatabases(
			DescribeDBInstancesResponse.DBInstance instance) {
		DescribeDatabasesRequest req = new DescribeDatabasesRequest();
		req.setDBInstanceId(instance.getDBInstanceId());
		req.setSysRegionId(instance.getRegionId());
		DescribeDatabasesResponse resp = invoke(req);

		return resp.getDatabases().stream().map(db -> {
			Map<String, Object> data = new LinkedHashMap<>();
			data.put("DBName", db.getDBName());
			data.put("CharacterSetName", db.getCharacterSetName());
			data.put("DBDescription", db.getDBDescription());
			data.put("DBStatus", db.getDBStatus());
			return data;
		}).collect(Collectors.toList());

	}

	private void appendProperties(Map<String, Map<String, Object>> map,
			List<DescribeDBInstancesResponse.DBInstance> instances) {
		instances.stream()
				.collect(Collectors
						.groupingBy(DescribeDBInstancesResponse.DBInstance::getRegionId))
				.forEach((regionId, list) -> setProperties(map, regionId, list));
	}

	private void setProperties(Map<String, Map<String, Object>> map, String regionId,
			List<DescribeDBInstancesResponse.DBInstance> instances) {
		if (instances == null || instances.size() == 0) {
			return;
		}

		DescribeDBInstanceAttributeRequest req = new DescribeDBInstanceAttributeRequest();
		req.setSysRegionId(regionId);

		int idx = 0;
		int batch = 30;
		do {
			int end = Math.min(idx + batch, instances.size());
			List<DescribeDBInstancesResponse.DBInstance> subList = instances.subList(idx,
					end);

			req.setDBInstanceId(subList.stream()
					.map(DescribeDBInstancesResponse.DBInstance::getDBInstanceId)
					.collect(Collectors.joining(",")));

			DescribeDBInstanceAttributeResponse resp = invoke(req);

			Map<String, DescribeDBInstanceAttributeResponse.DBInstanceAttribute> propMap;
			if (resp == null || resp.getItems() == null) {
				propMap = Collections.emptyMap();
			}
			else {
				propMap = resp.getItems().stream().collect(Collectors.toMap(
						DescribeDBInstanceAttributeResponse.DBInstanceAttribute::getDBInstanceId,
						Function.identity()));
			}

			subList.stream().forEach(instance -> {
				Map<String, Object> dataOfInstance = map.get(instance.getDBInstanceId());
				DescribeDBInstanceAttributeResponse.DBInstanceAttribute prop = propMap
						.get(instance.getDBInstanceId());
				dataOfInstance.put("properties", prop);
			});

			idx = end;
		}
		while (idx < instances.size());

	}

}
