## Be-20240901 项目说明文档

## 🔧 技术栈

### 基础环境

- **Java**：8

- **Spring Boot**：2\.7\.18

### 核心中间件

- **MySQL**：8

- **Kafka**：3\.7\.1

    - 运行模式：KRaft

    - 认证方式：SASL\_PLAINTEXT、PLAIN

- **Redis**：7

- **MinIO**：2025\-09\-07

## 📦 Maven 依赖

### 工具类依赖

- **thumbnailator**：图片缩略图处理

- **oshi\-core**：基于JNA的操作系统、硬件信息采集

- **easyexcel**：Excel文件读写操作

- **jave\-all\-deps**：基于FFmpeg的音视频处理

- **jsch**：SSH、SFTP远程连接操作

- **ip2region**：离线IP地址定位解析

- **hutool\-all**：全能Java工具库

- **jetbrains\-annotations**：JetBrains代码注解工具

### 支付对接依赖

- **wechatpay\-java**：微信支付V3接口对接

- **alipay\-sdk\-java**：支付宝支付接口对接

### 文件存储依赖

- **minio**：MinIO对象文件系统操作

- **aliyun\-sdk\-oss**：阿里云OSS对象存储对接

### 消息邮件依赖

- **javax\.mail**：邮件发送接收功能

### 接口文档依赖

- **springdoc\-openapi\-ui**：OpenAPI接口文档生成

- **knife4j\-springdoc\-ui**：Knife4j美化接口文档

### 数据操作依赖

- **redisson\-spring\-boot\-starter**：Redis分布式操作、锁工具

- **dynamic\-datasource\-spring\-boot\-starter**：多数据源动态切换

- **mybatis\-plus\-boot\-starter**：MyBatis增强ORM框架
