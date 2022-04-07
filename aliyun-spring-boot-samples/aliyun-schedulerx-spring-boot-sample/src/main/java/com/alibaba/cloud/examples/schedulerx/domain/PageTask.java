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

package com.alibaba.cloud.examples.schedulerx.domain;

/**
*
* @author xiaomeng.hxm
*/
public class PageTask {
    private String tableName;
    private long startId;
    private long endId;

    public PageTask(String tableName, long startId, long endId) {
        this.tableName = tableName;
        this.startId = startId;
        this.endId = endId;
    }

    public String getTableName() {
        return tableName;
    }

    public long getStartId() {
        return startId;
    }

    public long getEndId() {
        return endId;
    }
}
