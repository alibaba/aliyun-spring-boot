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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import com.alibaba.cloud.spring.boot.rds.env.RdsProperties;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancePerformanceRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancePerformanceResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;

/**
 * show rds preformance info by api.
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Endpoint(id = "rdsPerformance")
public class RdsPerformanceEndpoint extends AbstractInvoker {

    private static final String MYSQL_DEFAULT_KEYS = "MySQL_NetworkTraffic,MySQL_QPSTPS,MySQL_Sessions,"
            + "MySQL_InnoDBBufferRatio,MySQL_InnoDBDataReadWriten,MySQL_InnoDBLogRequests,MySQL_InnoDBLogWrites,"
            + "MySQL_TempDiskTableCreates,MySQL_MyISAMKeyBufferRatio,MySQL_MyISAMKeyReadWrites,MySQL_COMDML,"
            + "MySQL_RowDML,MySQL_MemCpuUsage,MySQL_IOPS,MySQL_DetailedSpaceUsage,MySQL_CPS,slavestat";

    private static final String SQL_SERVER_DEFAULT_KEYS = "SQLServer_Transactions,SQLServer_Sessions,"
            + "SQLServer_BufferHit,SQLServer_FullScans,SQLServer_SQLCompilations,SQLServer_CheckPoint,"
            + "SQLServer_Logins,SQLServer_LockTimeout,SQLServer_Deadlock,SQLServer_LockWaits,"
            + "SQLServer_NetworkTraffic,SQLServer_QPS,SQLServer_InstanceCPUUsage,SQLServer_IOPS,SQLServer_SpaceUsage";

    private static final String POSTGRE_SQL_DEFAULT_KEYS = "MemoryUsage,CpuUsage,PgSQL_SpaceUsage,PgSQL_IOPS,"
            + "PgSQL_Session";

    private static final Long TIME_RANGE = 12L * 60 * 60 * 1000;

    @Autowired
    private RdsProperties rdsProperties;

    @ReadOperation
    public Map<String, Object> performance() {
        Map<String, Object> map = new LinkedHashMap<>();

        List<DescribeDBInstancesResponse.DBInstance> instances = getInstances();

        instances.forEach(instance -> {
            Map<String, Object> dataOfInstance = performanceInfo(instance);
            map.put(instance.getDBInstanceId(), dataOfInstance);
        });

        return map;
    }

    @ReadOperation
    public Map<String, Object> performance(@Selector String instanceId) {
        DescribeDBInstancesResponse.DBInstance instance = getInstance(instanceId);
        if (instance == null) {
            return null;
        } else {
            return performanceInfo(instance);
        }
    }

    private Map<String, Object> performanceInfo(
            DescribeDBInstancesResponse.DBInstance instance) {

        String keys = getKeys(instance);

        if (keys == null || keys.length() == 0) {
            return Collections.emptyMap();
        }

        Date end = new Date();
        Date start = new Date(end.getTime() - TIME_RANGE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

        DescribeDBInstancePerformanceRequest req = new DescribeDBInstancePerformanceRequest();
        req.setDBInstanceId(instance.getDBInstanceId());
        req.setStartTime(simpleDateFormat.format(start));
        req.setEndTime(simpleDateFormat.format(end));
        req.setKey(keys);

        DescribeDBInstancePerformanceResponse resp = invoke(req);
        if (resp == null) {
            return Collections.emptyMap();
        }
        return resp.getPerformanceKeys().stream()
                .collect(Collectors.toMap(
                        DescribeDBInstancePerformanceResponse.PerformanceKey::getKey,
                        Function.identity(), (x, y) -> x));
    }

    private String getKeys(DescribeDBInstancesResponse.DBInstance instance) {
        if (rdsProperties.getPerformanceKey() != null
                && rdsProperties.getPerformanceKey().length() > 0) {
            return rdsProperties.getPerformanceKey();
        }
        String engine = instance.getEngine();

        /**
         * MySQL SQLServer PostgreSQL PPAS MariaDB
         */
        switch (engine) {
            case "MySQL":
                return MYSQL_DEFAULT_KEYS;
            case "SQLServer":
                return SQL_SERVER_DEFAULT_KEYS;
            case "PostgreSQL":
                return POSTGRE_SQL_DEFAULT_KEYS;
            default:
        }
        return null;
    }

}
