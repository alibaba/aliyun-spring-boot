# Aliyun Spring Boot Compileflow 示例

## 1. 项目说明

`compileflow`是一个非常轻量、高性能、可集成、可扩展的流程引擎。

`compileflow Process`引擎是淘宝工作流`TBBPM`引擎之一，是专注于纯内存执行，无状态的流程引擎，通过将流程文件转换生成`java`代码编译执行，简洁高效。当前是阿里业务中台交易等多个核心系统的流程引擎。

`compileflow`能让开发人员通过流程编辑器设计自己的业务流程，将复杂的业务逻辑可视化，为业务设计人员与开发工程师架起了一座桥梁。

更多相关信息，请参考 [compileflow](https://github.com/alibaba/compileflow)。

## 2. Quick Start

### Step1: 下载安装`IntelliJ IDEA`插件(可选)

插件下载地址：https://github.com/alibaba/compileflow-idea-designer

*安装说明：请使用`IntelliJ IDEA`本地安装方法进行安装，重新启动`IntelliJ IDEA`就会生效。*

### Step2: 引入`POM`文件

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>aliyun-compileflow-spring-boot-starter</artifactId>
</dependency>
```

**注意**: `compileflow`仅支持`JDK 1.8`及以上版本。

### Step3: 流程设计

下面以ktv demo为例，通过demo的演示和实践了解节点及属性的配置和`API`的使用。

demo描述：N个人去ktv唱歌，每人唱首歌，ktv消费原价为30元/人，如果总价超过300打九折，小于300按原价付款。

#### S3.1

创建`bpm`文件，如下图：  
![ktv_demo_s1](https://github.com/alibaba/compileflow/blob/master/doc/image/ktv_demo_s1.png)

*注：`bpm`文件路径要和`code`保持一致，在文件加载模式下流程引擎执行时会根据`code`找到文件。*

#### S3.2

通过插件进行流程设计或者直接编写流程`xml`文件。

#### S3.3 调用流程

编写如下单元测试：

```java
public void testProcessEngine() {
    final String code = "bpm.ktv.ktvExample";

    final Map<String, Object> context = new HashMap<>();
    final List<String> pList = new ArrayList<>();
    pList.add("wuxiang");
    pList.add("xuan");
    pList.add("yusu");
    context.put("pList", pList);

    final ProcessEngine<TbbpmModel> processEngine = ProcessEngineFactory.getProcessEngine();

    final TbbpmModel tbbpmModel = processEngine.load(code);
    final OutputStream outputStream = TbbpmModelConverter.getInstance().convertToStream(tbbpmModel);
    System.out.println(outputStream);
    System.out.println(processEngine.getTestCode(code));

    processEngine.preCompile(code);

    System.out.println(processEngine.start(code, context));
}
```

**_`compileflow`原生只支持淘宝`BPM`规范，为兼容`BPMN 2.0`规范，做了一定适配，但仅支持部分`BPMN 2.0`元素，如需其他元素支持，可在原来基础上扩展。_**

## 3. 更多资料

* [DEMO快速开始](https://github.com/alibaba/compileflow/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8BDEMO)
* [原始淘宝BPM规范详细说明](https://github.com/alibaba/compileflow/wiki/%E5%8D%8F%E8%AE%AE%E8%AF%A6%E8%A7%A3)

## 4. 欢迎加入`compileflow`开发群

1. 请钉钉联系 @余苏 @徐工 @梵度 @哲良 @无相

## Known Users

如果您在使用，请让我们知道，您的使用对我们非常重要。请在下面链接的issue中回复：https://github.com/alibaba/compileflow/issues/9

![](doc/image/known_users/alibaba.png)
![](doc/image/known_users/alipay.png)
![](doc/image/known_users/aliyun.png)
![](doc/image/known_users/taobao.png)
![](doc/image/known_users/tmall.png)
