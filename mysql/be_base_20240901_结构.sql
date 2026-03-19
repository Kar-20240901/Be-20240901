-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: karopendevlocal.top    Database: be_base_20240901
-- ------------------------------------------------------
-- Server version	8.4.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `base_activiti_process_instance`
--

DROP TABLE IF EXISTS `base_activiti_process_instance`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_activiti_process_instance`
(
    `id`                        bigint                                                    NOT NULL,
    `process_instance_id`       varchar(64)                                               NOT NULL COMMENT '流程实例 id',
    `process_instance_json_str` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程实例全局参数：BaseActivitiParamBO对象',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：流程实例全局参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_api_token`
--

DROP TABLE IF EXISTS `base_api_token`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_api_token`
(
    `id`            bigint                                                        NOT NULL,
    `user_id`       bigint                                                        NOT NULL COMMENT '用户 id',
    `create_time`   datetime                                                      NOT NULL,
    `update_time`   datetime                                                      NOT NULL,
    `token`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '调用 api时，传递的 token，格式：uuid，在 header头或者url里传递：BeApiToken',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '令牌名',
    `last_use_time` datetime                                                      NOT NULL COMMENT '最近使用时间',
    `enable_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `base_api_token_token_IDX` (`token`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：令牌表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_area`
--

DROP TABLE IF EXISTS `base_area`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_area`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '区域名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_area_ref_user`
--

DROP TABLE IF EXISTS `base_area_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_area_ref_user`
(
    `area_id` bigint NOT NULL COMMENT '区域主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`area_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：区域表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_auth`
--

DROP TABLE IF EXISTS `base_auth`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_auth`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300)                                                  NOT NULL,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名（不能重复）',
    `auth`        varchar(150)                                                  NOT NULL COMMENT '权限，例子：base:menu:edit',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_bulletin`
--

DROP TABLE IF EXISTS `base_bulletin`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_bulletin`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `update_id`    bigint                                                        NOT NULL,
    `update_time`  datetime                                                      NOT NULL,
    `enable_flag`  tinyint(1)                                                    NOT NULL COMMENT '启用/禁用',
    `remark`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述/备注',
    `content`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '公告内容（富文本）',
    `title`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '标题',
    `publish_time` datetime                                                      NOT NULL COMMENT '发布时间',
    `status`       tinyint                                                       NOT NULL COMMENT '公告状态：1 草稿 2 公示',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_bulletin_read_time_ref_user`
--

DROP TABLE IF EXISTS `base_bulletin_read_time_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_bulletin_read_time_ref_user`
(
    `user_id`            bigint   NOT NULL COMMENT '用户主键id',
    `bulletin_read_time` datetime NOT NULL COMMENT '用户最近查看公告的时间，目的：统计公告数量时，根据这个时间和公告发布时间来计算',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：关联表：公告表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_dept`
--

DROP TABLE IF EXISTS `base_dept`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_dept`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_dept_ref_user`
--

DROP TABLE IF EXISTS `base_dept_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_dept_ref_user`
(
    `dept_id` bigint NOT NULL COMMENT '部门主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`dept_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：部门表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_dict`
--

DROP TABLE IF EXISTS `base_dict`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_dict`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `dict_key`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '字典 key（不能重复），字典项要冗余这个 key，目的：方便操作',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '字典/字典项 名',
    `type`        int                                                           NOT NULL COMMENT '字典类型：1 字典 2 字典项',
    `value`       int                                                           NOT NULL COMMENT '字典项 value（数字 123...）备注：字典为 -1',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该参数的 uuid',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_email_configuration`
--

DROP TABLE IF EXISTS `base_email_configuration`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_email_configuration`
(
    `id`          bigint                                                        NOT NULL,
    `content_pre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正文前缀',
    `port`        int                                                           NOT NULL COMMENT '端口',
    `from_email`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送人邮箱',
    `pass`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送人密码',
    `ssl_flag`    tinyint(1)                                                    NOT NULL COMMENT '是否使用：SSL',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：邮箱配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_file`
--

DROP TABLE IF EXISTS `base_file`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_file`
(
    `id`                       bigint                                                        NOT NULL,
    `create_id`                bigint                                                        NOT NULL,
    `create_time`              datetime                                                      NOT NULL,
    `update_id`                bigint                                                        NOT NULL,
    `update_time`              datetime                                                      NOT NULL,
    `enable_flag`              tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `belong_id`                bigint                                                        NOT NULL COMMENT '归属者用户主键 id（拥有全部权限）',
    `bucket_name`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '桶名，例如：be-bucket',
    `uri`                      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx',
    `origin_file_name`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件原始名（包含文件类型）',
    `new_file_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '新的文件名（包含文件类型），例如：uuid.xxx',
    `file_ext_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '文件类型（不含点），备注：这个是读取文件流的头部信息获得文件类型',
    `extra_json`               varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '额外信息（json格式）',
    `upload_type`              int                                                           NOT NULL COMMENT '文件上传类型：101 头像 201 文件系统-文件',
    `storage_configuration_id` bigint                                                        NOT NULL COMMENT '存储文件配置主键 id',
    `storage_type`             int                                                           NOT NULL COMMENT '存放文件的服务器类型：101 阿里云oss 201 minio ',
    `pid`                      bigint                                                        NOT NULL COMMENT '上级文件夹的文件主键 id，默认为 0',
    `type`                     int                                                           NOT NULL COMMENT '类型：1 文件夹 2 文件',
    `show_file_name`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '展示用的文件名，默认为：原始文件名（包含文件类型）',
    `public_flag`              tinyint(1)                                                    NOT NULL COMMENT '是否公开访问：0 否 1 是',
    `file_size`                bigint                                                        NOT NULL COMMENT '文件大小',
    `ref_id`                   bigint                                                        NOT NULL COMMENT '关联 id',
    `pid_path_str`             longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '父id组合，例如：|0||1||2|，备注：不包含本级，但是包含顶级：0',
    `upload_flag`              tinyint                                                       NOT NULL COMMENT '是否还在上传中，目的：无法操作',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `belong_id` (`belong_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_file_auth`
--

DROP TABLE IF EXISTS `base_file_auth`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_file_auth`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `file_id`     bigint                                                        NOT NULL COMMENT '文件主键 id',
    `user_id`     bigint                                                        NOT NULL COMMENT '此权限拥有者的 userId',
    `read_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否可读：0 否 1 是',
    `write_flag`  tinyint(1)                                                    NOT NULL COMMENT '是否可写：0 否 1 是',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `file_id_and_user_id` (`file_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：文件操作权限表，主表：文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_file_storage_configuration`
--

DROP TABLE IF EXISTS `base_file_storage_configuration`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_file_storage_configuration`
(
    `id`                       bigint                                                        NOT NULL,
    `create_id`                bigint                                                        NOT NULL,
    `create_time`              datetime                                                      NOT NULL,
    `update_id`                bigint                                                        NOT NULL,
    `update_time`              datetime                                                      NOT NULL,
    `enable_flag`              tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件存储配置名',
    `type`                     int                                                           NOT NULL COMMENT '文件存储类型',
    `access_key`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '钥匙',
    `secret_key`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '秘钥',
    `upload_endpoint`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上传的端点',
    `public_download_endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公开下载的端点',
    `bucket_public_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公开类型的桶名',
    `bucket_private_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '私有类型的桶名',
    `default_flag`             tinyint(1)                                                    NOT NULL COMMENT '是否默认存储',
    `custom_domain`            varchar(255)                                                  NOT NULL COMMENT '自定义域名',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：文件存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_file_transfer`
--

DROP TABLE IF EXISTS `base_file_transfer`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_file_transfer`
(
    `id`                       bigint                                                        NOT NULL,
    `create_id`                bigint                                                        NOT NULL,
    `create_time`              datetime                                                      NOT NULL,
    `update_id`                bigint                                                        NOT NULL,
    `update_time`              datetime                                                      NOT NULL,
    `enable_flag`              tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`                  bigint                                                        NOT NULL COMMENT '用户主键 id',
    `file_id`                  bigint                                                        NOT NULL COMMENT '文件主键 id',
    `type`                     int                                                           NOT NULL COMMENT '类型：101 上传 201 下载',
    `new_file_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段：新的文件名（包含文件类型），例如：uuid.xxx',
    `show_file_name`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：展示用的文件名，默认为：原始文件名（包含文件类型）',
    `file_size`                bigint                                                        NOT NULL COMMENT '冗余字段：文件大小',
    `status`                   int                                                           NOT NULL COMMENT '状态：101 传输中 201 传输暂停 301 传输完成 401 传输取消 501 合并中 601 合并完成',
    `file_sign`                longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '文件签名，用于校验文件是否完整，一般采用 md5的方式',
    `chunk_total`              int                                                           NOT NULL COMMENT '总分片个数',
    `chunk_size`               bigint                                                        NOT NULL COMMENT '每个分片的大小',
    `bucket_name`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段：桶名，例如：be-bucket',
    `storage_configuration_id` bigint                                                        NOT NULL COMMENT '冗余字段：存储文件配置主键 id',
    `storage_type`             int                                                           NOT NULL COMMENT '冗余字段：存放文件的服务器类型：101 阿里云oss 201 minio',
    `uri`                      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：文件传输表，主表：文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_file_transfer_chunk`
--

DROP TABLE IF EXISTS `base_file_transfer_chunk`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_file_transfer_chunk`
(
    `id`              bigint                                                        NOT NULL,
    `create_id`       bigint                                                        NOT NULL,
    `create_time`     datetime                                                      NOT NULL,
    `update_id`       bigint                                                        NOT NULL,
    `update_time`     datetime                                                      NOT NULL,
    `enable_flag`     tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`          varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `transfer_id`     bigint                                                        NOT NULL COMMENT '文件传输主键 id',
    `user_id`         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `file_id`         bigint                                                        NOT NULL COMMENT '文件主键 id',
    `chunk_begin_num` bigint                                                        NOT NULL COMMENT '分片开始位置（包含），从 0开始',
    `chunk_end_num`   bigint                                                        NOT NULL COMMENT '分片结束位置（包含）',
    `chunk_size`      bigint                                                        NOT NULL COMMENT '分片大小，计算方式：结束位置 - 开始位置 + 1',
    `chunk_num`       int                                                           NOT NULL COMMENT '分片序号，从 1开始',
    `current_size`    bigint                                                        NOT NULL COMMENT '已经传输的分片大小',
    `uri`             varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx',
    `show_file_name`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '展示用的文件名，默认为：原始文件名（包含文件类型）',
    `ref_data`        varchar(300)                                                  NOT NULL COMMENT '关联的数据',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：文件传输分片表，主表：文件传输表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_apply_friend`
--

DROP TABLE IF EXISTS `base_im_apply_friend`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_apply_friend`
(
    `id`             bigint                                                        NOT NULL,
    `user_id`        bigint                                                        NOT NULL COMMENT '用户主键 id',
    `target_user_id` bigint                                                        NOT NULL COMMENT '申请目标用户的主键 id',
    `status`         int                                                           NOT NULL COMMENT '状态：101 申请中 201 已通过 301 已拒绝',
    `reject_reason`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拒绝理由',
    `apply_time`     datetime                                                      NOT NULL COMMENT '申请时间',
    `apply_content`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请内容',
    `session_id`     bigint                                                        NOT NULL COMMENT '会话主键 id，未通过时为：-1，通过了则赋值，并且后续不变',
    `update_time`    datetime                                                      NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：好友申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_apply_friend_extra`
--

DROP TABLE IF EXISTS `base_im_apply_friend_extra`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_apply_friend_extra`
(
    `apply_friend_id` bigint     NOT NULL COMMENT '好友申请主键 id',
    `user_id`         bigint     NOT NULL COMMENT '用户主键 id',
    `hidden_flag`     tinyint(1) NOT NULL COMMENT '是否隐藏',
    PRIMARY KEY (`apply_friend_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：好友申请扩展表，主表：好友申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_apply_group`
--

DROP TABLE IF EXISTS `base_im_apply_group`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_apply_group`
(
    `id`              bigint                                                        NOT NULL,
    `user_id`         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `target_group_id` bigint                                                        NOT NULL COMMENT '申请目标群组的主键 id',
    `status`          int                                                           NOT NULL COMMENT '状态：101 申请中 201 已通过 301 已拒绝 401 已取消',
    `reject_reason`   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拒绝理由',
    `apply_time`      datetime                                                      NOT NULL COMMENT '申请时间',
    `apply_content`   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请内容',
    `session_id`      bigint                                                        NOT NULL COMMENT '会话主键 id，未通过时为：-1，通过了则赋值，并且后续不变',
    `update_time`     datetime                                                      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：群组申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_apply_group_extra`
--

DROP TABLE IF EXISTS `base_im_apply_group_extra`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_apply_group_extra`
(
    `apply_group_id`   bigint     NOT NULL COMMENT '群组申请主键 id',
    `user_or_group_id` bigint     NOT NULL COMMENT '用户主键 id或者群组主键 id',
    `hidden_flag`      tinyint(1) NOT NULL COMMENT '是否隐藏',
    `type`             int        NOT NULL COMMENT '101 用户 201 群组',
    PRIMARY KEY (`apply_group_id`, `user_or_group_id`, `type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：群组申请扩展表，主表：群组申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_block`
--

DROP TABLE IF EXISTS `base_im_block`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_block`
(
    `create_id`   bigint   NOT NULL,
    `create_time` datetime NOT NULL,
    `user_id`     bigint   NOT NULL COMMENT '目标用户主键 id',
    `source_id`   bigint   NOT NULL COMMENT '拉黑该用户的来源 id，大多数情况下等于 createId，群聊的时候为：groupId',
    `source_type` int      NOT NULL COMMENT '拉黑该用户来源的类型：101 好友 201 群组',
    PRIMARY KEY (`user_id`, `source_id`, `source_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：拉黑表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_friend`
--

DROP TABLE IF EXISTS `base_im_friend`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_friend`
(
    `id`          bigint   NOT NULL,
    `create_time` datetime NOT NULL,
    `belong_id`   bigint   NOT NULL COMMENT '归属者主键 id',
    `friend_id`   bigint   NOT NULL COMMENT '好友用户主键 id',
    `session_id`  bigint   NOT NULL COMMENT '会话主键 id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：好友表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_group`
--

DROP TABLE IF EXISTS `base_im_group`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_group`
(
    `id`               bigint                                                        NOT NULL,
    `create_id`        bigint                                                        NOT NULL,
    `create_time`      datetime                                                      NOT NULL,
    `update_id`        bigint                                                        NOT NULL,
    `update_time`      datetime                                                      NOT NULL,
    `belong_id`        bigint                                                        NOT NULL COMMENT '归属者主键 id',
    `name`             varchar(50)                                                   NOT NULL COMMENT '群组名称',
    `avatar_file_id`   bigint                                                        NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `session_id`       bigint                                                        NOT NULL COMMENT '会话主键 id',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该群组的 uuid，备注：不能重复',
    `normal_mute_flag` tinyint(1)                                                    NOT NULL COMMENT '普通成员是否禁言',
    `manage_mute_flag` tinyint(1)                                                    NOT NULL COMMENT '管理员是否禁言，群主不会被禁言',
    `bio`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '群组简介',
    PRIMARY KEY (`id`),
    UNIQUE KEY `base_im_group_show_id_IDX` (`uuid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：群聊表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_group_ref_user`
--

DROP TABLE IF EXISTS `base_im_group_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_group_ref_user`
(
    `id`          bigint                                                       NOT NULL,
    `create_id`   bigint                                                       NOT NULL,
    `create_time` datetime                                                     NOT NULL,
    `update_id`   bigint                                                       NOT NULL,
    `update_time` datetime                                                     NOT NULL,
    `group_id`    bigint                                                       NOT NULL COMMENT '群组主键 id',
    `user_id`     bigint                                                       NOT NULL COMMENT '用户主键 id',
    `my_nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '我在群组的昵称',
    `mute_flag`   tinyint(1)                                                   NOT NULL COMMENT '是否禁言',
    `manage_flag` tinyint                                                      NOT NULL COMMENT '是否管理员：可以：审批入群，修改群名称，群头像，踢出群员，禁言群员，不能：赋权另外一个管理员，群组创建人不用该字段，并且该字段为 false',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：群组表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_search_history`
--

DROP TABLE IF EXISTS `base_im_search_history`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_search_history`
(
    `id`             bigint       NOT NULL,
    `create_time`    datetime     NOT NULL,
    `belong_id`      bigint       NOT NULL COMMENT '归属者主键 id',
    `search_history` varchar(255) NOT NULL COMMENT '搜索的内容',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：搜索历史表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_session`
--

DROP TABLE IF EXISTS `base_im_session`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_session`
(
    `id`                bigint NOT NULL COMMENT '会话主键 id',
    `source_apply_id`   bigint NOT NULL COMMENT '来源申请 id，目的：删除好友/群组之后，还可以恢复之前的会话内容，备注：群组不支持会话内容恢复，所以群组该值为 -1',
    `source_apply_type` int    NOT NULL COMMENT '来源申请类型：101 好友 201 群组',
    `last_receive_ts`   bigint NOT NULL COMMENT '最后一次接受到消息时的时间戳，默认为：当前时间，备注：该字段用于：排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_session_content`
--

DROP TABLE IF EXISTS `base_im_session_content`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_session_content`
(
    `id`          bigint                                                         NOT NULL,
    `create_id`   bigint                                                         NOT NULL,
    `create_time` datetime                                                       NOT NULL,
    `enable_flag` tinyint(1)                                                     NOT NULL COMMENT '是否启用，用于撤回功能',
    `session_id`  bigint                                                         NOT NULL COMMENT '会话主键 id',
    `content`     varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话内容',
    `type`        int                                                            NOT NULL COMMENT '内容类型',
    `create_ts`   bigint                                                         NOT NULL COMMENT '创建时间的时间戳，UTC+8',
    `ref_id`      bigint                                                         NOT NULL COMMENT '引用的内容主键 id，不引用时为 -1',
    `order_no`    int                                                            NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `create_ts` (`create_ts`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：会话内容表，主表：会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_session_content_ref_user`
--

DROP TABLE IF EXISTS `base_im_session_content_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_session_content_ref_user`
(
    `id`         bigint  NOT NULL,
    `session_id` bigint  NOT NULL COMMENT '会话主键 id',
    `content_id` bigint  NOT NULL COMMENT '会话内容主键 id',
    `user_id`    bigint  NOT NULL COMMENT '用户主键 id',
    `show_flag`  tinyint NOT NULL COMMENT '是否显示在：用户消息中',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：会话用户内容表，主表：会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_im_session_ref_user`
--

DROP TABLE IF EXISTS `base_im_session_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_session_ref_user`
(
    `id`               bigint                                                       NOT NULL,
    `session_id`       bigint                                                       NOT NULL COMMENT '会话主键 id',
    `user_id`          bigint                                                       NOT NULL COMMENT '用户主键 id',
    `last_open_ts`     bigint                                                       NOT NULL COMMENT '我最后一次打开该会话的时间戳',
    `show_flag`        tinyint(1)                                                   NOT NULL COMMENT '是否显示',
    `name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '我对会话显示的自定义名，默认为空',
    `avatar_url`       varchar(200)                                                 NOT NULL COMMENT '冗余字段：头像 url',
    `target_id`        bigint                                                       NOT NULL COMMENT '来源 id，可以查询到好友用户主键 id，或者群组主键 id',
    `target_type`      int                                                          NOT NULL COMMENT '来源申请类型：101 好友 201 群组',
    `target_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '来源昵称',
    `not_disturb_flag` tinyint(1)                                                   NOT NULL COMMENT '是否免打扰',
    PRIMARY KEY (`id`),
    KEY `base_im_session_ref_user_user_id_IDX` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：会话关联用户表，主表：会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_live_room`
--

DROP TABLE IF EXISTS `base_live_room`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_live_room`
(
    `id`          bigint      NOT NULL COMMENT '房间主键 id',
    `belong_id`   bigint      NOT NULL COMMENT '归属用户主键 id',
    `name`        varchar(50) NOT NULL COMMENT '实时房间名',
    `create_time` datetime    NOT NULL COMMENT '创建时间',
    `code`        varchar(30) NOT NULL COMMENT '房间验证码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：实时房间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_live_room_data`
--

DROP TABLE IF EXISTS `base_live_room_data`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_live_room_data`
(
    `id`              bigint       NOT NULL COMMENT '主键 id',
    `room_id`         bigint       NOT NULL COMMENT '实时房间主键 id',
    `data`            longblob     NOT NULL COMMENT '数据',
    `create_time`     datetime     NOT NULL COMMENT '创建时间',
    `create_ts`       bigint       NOT NULL COMMENT '创建时间的时间戳，UTC+8',
    `create_id`       bigint       NOT NULL COMMENT '创建者用户主键 id',
    `media_type`      varchar(200) NOT NULL COMMENT '媒体类型',
    `first_blob_flag` tinyint(1)   NOT NULL COMMENT '是否是第一个 blob',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：实时房间数据表：实时房间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_live_room_replay`
--

DROP TABLE IF EXISTS `base_live_room_replay`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_live_room_replay`
(
    `id`          bigint   NOT NULL COMMENT '主键 id',
    `room_id`     bigint   NOT NULL COMMENT '实时房间主键 id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `file_id`     bigint   NOT NULL COMMENT '关联的文件主键 id',
    `ms`          int      NOT NULL COMMENT '时间，单位：毫秒',
    `belong_id`   bigint   NOT NULL COMMENT '归属者用户主键 id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：实时房间回放表：实时房间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_live_room_user`
--

DROP TABLE IF EXISTS `base_live_room_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_live_room_user`
(
    `id`                 bigint                                                    NOT NULL COMMENT '主键 id',
    `room_id`            bigint                                                    NOT NULL COMMENT '实时房间主键 id',
    `user_id`            bigint                                                    NOT NULL COMMENT '用户主键 id',
    `socket_ref_user_id` bigint                                                    NOT NULL COMMENT '套接字关联用户主键 id',
    `create_time`        datetime                                                  NOT NULL COMMENT '创建时间',
    `first_blob_str`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第一个 blob，用于播放时，需要先传递该 blob',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：实时房间用户表，主表：实时房间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_menu`
--

DROP TABLE IF EXISTS `base_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_menu`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300)                                                  NOT NULL,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名',
    `path`        varchar(100)                                                  NOT NULL COMMENT '页面的 path，备注：不能重复',
    `icon`        varchar(100)                                                  NOT NULL COMMENT '图标',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `show_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到',
    `link_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否外链，即，打开页面会在一个新的窗口打开，可以配合 router',
    `router`      varchar(100)                                                  NOT NULL COMMENT '路由',
    `redirect`    varchar(100)                                                  NOT NULL COMMENT '重定向，优先级最高',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_param`
--

DROP TABLE IF EXISTS `base_param`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_param`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名，以 uuid为不变值进行使用，不要用此属性',
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '值',
    `uuid`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该参数的 uuid，备注：系统内置参数的 uuid等于 id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `uuid` (`uuid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_pay`
--

DROP TABLE IF EXISTS `base_pay`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_pay`
(
    `id`                        bigint                                                        NOT NULL,
    `create_id`                 bigint                                                        NOT NULL,
    `create_time`               datetime                                                      NOT NULL,
    `update_id`                 bigint                                                        NOT NULL,
    `update_time`               datetime                                                      NOT NULL,
    `enable_flag`               tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                    varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述/备注',
    `pay_type`                  int                                                           NOT NULL COMMENT '支付方式：101 支付宝 201 微信 301 云闪付 401 谷歌',
    `base_pay_configuration_id` bigint                                                        NOT NULL COMMENT '支付配置主键 id',
    `pay_app_id`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，应用 id',
    `user_id`                   bigint                                                        NOT NULL COMMENT '用户主键 id',
    `subject`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付名称',
    `body`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品描述',
    `original_price`            decimal(13, 3)                                                NOT NULL COMMENT '支付原始的钱',
    `pay_price`                 decimal(13, 3)                                                NOT NULL COMMENT '实际支付的钱',
    `pay_currency`              varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '实际支付的钱的单位，例如：人民币 CNY',
    `expire_time`               datetime                                                      NOT NULL COMMENT '支付过期时间',
    `open_id`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户 openId',
    `status`                    int                                                           NOT NULL COMMENT '支付状态',
    `trade_no`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，支付号',
    `pay_return_value`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '调用支付之后，支付平台返回的数据，用于检查问题',
    `ref_type`                  int                                                           NOT NULL COMMENT '关联的类型',
    `ref_id`                    bigint                                                        NOT NULL COMMENT '关联的 id',
    `ref_data`                  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联的数据',
    `ref_status`                int                                                           NOT NULL COMMENT '关联的状态',
    `package_name`              varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'app包名，必须是创建登录 api项目时，创建 android客户端 id使用包名，例如：谷歌支付',
    `product_id`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对应购买商品的商品 id，例如：谷歌支付',
    `token`                     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '购买成功后 Purchase对象的 getPurchaseToken()，例如：谷歌支付',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_pay_configuration`
--

DROP TABLE IF EXISTS `base_pay_configuration`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_pay_configuration`
(
    `id`                     bigint                                                        NOT NULL,
    `create_id`              bigint                                                        NOT NULL,
    `create_time`            datetime                                                      NOT NULL,
    `update_id`              bigint                                                        NOT NULL,
    `update_time`            datetime                                                      NOT NULL,
    `enable_flag`            tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                 varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述/备注',
    `default_flag`           tinyint(1)                                                    NOT NULL COMMENT '是否是默认支付方式，备注：只会有一个默认支付方式',
    `type`                   int                                                           NOT NULL COMMENT '支付类型：101 支付宝 201 微信 301 云闪付 401 谷歌',
    `name`                   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付名',
    `server_url`             varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，网关地址，例如：https://openapi.alipay.com/gateway.do',
    `app_id`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，应用 id',
    `private_key`            longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '支付平台，私钥',
    `platform_public_key`    longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '支付平台，公钥',
    `notify_url`             varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，异步接收地址',
    `merchant_id`            varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，商户号',
    `merchant_serial_number` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，商户证书序列号',
    `api_v3_key`             varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台，商户APIV3密钥',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：支付配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_post`
--

DROP TABLE IF EXISTS `base_post`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_post`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：岗位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_post_ref_user`
--

DROP TABLE IF EXISTS `base_post_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_post_ref_user`
(
    `post_id` bigint NOT NULL COMMENT '岗位主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`post_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20230301：关联表：岗位表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_profit_share`
--

DROP TABLE IF EXISTS `base_profit_share`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_profit_share`
(
    `id`                                bigint                                                        NOT NULL,
    `create_id`                         bigint                                                        NOT NULL,
    `create_time`                       datetime                                                      NOT NULL,
    `update_id`                         bigint                                                        NOT NULL,
    `update_time`                       datetime                                                      NOT NULL,
    `enable_flag`                       tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                            varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述/备注',
    `order_id`                          bigint                                                        NOT NULL COMMENT '订单主键 id',
    `user_id`                           bigint                                                        NOT NULL COMMENT '用户主键 id',
    `name`                              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分账的名称',
    `pay_money`                         decimal(13, 3)                                                NOT NULL COMMENT '用户支付的钱',
    `type`                              int                                                           NOT NULL COMMENT '第三方分账平台类型',
    `other_profit_share_transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方分账订单号，备注：是分账的那笔订单号，不是支付时的订单号，该值的作用：如果该值为空，则表示分账失败，如果不为空，则表示分账成功',
    `other_app_id`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方应用的 appId',
    `up_user_id`                        bigint                                                        NOT NULL COMMENT '上级用户主键 id',
    `up_other_app_open_id`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：上级用户第三方应用的 openId',
    `up_money`                          decimal(13, 3)                                                NOT NULL COMMENT '上级用户分成的钱，备注：0表示不用分账',
    `up_up_user_id`                     bigint                                                        NOT NULL COMMENT '上上级用户主键 id',
    `up_up_other_app_open_id`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：上上级用户第三方应用的 openId',
    `up_up_money`                       decimal(13, 3)                                                NOT NULL COMMENT '上上级用户分成的钱，备注：0表示不用分账',
    `ref_type`                          int                                                           NOT NULL COMMENT '关联的类型',
    `ref_id`                            bigint                                                        NOT NULL COMMENT '关联的 id',
    `ref_data`                          varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联的数据',
    `ref_status`                        int                                                           NOT NULL COMMENT '关联的状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：分账表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_request`
--

DROP TABLE IF EXISTS `base_request`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_request`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `uri`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的uri',
    `method`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '请求方式',
    `name`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口名（备用）',
    `cost_ms`      bigint                                                        NOT NULL COMMENT '耗时（毫秒）',
    `category`     int                                                           NOT NULL COMMENT '请求类别',
    `ip`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip',
    `region`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Ip2RegionUtil.getRegion() 获取到的 ip所处区域',
    `success_flag` tinyint(1)                                                    NOT NULL COMMENT '请求是否成功',
    `type`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '请求类型',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `create_id_and_type` (`create_id`, `type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_request_info`
--

DROP TABLE IF EXISTS `base_request_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_request_info`
(
    `id`              bigint                                                        NOT NULL,
    `uri`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的uri',
    `cost_ms`         bigint                                                        NOT NULL COMMENT '耗时（毫秒）',
    `success_flag`    tinyint(1)                                                    NOT NULL COMMENT '请求是否成功',
    `error_msg`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '失败信息',
    `request_param`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '请求的参数',
    `response_value`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '请求返回的值',
    `request_header`  longtext                                                      NOT NULL COMMENT '请求的 header',
    `response_header` longtext                                                      NOT NULL COMMENT '响应的 header',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：请求详情表，主表：请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_role`
--

DROP TABLE IF EXISTS `base_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_role`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `update_id`    bigint                                                        NOT NULL,
    `update_time`  datetime                                                      NOT NULL,
    `enable_flag`  tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`         varchar(100)                                                  NOT NULL COMMENT '角色名（不能重复）',
    `uuid`         varchar(32)                                                   NOT NULL COMMENT '唯一标识（不能重复）',
    `default_flag` tinyint(1)                                                    NOT NULL COMMENT '是否是默认角色，备注：只会有一个默认角色',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_role_ref_auth`
--

DROP TABLE IF EXISTS `base_role_ref_auth`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_role_ref_auth`
(
    `role_id` bigint NOT NULL COMMENT '角色主键id',
    `auth_id` bigint NOT NULL COMMENT '权限主键id',
    PRIMARY KEY (`auth_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_role_ref_menu`
--

DROP TABLE IF EXISTS `base_role_ref_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_role_ref_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色主键 id',
    `menu_id` bigint NOT NULL COMMENT '菜单主键 id',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_role_ref_user`
--

DROP TABLE IF EXISTS `base_role_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_role_ref_user`
(
    `role_id` bigint NOT NULL COMMENT '角色主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_sms_configuration`
--

DROP TABLE IF EXISTS `base_sms_configuration`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_sms_configuration`
(
    `id`                    bigint                                                        NOT NULL,
    `create_id`             bigint                                                        NOT NULL,
    `create_time`           datetime                                                      NOT NULL,
    `update_id`             bigint                                                        NOT NULL,
    `update_time`           datetime                                                      NOT NULL,
    `enable_flag`           tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述/备注',
    `default_flag`          tinyint(1)                                                    NOT NULL COMMENT '是否是默认短信发送，备注：只会有一个默认短信发送',
    `type`                  int                                                           NOT NULL COMMENT '短信类型：101 阿里 201 腾讯',
    `name`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信名',
    `secret_id`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '钥匙',
    `secret_key`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '秘钥',
    `sdk_app_id`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信应用 id',
    `sign_name`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '签名内容',
    `send_common`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：通用短信',
    `send_sign_up`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：注册短信',
    `send_sign_in`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：登录短信',
    `send_set_password`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：设置密码',
    `send_update_password`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：修改密码',
    `send_set_user_name`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：设置用户名',
    `send_update_user_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：修改用户名',
    `send_set_email`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：设置邮箱',
    `send_update_email`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：修改邮箱',
    `send_set_wx`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：设置微信',
    `send_update_wx`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：修改微信',
    `send_set_phone`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：设置手机',
    `send_update_phone`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：修改手机',
    `send_forget_password`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：忘记密码',
    `send_sign_delete`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送：账号注销',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：短信配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_socket`
--

DROP TABLE IF EXISTS `base_socket`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_socket`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `scheme`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '协议',
    `host`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '主机',
    `port`        int                                                           NOT NULL COMMENT '端口',
    `path`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '路径',
    `type`        int                                                           NOT NULL COMMENT 'socket类型：101 tcp 201 webSocket 301 udp',
    `mac_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT 'mac地址，用于：和 port一起判断是否是重复启动，如果是，则需要移除之前的 socket信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：socket表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_socket_ref_user`
--

DROP TABLE IF EXISTS `base_socket_ref_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_socket_ref_user`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`     bigint                                                        NOT NULL COMMENT '用户主键 id',
    `socket_id`   bigint                                                        NOT NULL COMMENT 'socket主键 id',
    `nickname`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段，用户昵称',
    `scheme`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段，协议',
    `host`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段，主机',
    `port`        int                                                           NOT NULL COMMENT '冗余字段，端口',
    `path`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段，路径',
    `mac_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '冗余字段：mac地址，用于：和 port一起判断是否是重复启动，如果是，则需要移除之前的 socket信息',
    `type`        int                                                           NOT NULL COMMENT '冗余字段，socket类型',
    `online_type` int                                                           NOT NULL COMMENT 'socket 在线状态',
    `ip`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip',
    `region`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Ip2RegionUtil.getRegion() 获取到的 ip所处区域',
    `category`    int                                                           NOT NULL COMMENT '请求类别',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20230901：关联表：socket，用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_sql_slow`
--

DROP TABLE IF EXISTS `base_sql_slow`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_sql_slow`
(
    `id`          bigint                                                       NOT NULL,
    `create_id`   bigint                                                       NOT NULL,
    `create_time` datetime                                                     NOT NULL,
    `name`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci    NOT NULL COMMENT 'sqlId，如：com.cmcorg20230301.be.engine.security.mapper.SysUserMapper.insert',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'sql语句类型：SELECT、DELETE、INSERT、UPDATE',
    `cost_ms`     bigint                                                       NOT NULL COMMENT '耗时（毫秒）',
    `sql_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci    NOT NULL COMMENT 'sql内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：慢sql日表志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_third_app`
--

DROP TABLE IF EXISTS `base_third_app`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_third_app`
(
    `id`                      bigint                                                        NOT NULL,
    `create_id`               bigint                                                        NOT NULL,
    `create_time`             datetime                                                      NOT NULL,
    `update_id`               bigint                                                        NOT NULL,
    `update_time`             datetime                                                      NOT NULL,
    `enable_flag`             tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `type`                    int                                                           NOT NULL COMMENT '第三方应用类型：101 微信小程序 102 微信公众号',
    `name`                    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方应用名',
    `app_id`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方应用的 appId，备注：同一个类型下，所有租户不能重复，原因：比如接收公众号消息时，就无法找到具体是哪一个租户',
    `secret`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方应用的 secret',
    `subscribe_reply_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户点击关注之后，回复的内容，备注：如果取关然后再关注，也会回复该内容',
    `text_reply_content`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户发送文字之后，回复的内容',
    `image_reply_content`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户发送图片之后，回复的内容',
    `qr_code`                 varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '二维码，备注：不是二维码图片的地址，而是二维码解码之后的值',
    `open_id`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方应用的 openId/微信号/企业ID，例如：接收微信公众号消息时的 ToUserName',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：三方应用配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_third_app_official_menu`
--

DROP TABLE IF EXISTS `base_third_app_official_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_third_app_official_menu`
(
    `id`            bigint                                                        NOT NULL,
    `create_id`     bigint                                                        NOT NULL,
    `create_time`   datetime                                                      NOT NULL,
    `update_id`     bigint                                                        NOT NULL,
    `update_time`   datetime                                                      NOT NULL,
    `enable_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`        varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名，备注：一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以 ... 代替',
    `pid`           bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`      int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `third_app_id`  bigint                                                        NOT NULL COMMENT '所属的，第三方应用相关配置主键 id',
    `type`          int                                                           NOT NULL COMMENT '类型：101 微信公众号',
    `button_type`   int                                                           NOT NULL COMMENT '按钮类型：101 点击 201 链接 301 小程序',
    `value`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '如果是按钮，则表示按钮的 key，如果是链接，则表示是 url',
    `reply_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复的内容，备注：一般是点击按钮之后，回复的内容',
    `page_path`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '小程序的页面路径',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：公众号菜单配置表，主表：三方应用配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user`
--

DROP TABLE IF EXISTS `base_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `remark`      varchar(300)                                                  NOT NULL,
    `password`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】',
    `email`       varchar(100)                                                  NOT NULL COMMENT '邮箱，可以为空',
    `username`    varchar(20)                                                   NOT NULL COMMENT '用户名，可以为空',
    `phone`       varchar(100)                                                  NOT NULL COMMENT '手机号，可以为空',
    `wx_app_id`   varchar(100)                                                  NOT NULL COMMENT '微信 appId，可以为空',
    `wx_open_id`  varchar(100)                                                  NOT NULL COMMENT '微信 openId，可以为空',
    `wx_union_id` varchar(100)                                                  NOT NULL COMMENT '微信 unionId，可以为空',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_bank_card`
--

DROP TABLE IF EXISTS `base_user_bank_card`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_bank_card`
(
    `id`               bigint                                                        NOT NULL COMMENT '主键 id',
    `create_id`        bigint                                                        NOT NULL,
    `create_time`      datetime                                                      NOT NULL,
    `update_id`        bigint                                                        NOT NULL,
    `update_time`      datetime                                                      NOT NULL,
    `enable_flag`      tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`          bigint                                                        NOT NULL COMMENT '用户主键 id',
    `bank_card_no`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '卡号',
    `open_bank_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '开户行',
    `branch_bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支行',
    `payee_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收款人姓名',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：用户银行卡表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_configuration`
--

DROP TABLE IF EXISTS `base_user_configuration`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_configuration`
(
    `id`                       bigint     NOT NULL COMMENT '主键 id',
    `user_name_sign_up_enable` tinyint(1) NOT NULL COMMENT '是否启用：用户名注册功能，默认启用',
    `email_sign_up_enable`     tinyint(1) NOT NULL COMMENT '是否启用：邮箱注册功能，默认启用',
    `phone_sign_up_enable`     tinyint(1) NOT NULL COMMENT '是否启用：手机号码注册功能，默认启用',
    `manage_operate_enable`    tinyint    NOT NULL COMMENT '管理员用户是否可以操作，备注：超级管理员除外',
    `normal_operate_enable`    tinyint    NOT NULL COMMENT '普通用户是否可以操作，备注：管理员和超级管理员除外',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：用户配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_delete_log`
--

DROP TABLE IF EXISTS `base_user_delete_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_delete_log`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `create_id`        bigint                                                        NOT NULL,
    `create_time`      datetime                                                      NOT NULL,
    `update_id`        bigint                                                        NOT NULL,
    `update_time`      datetime                                                      NOT NULL,
    `password`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】',
    `email`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱，可以为空',
    `username`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '用户名，可以为空',
    `phone`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号，可以为空',
    `wx_open_id`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信 openId，可以为空',
    `wx_app_id`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信 appId，可以为空',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid',
    `nickname`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '昵称',
    `bio`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '个人简介',
    `avatar_file_id`   bigint                                                        NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `user_create_time` datetime                                                      NOT NULL COMMENT '用户创建时间',
    `sign_up_type`     int                                                           NOT NULL COMMENT '注册终端',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：用户注销记录表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_info`
--

DROP TABLE IF EXISTS `base_user_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_info`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统目前只有即时聊天使用该字段，备注：不能重复',
    `nickname`         varchar(50)                                                   NOT NULL COMMENT '昵称',
    `bio`              varchar(100)                                                  NOT NULL COMMENT '个人简介',
    `avatar_file_id`   bigint                                                        NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `sign_up_type`     int                                                           NOT NULL COMMENT '注册终端',
    `last_active_time` datetime                                                      NOT NULL COMMENT '最近活跃时间',
    `last_ip`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最近 ip',
    `last_region`      varchar(200)                                                  NOT NULL COMMENT '最近 ip所处区域',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `base_user_info_uuid_IDX` (`uuid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：用户基本信息表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_wallet`
--

DROP TABLE IF EXISTS `base_user_wallet`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_wallet`
(
    `id`                 bigint                                                        NOT NULL COMMENT '用户主键 id',
    `create_id`          bigint                                                        NOT NULL,
    `create_time`        datetime                                                      NOT NULL,
    `update_id`          bigint                                                        NOT NULL,
    `update_time`        datetime                                                      NOT NULL,
    `enable_flag`        tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `remark`             varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `withdrawable_money` decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：用户钱包表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_wallet_log`
--

DROP TABLE IF EXISTS `base_user_wallet_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_wallet_log`
(
    `id`                        bigint                                                        NOT NULL,
    `create_id`                 bigint                                                        NOT NULL,
    `create_time`               datetime                                                      NOT NULL,
    `update_id`                 bigint                                                        NOT NULL,
    `update_time`               datetime                                                      NOT NULL,
    `enable_flag`               tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                    varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`                   bigint                                                        NOT NULL COMMENT '用户主键 id',
    `name`                      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录名',
    `type`                      int                                                           NOT NULL COMMENT '记录类型：1开头 增加 2开头 减少',
    `ref_id`                    bigint                                                        NOT NULL COMMENT '关联的 id',
    `ref_data`                  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联的数据',
    `withdrawable_money_pre`    decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，前',
    `withdrawable_money_change` decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，变',
    `withdrawable_money_suf`    decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，后',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：用户钱包操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_user_wallet_withdraw_log`
--

DROP TABLE IF EXISTS `base_user_wallet_withdraw_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_wallet_withdraw_log`
(
    `id`               bigint                                                        NOT NULL,
    `create_id`        bigint                                                        NOT NULL,
    `create_time`      datetime                                                      NOT NULL,
    `update_id`        bigint                                                        NOT NULL,
    `update_time`      datetime                                                      NOT NULL,
    `enable_flag`      tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`          bigint                                                        NOT NULL COMMENT '用户主键 id',
    `withdraw_money`   decimal(13, 3)                                                NOT NULL COMMENT '提现金额',
    `bank_card_no`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：卡号',
    `open_bank_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：开户行',
    `branch_bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：支行',
    `payee_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：收款人姓名',
    `withdraw_status`  int                                                           NOT NULL COMMENT '提现状态',
    `reject_reason`    varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拒绝理由',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：用户钱包提现记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'be_base_20240901'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2026-03-18 10:46:33
