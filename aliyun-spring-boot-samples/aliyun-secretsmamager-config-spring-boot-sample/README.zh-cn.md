# 阿里云 Spring Boot 凭据配置

## 简介

   如果您的应用是 Spring Cloud 应用，且需要使用阿里云的凭据管家服务进行机密配置加载，那么您可以使用凭据管家完成 Spring Cloud 应用的机密配置自动完成应用配置加载。
   
   凭据管理是企业IT系统运维安全的核心诉求之一。[KMS凭据管家](https://help.aliyun.com/document_detail/152001.html?spm=a2c4g.11186623.6.594.20d45999rbodnG)为您提供凭据的创建、检索、更新、删除等全生命周期的管理服务，轻松实现对敏感凭据的统一管理。

## 示例

### 接入凭据管家

在启动示例进行演示之前，我们先了解一下如何接入凭据管家。

1. pom.xml中添加aliyun-secretsmamager-config-spring-boot-starter依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>aliyun-secretsmamager-config-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
``` 
    
2. 添加`/src/main/resources/bootstrap.yml`，可选择以下4种访问鉴权方式

  * 采用阿里云ECS Ram Role作为访问鉴权方式(推荐)  
   ```properties         
aliyun:
   secretsmanager:
      regionId: #your_region_id#
      secretName: #your_secret_name#
      credentialsType: ecs_ram_role
      ecsRoleName: #ecsRoleName#
   ``` 
     
  * 采用阿里云AccessKey作为访问鉴权方式
   ```properties  
aliyun:
   secretsmanager:
      regionId: #your_region_id#
      secretName: #your_secret_name#
      credentialsType: ak
      accessKeyId: #accessKeyId#
      accessKeySecret: #accessKeySecret#
   ``` 
  * 采用阿里云STS作为访问鉴权方式  
   ```properties     
aliyun:
   secretsmanager:
      regionId: #your_region_id#
      secretName: #your_secret_name#
      credentialsType: token
      accessTokenId: #accessTokenId#
      accessToken: #accessToken#
   ``` 
  * 采用阿里云Ram Role作为访问鉴权方式
   ```properties 
aliyun:
   secretsmanager:
      regionId: #your_region_id#
      secretName: #your_secret_name#
      credentialsType: ram_role
      accessKeyId: #accessKeyId#
      accessKeySecret: #accessKeySecret#
      roleSessionName: #roleSessionName#
      roleArn: #roleArn#
      policy: #policy#
   ``` 
        
### 创建凭据 
* 使用 [KMS控制台](https://kms.console.aliyun.com)或者CLI创建凭据(如下所示).

```
aliyun kms CreateSecret --SecretName aliyun_ssm_config_sample --SecretData '{"environment":"prod","categories.types":["prod"],"applicationId":"aliyun-ssm-config-sample"}'
```

### 启动应用
  
在IDE启动应用或者构建一个fatjar.

- 在IDE启动: 找到主类 `AliyunSecretPropertiesApplication`, 执行main方法.
- 构建一个fatjar：
  1. 添加 `maven-assembly-plugin`插件到`pom.xml`
  2. 运行`mvn clean package`构建一个fatjar.
  3. 执行`java -jar ssm-config-sample.jar`启动应用.
	    
### 验证示例

通过浏览器访问指定的地址或者在部署机器上使用`curl` 访问应用.

```
curl http://localhost:8090/api/sample/v1
``` 

HTTP请求的结果如下

```
{"environment":"\"prod\"","types":["\"prod\""],"applicationId":"\"aliyun-ssm-config-sample\""}
```     	    
   