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

import com.alibaba.schedulerx.worker.processor.BizSubTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 并行计算模拟业务对象信息，业务对象需要实现BizSubTask接口
 * @author yaohui
 * @create 2022/3/16 下午6:14
 **/
public class ParallelAccountInfo implements BizSubTask {

    /**
     * 主键
     */
    private long id;

    private String name;

    private String accountId;

    public ParallelAccountInfo(long id, String name, String accountId) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
    }

    /**
     * 实现labelMap方法，用于设置对应子任务的标签信息
     * @return
     */
    @Override
    public Map<String, String> labelMap() {
        Map<String, String> labelMap = new HashMap();
        labelMap.put("户名", name);
        return labelMap;
    }

}