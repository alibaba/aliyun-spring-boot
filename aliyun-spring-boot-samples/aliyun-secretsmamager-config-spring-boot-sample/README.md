# Aliyun Spring Boot Properties Sample with Secrets Manager

## Introduction
   If your applications are Spring Boot applications and you need to use application properties with Alibaba Cloud Secrets Manager service for secret properties. This topic provides an example to illustrate how to use Secrets Manager Configuration starter to implement secrets properties for Spring Boot applications.
   
   Secret management is one of the core demands of enterprises on IT system O&M security. [Secrets Manager](https://www.alibabacloud.com/help/doc-detail/152001.htm?spm=a2c63.l28256.a3.32.23f834e49maMsD) enables you to manage your secrets in a centralized manner throughout their lifecycle, such as creation, retrieval, updating, and deletion of your secrets.
   

## Demo

### Connect to Secrets Manager

Before we start the demo, let's learn how to connect Secrets Manager to a Spring Boot application. 

1. Add dependency aliyun-secretsmamager-config-spring-boot-starter in the pom.xml file in your Spring Boot project.

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>aliyun-secretsmamager-config-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
``` 

2. Add file `/src/main/resources/bootstrap.yml` to access Secrets Manager with any of the following four Alibaba Cloud credentials

  * Use Alibaba Cloud ECS Ram Role credentials(recommended) 
   
   ```properties         
    aliyun:
       secretsmanager:
          regionId: #your_region_id#
          secretName: #your_secret_name#
          credentialsType: ecs_ram_role
          ecsRoleName: #ecsRoleName#
   ``` 
     
  * Use Alibaba Cloud Access Key credentials
  
   ```properties  
    aliyun:
       secretsmanager:
          regionId: #your_region_id#
          secretName: #your_secret_name#
          credentialsType: ak
          accessKeyId: #accessKeyId#
          accessKeySecret: #accessKeySecret#
   ``` 
   
  * Use Alibaba Cloud [STS](https://www.alibabacloud.com/help/doc-detail/28756.html) credentials
  
   ```properties     
    aliyun:
       secretsmanager:
          regionId: #your_region_id#
          secretName: #your_secret_name#
          credentialsType: token
          accessTokenId: #accessTokenId#
          accessToken: #accessToken#
   ``` 
   
  * Use Alibaba Cloud [RAM Role](https://www.alibabacloud.com/help/doc-detail/93689.htm) credentials
  
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
        
### Create Alibaba Cloud Secrets

* Use [KMS Console](https://kms.console.aliyun.com) or CLI to create Alibaba Cloud Secrets for your spring boot application(as the follow).

```
aliyun kms CreateSecret --SecretName aliyun_ssm_config_sample --SecretData '{"environment":"prod","categories.types":["prod"],"applicationId":"aliyun-ssm-config-sample"}'
```
 
### Start Application
  
Start the application in IDE or by building a fatjar.

- Start in IDE: Find main class `AliyunSecretPropertiesApplication`, and execute the main method.
- Build a fatjar：
  1. Add `maven-assembly-plugin` plugin into file `pom.xml`
  2. Execute command `mvn clean package` to build a fatjar.
  3. Run command `java -jar ssm-config-sample.jar` to start the application.
	    
### Verify the sample

Use browser to access the application with the given website or use `curl` to access the application on the deployment machine.

```
curl http://localhost:8090/api/sample/v1
``` 

The response of the http request is as the follow

```
{"environment":"\"prod\"","types":["\"prod\""],"applicationId":"\"aliyun-ssm-config-sample\""}
```     	    
	    

   