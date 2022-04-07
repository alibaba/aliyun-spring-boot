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

package com.alibaba.cloud.examples.schedulerx.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.cloud.examples.schedulerx.domain.PageTask;
import com.alibaba.cloud.examples.schedulerx.domain.Record;
import com.alibaba.schedulerx.common.domain.Pair;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.MapJobProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.google.common.collect.Lists;

/**
* 通过Map模型扫描分库分表
* 
* @author xiaomeng.hxm
*/
@Component
public class ScanShardingTableJob extends MapJobProcessor {

    private final int PAGE_SIZE = 500;

    @Override
    public ProcessResult process(JobContext context) throws Exception {
        String taskName = context.getTaskName();
        Object task = context.getTask();
        if (isRootTask(context)) {
            //先分库
            List<String> dbList = getDbList();
            return map(dbList, "DbTask");
        } else if (taskName.equals("DbTask")) {
            //根据分库去分表
            String dbName = (String)task;
            List<String> tableList = getTableList(dbName);
            return map(tableList, "TableTask");
        } else if (taskName.equals("TableTask")) {
            //如果一个分表也很大，再分页
            String tableName = (String)task;
            Pair<Long, Long> idPair = queryMinAndMaxId(tableName);
            long minId = idPair.getFirst();
            long maxId = idPair.getSecond();
            List<PageTask> tasks = Lists.newArrayList();
            int step = (int) ((maxId - minId) / PAGE_SIZE); //计算分页数量
            for (long i = minId; i < maxId; i+=PAGE_SIZE) {
                long startId = i;
                long endId = (i+PAGE_SIZE > maxId ? maxId : i+PAGE_SIZE);
                tasks.add(new PageTask(tableName, startId, endId));
            }
            return map(tasks, "PageTask");
        } else if (taskName.equals("PageTask")) {
            PageTask pageTask = (PageTask)task;
            String tableName = pageTask.getTableName();
            long startId = pageTask.getStartId();
            long endId = pageTask.getEndId();
            List<Record> records = queryRecord(tableName, startId, endId);
            //TODO handle records
            return new ProcessResult(true);
        }
 
        return new ProcessResult(false);
    }

    private List<String> getDbList() {
        List<String> dbList = Lists.newArrayList();
        //TODO 返回分库列表
        return dbList;
    }

    private List<String> getTableList(String dbName) {
        List<String> tableList = Lists.newArrayList();
        //TODO 返回分表列表
        return tableList;
    }

    private Pair<Long, Long> queryMinAndMaxId(String tableName) {
        //TODO select min(id),max(id) from [tableName]
        return new Pair<Long, Long>(1L, 10000L);
    }

    private List<Record> queryRecord(String tableName, long startId, long endId) {
        List<Record> records = Lists.newArrayList();
        //TODO select * from [tableName] where id>=[startId] and id<[endId]
        return records;
    }
}