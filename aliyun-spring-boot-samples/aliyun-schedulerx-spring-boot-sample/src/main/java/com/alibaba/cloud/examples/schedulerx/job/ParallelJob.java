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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.examples.schedulerx.domain.ParallelAccountInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.schedulerx.common.util.StringUtils;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.MapReduceJobProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;

/**
 * 可视化的分布式任务，分布式模型需要选择并行计算
 * 可以看到每个子任务级别的状态、结果和日志
 * 可以给每个子任务定义label，支持通过label搜索，比如通过每个子任务是一个卡号，通过卡号搜索
 * 
 * @author yaohui
 * @create 2021/10/21 下午8:12
 **/
@Component
public class ParallelJob extends MapReduceJobProcessor {

    private static final Logger logger = LoggerFactory.getLogger("schedulerx");

    @Override
    public ProcessResult reduce(JobContext context) throws Exception {
        return new ProcessResult(true);
    }

    @Override
    public ProcessResult process(JobContext context) throws Exception {
        if(isRootTask(context)){
            logger.info("构建并行计算的子任务列表...");
            List<ParallelAccountInfo> list = new LinkedList();
            /**
             *  判断如果是rootTask的情况下，构建并行计算子任务对象列表
             *  在实际业务场景中，用户可自行根据业务场景加载子任务对象且该业务对象实现BizSubTask接口
             *  场景案例：
             *  1、从数据库中加载未被处理的客户账户信息
             *  2、构建省份城市地区信息列表，按区域分发任务处理
             *  3、根据业务标签作为子任务分类，如：电器、日用品、食品等
             *  4、可根据时间作为子任务分类，如：按月（1月、2月...）
             */
            for(int i=0; i < 20; i++){
                list.add(new ParallelAccountInfo(i, "CUS"+StringUtils.leftPad(i+"", 4, "0"),
                        "AC"+StringUtils.leftPad(i+"", 12, "0")));
            }
            return map(list, "transfer");
        }else {
            /**
             * 非rootTask，用户可以获取对应的子任务信息进行相应的业务处理
             */
            ParallelAccountInfo obj = (ParallelAccountInfo)context.getTask();
            // 针对获取的 obj子任务信息，进行业务逻辑处理
            // do something
            logger.info("处理子任务信息:{}", JSON.toJSONString(obj));
            return new ProcessResult(true);
        }
    }


}

