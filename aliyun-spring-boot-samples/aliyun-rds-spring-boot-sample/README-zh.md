# Aliyun Spring Boot RDS 示例

## 项目说明
阿里云关系型数据库RDS（Relational Database Service）是一种稳定可靠、可弹性伸缩的在线数据库服务，提供容灾、备份、恢复、迁移等方面的全套解决方案，彻底解决数。

本示例工程，以RDS Mysql版为例，展示如何配置和使用RDS数据源；

## 准备工作

在运行此示例之前，你需要先完成如下几步准备工作：

1 创建 ORDER_TBL 表
```sql
DROP TABLE IF EXISTS `order_tbl`;
CREATE TABLE `order_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  `money` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

2 初始化数据
```sql
INSERT INTO `order_tbl`(`user_id`, `commodity_code`, `count`, `money`) values('1001', '0001', '10', '20');
INSERT INTO `order_tbl`(`user_id`, `commodity_code`, `count`, `money`) values('1002', '0002', '3', '13');
```

## 配置示例
### 增加rds starter 依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>aliyun-rds-spring-boot-starter</artifactId>
</dependency>
```
### 增加数据库驱动
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.31</version>
</dependency>
```

### 增加连接池依赖
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.17</version>
</dependency>
```
数据库连接池是可选配置，不过在生产环境，为了更好的优化系统性能一般都会使用数据库连接池

### 应用配置
* Aliyun 授权信息配置
```properties
alibaba.cloud.access-key=***
alibaba.cloud.secret-key=***
```
你可以在https://usercenter.console.aliyun.com/#/manage/ak查看自己的access-key&secret-key

* 数据源配置
```properties
spring.datasource.name=defaultDataSource

# 数据库连接地址：
spring.datasource.url=jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC

# 数据库用户名&密码：
spring.datasource.username=****
spring.datasource.password=****
```
数据库访问账号&密码，请在控制台中，对应数据库实例的"账号管理"功能下维护

## 运行示例
启动com.alibaba.cloud.spring.boot.examples.rds.RdsApplication

查询数据：
```
http://127.0.0.1:9090/query/1001
```

更新数据
```
http://127.0.0.1:9090/update/1001/5
```

## 参考
[Spring Boot 文档参考](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
[RDS控制台](https://rdsnext.console.aliyun.com)
[Druid连接池](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE)