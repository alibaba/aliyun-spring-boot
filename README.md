# Introduction

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

This project is for Spring Boot Starters of Alibaba Cloud services, which makes it easy to create the Spring Boot 
Application in Alibaba Cloud services.

See the [中文文档](README-zh.md) for Chinese readme.




## Starters

- **[Alibaba Cloud OSS](https://www.aliyun.com/product/oss)**: An encrypted and secure cloud storage service which stores, processes and accesses massive amounts of data from anywhere in the world.

- **[Alibaba Cloud SMS](https://www.aliyun.com/product/sms)**: A messaging service that covers the globe, Alibaba SMS provides convenient, efficient, and intelligent communication capabilities that help businesses quickly contact their customers.

- **[Alibaba Cloud SchedulerX](https://www.aliyun.com/product/SchedulerX)**: accurate, highly reliable, and highly available scheduled job scheduling services with response time within seconds.

- **[Alibaba Cloud Redis](https://www.aliyun.com/product/kvstore)**: A key value database service that offers in-memory caching and high-speed access to applications hosted on the cloud.

- **[Alibaba Cloud RDS MySQL](https://www.aliyun.com/product/rds/mysql)**: A fully hosted online database service that supports MySQL 5.5, 5.6, 5.7, and 8.0.




## How to build

Spring Boot uses Maven for most build-related activities, and you should be able to get off the ground quite quickly by cloning the project you are interested in and typing:

```shell script
./mvnw install
```

## How to Use

### Add maven dependency 

These artifacts are available from Maven Central via BOM:

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

add the module in  `dependencies`.


## Samples

A `aliyun-spring-boot-samples` module is included in our project for you to get started with Aliyun Spring Boot quickly. 
It contains an example, and you can refer to the readme file in the samples project for a quick walkthrough.

Samples：

- **[Alibaba Cloud OSS Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample)**
- **[Alibaba Cloud SMS Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-sms-spring-boot-sample)**
- **[Alibaba Cloud Redis Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-redis-spring-boot-sample)**


## Version

Current project only supports Spring Boot 2 and above, does not support Spring Boot 1.x at all.


## Contact Us
Mailing list is recommended for discussing almost anything related to `aliyun-spring-boot`. 

aliyun-spring-boot@googlegroups.com:You can ask questions here if you encounter any problem when using or developing aliyun-spring-boot.
