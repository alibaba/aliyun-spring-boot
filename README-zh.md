# 介绍

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

本项目为阿里云服务 Spring Boot Starters 工程，它致力于简化阿里云环境下的 Spring Boot 应用开发。

参考英文文档 [English Document](README.md).




## Starters 组件

- **[Alibaba Cloud OSS](https://www.aliyun.com/product/oss)**: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。

- **[Alibaba Cloud SchedulerX](https://help.aliyun.com/document_detail/43136.html)**: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。

- **[Alibaba Cloud SMS](https://www.aliyun.com/product/sms)**: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

- **[Alibaba Cloud Redis](https://www.aliyun.com/product/kvstore)**: 高可靠双机热备架构及可无缝扩展的集群架构，满足高读写性能场景及容量需弹性变配的业务需求。

- **[Alibaba Cloud RDS MySQL](https://www.aliyun.com/product/rds/mysql)**: 阿里云关系型数据库RDS（Relational Database Service）是一种稳定可靠、可弹性伸缩的在线数据库服务，提供容灾、备份、恢复、迁移等方面的全套解决方案，彻底解决数据库运维的烦恼。




## 如何构建

当前工程使用 Maven 来构建，最快的使用方式是将本项目 clone 到本地，然后执行以下命令：

```shell script
./mvnw install
```

## 如何使用

### 如何引入依赖

如果需要使用已发布的版本，在 `dependencyManagement` 中添加如下配置。

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>aliyun-spring-boot-dependencies</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

然后在 `dependencies` 中添加自己所需使用的依赖即可使用。




## 使用示例

`aliyun-spring-boot-samples` 模块包含了 Aliyun Spring Boot Starters 使用示例，这些示例能够帮助您快速掌握工程特性。

示例列表：

- **[Alibaba Cloud OSS Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample)**

- **[Alibaba Cloud SMS Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-sms-spring-boot-sample)**

- **[Alibaba Cloud Redis Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-redis-spring-boot-sample)**


## Version

当前项目仅支持 Spring Boot 2.0 以上的版本，并且不再对 Spring Boot 1.x 予以支持。


## 社区交流

### 钉钉群

![DingQR](https://img.alicdn.com/tfs/TB1jXikzAL0gK0jSZFtXXXQCXXa-1002-323.png)
