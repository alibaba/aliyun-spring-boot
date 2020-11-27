# Introduction

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

This project is for Spring Boot Starters of Alibaba Cloud services, which makes it easy to create the Spring Boot 
Application in Alibaba Cloud services.

See the [中文文档](README-zh.md) for Chinese readme.


## How to Use

### Add maven dependency 

These artifacts are available from Maven Central via BOM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>aliyun-spring-boot-dependencies</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

add the module in  `dependencies`.

If you'd like to use the SNAPSHOT artifacts, please add the following `<repository>` into `pom.xml`:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <name>Sonatype Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>
</repositories>
```



## Components

- **[Alibaba Cloud OSS](https://www.aliyun.com/product/oss)**: An encrypted and secure cloud storage service which stores, processes and accesses massive amounts of data from anywhere in the world.
    - Starter - [aliyun-oss-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-oss-spring-boot-starter)
    - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-oss-spring-boot-sample)

- **[Alibaba Cloud SMS](https://www.aliyun.com/product/sms)**: A messaging service that covers the globe, Alibaba SMS provides convenient, efficient, and intelligent communication capabilities that help businesses quickly contact their customers.
    - Starter - [aliyun-sms-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-sms-spring-boot-starter)
    - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-sms-spring-boot-sample)

- **[Alibaba Cloud SchedulerX](https://www.aliyun.com/product/SchedulerX)**: accurate, highly reliable, and highly available scheduled job scheduling services with response time within seconds.
    - Starter - [aliyun-schedulerx-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-schedulerx-spring-boot-starter)
    - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-schedulerx-spring-boot-sample)

- **[Alibaba Cloud Redis](https://www.aliyun.com/product/kvstore)**: A key value database service that offers in-memory caching and high-speed access to applications hosted on the cloud.
   - Starter - [aliyun-redis-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-redis-spring-boot-starter)
   - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-redis-spring-boot-sample)

- **[Alibaba Cloud RDS MySQL](https://www.aliyun.com/product/rds/mysql)**: A fully hosted online database service that supports MySQL 5.5, 5.6, 5.7, and 8.0.
   - Starter - [aliyun-rds-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-rds-spring-boot-starter)
   - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-rds-spring-boot-sample)
   
- **[Alibaba Cloud Compileflow](https://github.com/alibaba/compileflow)**: Core business process engine of Alibaba Halo platform. Best engine for trade Scenes.
   - Starter - [aliyun-compileflow-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-compileflow-spring-boot-starter)
   - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-compileflow-spring-boot-sample)   
   
- **[Alibaba Cloud Function Compute](https://www.aliyun.com/product/fc)**: Core business process engine of Alibaba Halo platform. Best engine for trade Scenes.
   - Starter - [aliyun-fc-spring-boot-starter](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-starters/aliyun-fc-spring-boot-starter)
   - [Sample](https://github.com/alibaba/aliyun-spring-boot/tree/master/aliyun-spring-boot-samples/aliyun-fc-spring-boot-sample)   


## Version

Current project only supports Spring Boot 2 and above, does not support Spring Boot 1.x at all.



## How to build

Spring Boot uses Maven for most build-related activities, and you should be able to get off the ground quite quickly by cloning the project you are interested in and typing:

```shell script
./mvnw install
```



## Contact Us

DingDing Chat group is recommended for discussing almost anything related to `aliyun-spring-boot`. 

![DingQR](https://img.alicdn.com/tfs/TB1jXikzAL0gK0jSZFtXXXQCXXa-1002-323.png)
