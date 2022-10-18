# SchedulerX Example

## 项目说明

[SchedulerX](https://www.aliyun.com/aliware/schedulerx) 是阿里巴巴自研的基于Akka架构的分布式任务调度平台（兼容开源XXL-JOB/ElasticJob），支持Cron定时、一次性任务、任务编排、分布式，具有高可用、可视化、低延时等能力。


## 示例

### 如何接入

1. 登录阿里云分布式任务调度[Schedulerx控制台](https://schedulerx2.console.aliyun.com)，点击开通服务

2. 进入任务调度控制台->应用管理，点击"创建应用"，配置应用分组信息保存

3. pom增加依赖aliyun-schedulerx-spring-boot-starter
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>aliyun-schedulerx-spring-boot-starter</artifactId>
</dependency>
```

4. 应用配置application.properties，可在控制台->应用管理->XXX应用（接入配置）点击获取
```properties
spring.schedulerx2.endpoint=acm.aliyun.com                     #请填写不同region的endpoint
spring.schedulerx2.namespace=aad167f6-8bee-xxxx-xxxx-xxxxxxxxx #region内全局唯一，控制台上创建命名空间时自动生成
spring.schedulerx2.groupId=qianxi.dev                          #应用的id标识，从控制台上创建应用分组时自定义填写，一个命名空间下需要唯一
spring.schedulerx2.appKey=xxxxxxxxxxxxxxxxxxxxxxxx             #应用的key，从控制台上获取
spring.schedulerx2.task.scheduling.scheduler=schedulerx        #接管Spring定时任务

# 可选，开启自动同步Spring注解任务
#spring.schedulerx2.task.scheduling.sync=true
#spring.schedulerx2.task.scheduling.overwrite=false
#spring.schedulerx2.regionId=public
#spring.schedulerx2.aliyunAccessKey=XXXXXXXXX
#spring.schedulerx2.aliyunSecretKey=XXXXXXXXX
```

通过配置文件定义任务并自动同步创建，配置application.yml （可选模式）
```yaml
spring:
   schedulerx2:
      endpoint: acm.aliyun.com                     #请填写不同region的endpoint
      namespace: 433d8b23-xxx-xxx-xxx-90d4d1b9a4af #region内全局唯一，推荐控制台手动创建，如控制台region下不存在且存在任务配置时会自动创建（建议使用UUID生成）
      namespaceName: 测试环境
      namespaceSource: schedulerx
      appName: myTest            #应用名称，如控制台手动创建应用分组则直接复制过来配置即可
      groupId: myTest.group      #应用的id标识，一个命名空间下需要唯一，推荐控制台手动创建，如控制台region下不存在且存在任务配置时会自动创建
      appKey: myTest123@alibaba  #应用的key，不要太简单，注意保管好，如控制台手动创建应用分组则直接复制过来配置即可
      regionId: public           #请填写不同的regionId，用于自动同步下方的任务信息至指定的region
      aliyunAccessKey: xxxxxxxxx
      aliyunSecretKey: xxxxxxxxx
      jobs:
         simpleJob: 
            jobModel: standalone
            className: com.alibaba.cloud.examples.schedulerx.job.processor.SimpleJob
            cron: 0/30 * * * * ?   # cron表达式
            jobParameter: hello
            overwrite: true 
         shardingJob: 
            jobModel: sharding
            className: com.alibaba.cloud.examples.schedulerx.job.processor.ShardingJob
            oneTime: 2022-06-02 12:00:00   # 一次性任务表达式
            jobParameter: 0=Beijing,1=Shanghai,2=Guangzhou
            overwrite: true
         broadcastJob:   # 不填写cron和oneTime，表示api任务
            jobModel: broadcast
            className: com.alibaba.cloud.examples.schedulerx.job.processor.BroadcastJob
            jobParameter: hello
            overwrite: true
         mapReduceJob: 
            jobModel: mapreduce
            className: com.alibaba.cloud.examples.schedulerx.job.processor.MapReduceJob
            cron: 0 * * * * ?
            jobParameter: 100
            overwrite: true
```
	
5. 实现任务接口，以单机任务为例，更多任务模型请看examples
```java
package com.alibaba.cloud.examples.schedulerx.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class SimpleJob extends JavaProcessor {

	private static final Logger logger = LoggerFactory.getLogger("schedulerx");

	@Override
	public ProcessResult process(JobContext context) throws Exception {
		System.out.println("this is process, para=" + context.getJobParameters());
		logger.info("hello schedulerx!");
		return new ProcessResult(true);
	}

	@Override
	public void kill(JobContext context) {
	}
}
```
支持Spring原生定时任务调度
```java
@Component
public class SpringJob {

    /**
     * log4j2/logback配置schedulerxLogAppender，可以进行日志采集
     */
    private static final Logger logger = LoggerFactory.getLogger("schedulerx");

    /**
     * An spring scheduled job
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void simpleJob() {
        logger.info("hello schedulerx! this's an spring simple job.");
    }

    /**
     * An spring scheduled job
     * Can use @SchedulerX annotation set job config when open sync(true)
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @SchedulerX(name = "simpleJobWithAnnotation", model = ExecuteMode.STANDALONE)
    public void simpleJobWithAnnotation() {
        logger.info("hello schedulerx! this's an spring simple job with SchedulerX annotation.");
    }

}
```

6. 启动你的springboot启动类，大概1分钟左右，你的定时任务就可以正常调度起来了
```
hello schedulerx!
hello schedulerx!
hello schedulerx!
```
