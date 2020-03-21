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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeSlowLogsRequest;
import com.aliyuncs.rds.model.v20140815.DescribeSlowLogsResponse;

/**
 * show top 100 slow sql in rdb.
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Endpoint(id = "rdsSlow100")
public class RdsSlowEndpoint extends AbstractInvoker {

    private static final String TIME_PATTEN = "yyyy-MM-dd'Z'";

    private static final Long TIME_RANGE = 7L * 24 * 60 * 60 * 1000;

    @ReadOperation
    public Map<String, List<DescribeSlowLogsResponse.SQLSlowLog>> slow() {
        List<DescribeDBInstancesResponse.DBInstance> instances = getInstances();
        Map<String, List<DescribeSlowLogsResponse.SQLSlowLog>> map = instances.stream()
                .collect(Collectors.toMap(
                        DescribeDBInstancesResponse.DBInstance::getDBInstanceId,
                        this::slow));

        return map;
    }

    @ReadOperation
    public List<DescribeSlowLogsResponse.SQLSlowLog> slow(@Selector String instanceId) {
        DescribeDBInstancesResponse.DBInstance instance = getInstance(instanceId);
        return slow(instance);
    }

    @ReadOperation
    public List<DescribeSlowLogsResponse.SQLSlowLog> slow(@Selector String instanceId,
                                                          @Selector String start, @Selector String end) {
        DescribeDBInstancesResponse.DBInstance instance = getInstance(instanceId);
        return slow(instance, start, end);
    }

    private List<DescribeSlowLogsResponse.SQLSlowLog> slow(
            DescribeDBInstancesResponse.DBInstance instance) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTEN);

        Date end = new Date();
        Date start = new Date(end.getTime() - TIME_RANGE);

        return slow(instance, format.format(start), format.format(end));
    }

    private List<DescribeSlowLogsResponse.SQLSlowLog> slow(
            DescribeDBInstancesResponse.DBInstance instance, @Selector String start,
            @Selector String end) {
        DescribeSlowLogsRequest request = new DescribeSlowLogsRequest();
        request.setDBInstanceId(instance.getDBInstanceId());
        request.setSysRegionId(instance.getRegionId());
        request.setStartTime(start);
        request.setEndTime(end);
        request.setPageNumber(1);
        request.setPageSize(100);

        DescribeSlowLogsResponse resp = invoke(request);
        if (resp == null) {
            return Collections.emptyList();
        } else {
            return resp.getItems();
        }
    }

}
