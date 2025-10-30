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
    `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '该用户的 uuid',
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
-- Dumping data for table `base_user_delete_log`
--

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
-- Dumping data for table `base_socket_ref_user`
--

--
-- Table structure for table `base_im_apply_group_extra`
--

DROP TABLE IF EXISTS `base_im_apply_group_extra`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_apply_group_extra`
(
    `apply_group_id` bigint     NOT NULL COMMENT '群组申请主键 id',
    `user_id`        bigint     NOT NULL COMMENT '用户主键 id',
    `hidden_flag`    tinyint(1) NOT NULL COMMENT '是否隐藏',
    PRIMARY KEY (`apply_group_id`, `user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：群组申请扩展表，主表：群组申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_im_apply_group_extra`
--

LOCK TABLES `base_im_apply_group_extra` WRITE;
/*!40000 ALTER TABLE `base_im_apply_group_extra`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_im_apply_group_extra`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_activiti_process_instance`
--

LOCK TABLES `base_activiti_process_instance` WRITE;
/*!40000 ALTER TABLE `base_activiti_process_instance`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_activiti_process_instance`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_menu`
--

LOCK TABLES `base_menu` WRITE;
/*!40000 ALTER TABLE `base_menu`
    DISABLE KEYS */;
INSERT INTO `base_menu`
VALUES (1, 0, '2024-08-16 16:18:15', 240902154357005281, '2024-09-02 16:07:57', 1, '', '系统管理',
        '/base', 'ri:settings-3-line', 0, 1, 0, '', '', 10000, 'Base'),
       (2, 0, '2024-08-16 16:18:15', 0, '2024-08-26 10:43:33', 1, '', '菜单管理', '/base/menu',
        'ep:menu', 1, 1, 0, '', '', 10000, 'BaseMenu'),
       (240823111516002532, 0, '2024-08-23 11:15:16', 0, '2024-08-26 10:43:33', 1, '', '角色管理',
        '/base/role', 'ri:file-user-fill', 1, 1, 0, '', '', 9900, 'BaseRole'),
       (240823140711002641, 0, '2024-08-23 14:07:11', 0, '2024-08-30 09:10:37', 1, '', '权限管理',
        '/base/auth', 'ri:key-2-fill', 1, 1, 0, '', '', 9800, 'BaseAuth'),
       (240823162121002971, 0, '2024-08-23 16:21:21', 0, '2024-08-26 10:43:33', 1, '', '用户管理',
        '/base/user', 'ri:user-settings-fill', 1, 1, 0, '', '', 9700, 'BaseUser'),
       (240912092930005831, 0, '2024-09-12 09:29:31', 0, '2024-09-12 09:39:52', 1, '', '区域管理',
        '/base/area', 'ri:compass-3-fill', 1, 1, 0, '', '', 9500, 'BaseArea'),
       (240912093214005832, 0, '2024-09-12 09:32:14', 0, '2024-09-12 09:43:35', 1, '', '部门管理',
        '/base/dept', 'ri:cup-fill', 1, 1, 0, '', '', 9400, 'BaseDept'),
       (240912094324005833, 0, '2024-09-12 09:43:25', 0, '2024-09-12 09:43:38', 1, '', '岗位管理',
        '/base/post', 'ep:briefcase', 1, 1, 0, '', '', 9300, 'BasePost'),
       (240924112136000441, 0, '2024-09-24 11:21:37', 0, '2024-09-24 11:25:31', 1, '', '参数管理',
        '/base/param', 'ri:album-fill', 1, 1, 0, '', '', 9200, 'BaseParam'),
       (240924112334000442, 0, '2024-09-24 11:23:34', 0, '2024-09-24 11:26:08', 1, '', '字典管理',
        '/base/dict', 'ri:book-2-fill', 1, 1, 0, '', '', 9100, 'BaseDict'),
       (241125092352001852, 240902154357005281, '2024-11-25 09:23:52', 240902154357005281,
        '2024-11-25 11:03:41', 1, '', '请求日志', '/base/request', 'ri:file-search-fill',
        241125105944002273, 1, 0, '', '', 10000, 'BaseRequest'),
       (241125105944002273, 240902154357005281, '2024-11-25 10:59:45', 240902154357005281,
        '2024-11-25 11:04:39', 1, '', '系统监控', '/request', 'ep:monitor', 0, 1, 0, '', '', 9900,
        'Request'),
       (241125110315002295, 240902154357005281, '2024-11-25 11:03:15', 240902154357005281,
        '2024-11-25 11:03:15', 1, '', '在线用户', '/base/socketRefUser', 'ri:user-voice-fill',
        241125105944002273, 1, 0, '', '', 9900, 'BaseSocketRefUser'),
       (241125110422002303, 240902154357005281, '2024-11-25 11:04:23', 240902154357005281,
        '2024-11-25 11:04:23', 1, '', '套接字管理', '/base/socket', 'ri:server-fill',
        241125105944002273, 1, 0, '', '', 9800, 'BaseSocket'),
       (241125110642002342, 240902154357005281, '2024-11-25 11:06:43', 0, '2024-11-27 16:15:05', 1,
        '', '文件配置', '/file/fileStorageConfiguration', 'ri:folder-settings-fill',
        241125111227002399, 1, 0, '', '', 9800, 'BaseFileStorageConfiguration'),
       (241125110957002380, 240902154357005281, '2024-11-25 11:09:57', 240902154357005281,
        '2024-11-25 11:12:37', 1, '', '用户配置', '/base/userConfiguration',
        'ri:user-settings-fill', 241125111227002399, 1, 0, '', '', 10000, 'BaseUserConfiguration'),
       (241125111059002386, 240902154357005281, '2024-11-25 11:10:59', 240902154357005281,
        '2024-11-25 11:12:43', 1, '', '邮件配置', '/base/emailConfiguration',
        'ri:mail-settings-fill', 241125111227002399, 1, 0, '', '', 9900, 'BaseEmailConfiguration'),
       (241125111227002399, 240902154357005281, '2024-11-25 11:12:28', 240902154357005281,
        '2024-11-25 11:12:28', 1, '', '系统配置', '/configuration', 'ri:settings-6-line', 0, 1, 0,
        '', '', 9700, 'Configuration'),
       (241125111514002426, 240902154357005281, '2024-11-25 11:15:14', 0, '2025-01-03 15:02:10', 1,
        '', '支付配置', '/pay/payConfiguration', 'ri:creative-commons-nd-fill', 241125111227002399,
        1, 0, '', '', 9700, 'BasePayConfiguration'),
       (241125111820002436, 240902154357005281, '2024-11-25 11:18:20', 0, '2024-12-18 10:37:33', 1,
        '', '三方应用配置', '/base/thirdApp', 'ri:angularjs-fill', 241125111227002399, 1, 0, '', '',
        9600, 'BaseOtherApp'),
       (241125111957002443, 240902154357005281, '2024-11-25 11:19:57', 240902154357005281,
        '2024-11-25 16:37:36', 1, '', '短信配置', '/base/smsConfiguration', 'ri:smartphone-fill',
        241125111227002399, 1, 0, '', '', 9500, 'BaseSmsConfiguration'),
       (241125163831002660, 240902154357005281, '2024-11-25 16:38:32', 0, '2024-11-27 09:19:50', 1,
        '', '用户注销日志', '/base/userDeleteLog', 'ri:user-unfollow-fill', 1, 1, 0, '', '', 9600,
        'BaseUserDeleteLog'),
       (241125164313002674, 240902154357005281, '2024-11-25 16:43:14', 0, '2024-11-28 09:53:53', 1,
        '', '文件管理', '/file/fileSystem', 'ri:file-list-2-fill', 241128095243004921, 1, 0, '', '',
        10000, 'FileSystem'),
       (241125165403002869, 0, '2024-11-25 16:54:04', 0, '2024-11-25 16:54:04', 1, '', '令牌管理',
        '/base/apiToken', 'ri:key-2-fill', 1, 1, 0, '', '', 9000, 'BaseApiToken'),
       (241128095243004921, 0, '2024-11-28 09:52:44', 0, '2024-11-28 09:52:57', 1, '', '文件系统',
        '/file', 'ep:files', 0, 1, 0, '', '', 9600, 'File'),
       (250516090553035041, 0, '2025-05-16 09:05:53', 0, '2025-08-25 16:24:23', 1, '', '实时房间',
        '/live', 'ep:video-camera', 0, 0, 0, '', '', 9500, 'Live'),
       (250516091519035092, 0, '2025-05-16 09:15:20', 0, '2025-05-16 09:15:27', 1, '', '房间管理',
        '/live/self/liveRoomSelf', 'ri:settings-2-fill', 250516090553035041, 1, 0, '', '', 9400,
        'BaseLiveRoomSelf'),
       (250516091721035107, 0, '2025-05-16 09:17:21', 0, '2025-05-16 09:17:21', 1, '', '当前房间',
        '/live/self/liveRoomUserSelf', 'ri:play-circle-fill', 250516090553035041, 1, 0, '', '',
        9300, 'BaseLiveRoomUserSelf'),
       (250826084605063989, 0, '2025-08-26 08:46:05', 0, '2025-08-26 08:46:15', 1, '', '即时聊天',
        '/im', 'ri:chat-1-line', 0, 1, 0, '', '', 9400, 'Im'),
       (250826084724063998, 0, '2025-08-26 08:47:24', 0, '2025-08-26 08:47:24', 1, '', '开始聊天',
        '/im/imIndex', 'ri:chat-2-fill', 250826084605063989, 1, 0, '', '', 10000, 'ImIndex');
/*!40000 ALTER TABLE `base_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_file_auth`
--

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
-- Dumping data for table `base_dept_ref_user`
--

LOCK TABLES `base_dept_ref_user` WRITE;
/*!40000 ALTER TABLE `base_dept_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_dept_ref_user`
VALUES (240912141649006163, 240902163618005502);
/*!40000 ALTER TABLE `base_dept_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    `last_receive_ts`   bigint NOT NULL COMMENT '最后一次接受到消息时的时间戳，默认为：-1，备注：该字段用于：排序',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_im_session`
--

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
-- Dumping data for table `base_role`
--

LOCK TABLES `base_role` WRITE;
/*!40000 ALTER TABLE `base_role`
    DISABLE KEYS */;
INSERT INTO `base_role`
VALUES (240830160154004401, 0, '2024-08-30 16:01:55', 0, '2025-08-25 16:24:48', 1, '', '管理员',
        'admin', 0),
       (240830160647004402, 0, '2024-08-30 16:06:48', 0, '2025-08-25 16:24:41', 1, '', '默认角色',
        'default', 1);
/*!40000 ALTER TABLE `base_role`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_live_room_data`
--

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
-- Dumping data for table `base_pay_configuration`
--

LOCK TABLES `base_pay_configuration` WRITE;
/*!40000 ALTER TABLE `base_pay_configuration`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_pay_configuration`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_area_ref_user`
--

LOCK TABLES `base_area_ref_user` WRITE;
/*!40000 ALTER TABLE `base_area_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_area_ref_user`
VALUES (240912111901006052, 240902154357005281),
       (240912111901006052, 240902163618005502),
       (240912111901006052, 240912095958005834);
/*!40000 ALTER TABLE `base_area_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_apply_friend_extra`
--

LOCK TABLES `base_im_apply_friend_extra` WRITE;
/*!40000 ALTER TABLE `base_im_apply_friend_extra`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_im_apply_friend_extra`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_im_group`
--

DROP TABLE IF EXISTS `base_im_group`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_group`
(
    `id`             bigint      NOT NULL,
    `create_id`      bigint      NOT NULL,
    `create_time`    datetime    NOT NULL,
    `update_id`      bigint      NOT NULL,
    `update_time`    datetime    NOT NULL,
    `belong_id`      bigint      NOT NULL COMMENT '归属者主键 id',
    `name`           varchar(50) NOT NULL COMMENT '群组名称',
    `avatar_file_id` bigint      NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `session_id`     bigint      NOT NULL COMMENT '会话主键 id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：群组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_im_group`
--

LOCK TABLES `base_im_group` WRITE;
/*!40000 ALTER TABLE `base_im_group`
    DISABLE KEYS */;
INSERT INTO `base_im_group`
VALUES (250427140812019547, 0, '2025-04-27 14:08:13', 0, '2025-04-27 14:08:13', 0, '测试群1', -1,
        250427140812019548);
/*!40000 ALTER TABLE `base_im_group`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_user_wallet_log`
--

DROP TABLE IF EXISTS `base_user_wallet_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_wallet_log`
(
    `id`                                bigint                                                        NOT NULL,
    `create_id`                         bigint                                                        NOT NULL,
    `create_time`                       datetime                                                      NOT NULL,
    `update_id`                         bigint                                                        NOT NULL,
    `update_time`                       datetime                                                      NOT NULL,
    `enable_flag`                       tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                            varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `user_id`                           bigint                                                        NOT NULL COMMENT '用户主键 id',
    `name`                              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录名',
    `type`                              int                                                           NOT NULL COMMENT '记录类型：1开头 增加 2开头 减少',
    `ref_id`                            bigint                                                        NOT NULL COMMENT '关联的 id',
    `ref_data`                          varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联的数据',
    `withdrawable_money_pre`            decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，前',
    `withdrawable_money_change`         decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，变',
    `withdrawable_money_suf`            decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，后',
    `withdrawable_pre_use_money_pre`    decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，预使用，前',
    `withdrawable_pre_use_money_change` decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，预使用，变',
    `withdrawable_pre_use_money_suf`    decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱，预使用，后',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：用户钱包操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_user_wallet_log`
--

LOCK TABLES `base_user_wallet_log` WRITE;
/*!40000 ALTER TABLE `base_user_wallet_log`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_user_wallet_log`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_file`
--

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
-- Dumping data for table `base_role_ref_auth`
--

LOCK TABLES `base_role_ref_auth` WRITE;
/*!40000 ALTER TABLE `base_role_ref_auth`
    DISABLE KEYS */;
INSERT INTO `base_role_ref_auth`
VALUES (240830160154004401, 240902143347004951),
       (240830160154004401, 240902143348004952),
       (240830160647004402, 240902143348004952),
       (240830160154004401, 240902143348004953),
       (240830160154004401, 240902143348004954),
       (240830160647004402, 240902143348004954),
       (240830160154004401, 240902143348004955),
       (240830160647004402, 240902143348004955),
       (240830160154004401, 240902143451004956),
       (240830160154004401, 240902143451004957),
       (240830160647004402, 240902143451004957),
       (240830160154004401, 240902143451004958),
       (240830160154004401, 240902143451004959),
       (240830160647004402, 240902143451004959),
       (240830160154004401, 240902143452004960),
       (240830160647004402, 240902143452004960),
       (240830160154004401, 240902143513004961),
       (240830160154004401, 240902143513004962),
       (240830160647004402, 240902143513004962),
       (240830160154004401, 240902143513004963),
       (240830160154004401, 240902143513004964),
       (240830160647004402, 240902143513004964),
       (240830160154004401, 240902143514004965),
       (240830160647004402, 240902143514004965),
       (240830160154004401, 240902143532004966),
       (240830160154004401, 240902143532004967),
       (240830160647004402, 240902143532004967),
       (240830160154004401, 240902143533004968),
       (240830160154004401, 240902143533004969),
       (240830160154004401, 240902143533004970),
       (240830160647004402, 240902143533004970),
       (240830160154004401, 241015142748002311),
       (240830160154004401, 241015142749002312),
       (240830160647004402, 241015142749002312),
       (240830160154004401, 241015142749002313),
       (240830160154004401, 241015142750002314),
       (240830160647004402, 241015142750002314),
       (240830160154004401, 241015142751002315),
       (240830160647004402, 241015142751002315),
       (240830160154004401, 241015142855002316),
       (240830160154004401, 241015142855002317),
       (240830160647004402, 241015142855002317),
       (240830160154004401, 241015142856002318),
       (240830160154004401, 241015142857002319),
       (240830160647004402, 241015142857002319),
       (240830160154004401, 241015142857002320),
       (240830160647004402, 241015142857002320),
       (240830160154004401, 241015143600002321),
       (240830160154004401, 241015143600002322),
       (240830160647004402, 241015143600002322),
       (240830160154004401, 241015143601002323),
       (240830160154004401, 241015143602002324),
       (240830160647004402, 241015143602002324),
       (240830160154004401, 241015143602002325),
       (240830160647004402, 241015143602002325),
       (240830160154004401, 241015143619002326),
       (240830160154004401, 241015143619002327),
       (240830160647004402, 241015143619002327),
       (240830160154004401, 241015143620002328),
       (240830160154004401, 241015143621002329),
       (240830160647004402, 241015143621002329),
       (240830160154004401, 241015143621002330),
       (240830160647004402, 241015143621002330),
       (240830160154004401, 241015143631002331),
       (240830160154004401, 241015143632002332),
       (240830160647004402, 241015143632002332),
       (240830160154004401, 241015143632002333),
       (240830160154004401, 241015143633002334),
       (240830160647004402, 241015143633002334),
       (240830160154004401, 241015143634002335),
       (240830160647004402, 241015143634002335),
       (240830160154004401, 241125092606001883),
       (240830160647004402, 241125092606001883),
       (240830160154004401, 241125092636001887),
       (240830160154004401, 250915080423066977);
/*!40000 ALTER TABLE `base_role_ref_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_file_transfer_chunk`
--

LOCK TABLES `base_file_transfer_chunk` WRITE;
/*!40000 ALTER TABLE `base_file_transfer_chunk`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_file_transfer_chunk`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_bulletin`
--

LOCK TABLES `base_bulletin` WRITE;
/*!40000 ALTER TABLE `base_bulletin`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_bulletin`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_socket`
--

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
-- Dumping data for table `base_user_wallet_withdraw_log`
--

LOCK TABLES `base_user_wallet_withdraw_log` WRITE;
/*!40000 ALTER TABLE `base_user_wallet_withdraw_log`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_user_wallet_withdraw_log`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_area`
--

LOCK TABLES `base_area` WRITE;
/*!40000 ALTER TABLE `base_area`
    DISABLE KEYS */;
INSERT INTO `base_area`
VALUES (240912111901006052, 0, '2024-09-12 11:19:01', 0, '2024-09-12 14:15:58', 1, '', '地球', 0,
        10000, '504841d4b6c84889961c307b977ab58b'),
       (240912111910006053, 0, '2024-09-12 11:19:11', 0, '2024-09-12 14:10:02', 1, '', '中国',
        240912111901006052, 10000, 'f7e06bdb5ede49d4ac9f28b4cc52bdd7'),
       (240912111927006054, 1, '2024-09-12 11:19:28', 0, '2024-09-12 14:10:02', 1, '', '美国',
        240912111901006052, 9900, '1fe958edb5f3460ba1fa1733e8b98839'),
       (240912111946006055, 0, '2024-09-12 11:19:47', 0, '2024-09-12 14:10:02', 1, '', '北京',
        240912111910006053, 10000, '5693a2bbde684df9a4d653f209f02c47'),
       (240912111954006056, 0, '2024-09-12 11:19:54', 0, '2024-09-12 14:10:02', 1, '', '纽约',
        240912111927006054, 10000, '87877183d5cf4a18973700f0a2d6543c'),
       (240912112022006057, 0, '2024-09-12 11:20:22', 0, '2024-09-12 14:10:02', 1, '', '洛杉矶',
        240912111927006054, 9900, '11116b4ca1b4441089181ab481f035bd'),
       (240912112028006058, 0, '2024-09-12 11:20:28', 0, '2024-09-12 14:10:02', 1, '', '芝加哥',
        240912111927006054, 9800, 'd626992880b441d68556db0448f75f5b'),
       (240912112034006059, 0, '2024-09-12 11:20:34', 0, '2024-09-12 14:10:02', 1, '', '华盛顿',
        240912111927006054, 9700, '60c384168f44470daccd7d6bfd5c480b'),
       (240912112043006060, 0, '2024-09-12 11:20:44', 0, '2024-09-12 14:10:02', 1, '', '上海',
        240912111910006053, 9900, 'e350a925ae8545b587eb6ad23637b1e3'),
       (240912140929006161, 0, '2024-09-12 14:09:30', 0, '2024-09-12 14:10:02', 1, '', '广州',
        240912111910006053, 9800, '5652f0e49d1d4b718fa3f4292f328ebb'),
       (240912140937006162, 0, '2024-09-12 14:09:37', 0, '2024-09-12 14:10:02', 1, '', '深圳',
        240912111910006053, 9700, '92d3475c58df465e9d10fd24419b7094');
/*!40000 ALTER TABLE `base_area`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_sms_configuration`
--

LOCK TABLES `base_sms_configuration` WRITE;
/*!40000 ALTER TABLE `base_sms_configuration`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_sms_configuration`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_bulletin_read_time_ref_user`
--

LOCK TABLES `base_bulletin_read_time_ref_user` WRITE;
/*!40000 ALTER TABLE `base_bulletin_read_time_ref_user`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_bulletin_read_time_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_file_transfer`
--

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
    `name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名，以 uuid为不变值进行使用，不要用此属性',
    `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL COMMENT '值',
    `uuid`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该参数的 uuid，备注：系统内置参数的 uuid等于 id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `uuid` (`uuid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_param`
--

LOCK TABLES `base_param` WRITE;
/*!40000 ALTER TABLE `base_param`
    DISABLE KEYS */;
INSERT INTO `base_param`
VALUES (240924144747000551, 0, '2024-09-24 14:47:47', 0, '2024-09-24 14:47:47', 1, '',
        '默认允许后台登录', 'true', '3');
/*!40000 ALTER TABLE `base_param`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_friend`
--

LOCK TABLES `base_im_friend` WRITE;
/*!40000 ALTER TABLE `base_im_friend`
    DISABLE KEYS */;
INSERT INTO `base_im_friend`
VALUES (250428110342020958, '2025-04-28 11:03:42', 0, 240902154357005281, 250428105947020916),
       (250428110342020959, '2025-04-28 11:03:42', 240902154357005281, 0, 250428105947020916);
/*!40000 ALTER TABLE `base_im_friend`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_user`
--

LOCK TABLES `base_user` WRITE;
/*!40000 ALTER TABLE `base_user`
    DISABLE KEYS */;
INSERT INTO `base_user`
VALUES (0, 0, '2024-09-12 09:59:59', 0, '2025-09-22 17:03:42', 0, '',
        'a67fd7506ef145958d866f5b2cad70b3/6c2bbd3f3e9041c789e33da5bd520011c1094b70ad8b5c29c04a85051dee561da4f3dce475964eea913196e37e75b5e1fdd49740f228d3bb06df99db7bec7bab',
        '', 'admin', '', '', '', ''),
       (240902154357005281, 0, '2024-09-02 15:43:57', 0, '2024-11-27 16:27:31', 1, '',
        'fc3727dc1a1c43c0a0a9fec4d4650225/4b4e73f56f4d2a54efe1ecc780261a79162f76ee9fd5ab04d1c6ac4b78e9974ddc3a5c9fe1088eb545779a89f64afd21bcd293871991e41500d184e812e1fdf8',
        'dimensional_logic@foxmail.com', 'kar1', '', '', '', ''),
       (240902163618005502, 240902154357005281, '2024-09-02 16:36:18', 0, '2024-09-24 15:32:32', 1,
        '',
        '16b275b3a5b549838c6399c0e9159995/5693d65214a99b53a947a4dd7af5b17a48d01cdee0207c949ea0e37acc0505dc3ce87673e1320b975cda4bc71ad29df26aed05a6dfb7063adbcbd86deed59c1b',
        '', 'kar2', '', '', '', '');
/*!40000 ALTER TABLE `base_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_role_ref_menu`
--

LOCK TABLES `base_role_ref_menu` WRITE;
/*!40000 ALTER TABLE `base_role_ref_menu`
    DISABLE KEYS */;
INSERT INTO `base_role_ref_menu`
VALUES (240830160154004401, 1),
       (240830160154004401, 2),
       (240830160154004401, 240823111516002532),
       (240830160154004401, 240823140711002641),
       (240830160154004401, 240823162121002971),
       (240830160154004401, 240912092930005831),
       (240830160154004401, 240912093214005832),
       (240830160154004401, 240912094324005833),
       (240830160154004401, 240924112136000441),
       (240830160154004401, 240924112334000442),
       (240830160154004401, 241125092352001852),
       (240830160154004401, 241125105944002273),
       (240830160154004401, 241125110315002295),
       (240830160154004401, 241125110422002303),
       (240830160154004401, 241125110642002342),
       (240830160154004401, 241125110957002380),
       (240830160154004401, 241125111059002386),
       (240830160154004401, 241125111227002399),
       (240830160154004401, 241125111514002426),
       (240830160154004401, 241125111820002436),
       (240830160154004401, 241125111957002443),
       (240830160154004401, 241125163831002660),
       (240830160154004401, 241125164313002674),
       (240830160154004401, 241125165403002869),
       (240830160154004401, 241128095243004921),
       (240830160154004401, 250826084605063989),
       (240830160154004401, 250826084724063998),
       (240830160647004402, 1),
       (240830160647004402, 2),
       (240830160647004402, 240823111516002532),
       (240830160647004402, 240823140711002641),
       (240830160647004402, 240823162121002971),
       (240830160647004402, 240912092930005831),
       (240830160647004402, 240912093214005832),
       (240830160647004402, 240912094324005833),
       (240830160647004402, 240924112136000441),
       (240830160647004402, 240924112334000442),
       (240830160647004402, 241125092352001852),
       (240830160647004402, 241125105944002273),
       (240830160647004402, 241125110315002295),
       (240830160647004402, 241125110422002303),
       (240830160647004402, 241125110642002342),
       (240830160647004402, 241125110957002380),
       (240830160647004402, 241125111059002386),
       (240830160647004402, 241125111227002399),
       (240830160647004402, 241125111514002426),
       (240830160647004402, 241125111820002436),
       (240830160647004402, 241125111957002443),
       (240830160647004402, 241125163831002660),
       (240830160647004402, 241125164313002674),
       (240830160647004402, 241125165403002869),
       (240830160647004402, 241128095243004921),
       (240830160647004402, 250826084605063989),
       (240830160647004402, 250826084724063998);
/*!40000 ALTER TABLE `base_role_ref_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_third_app_official_menu`
--

LOCK TABLES `base_third_app_official_menu` WRITE;
/*!40000 ALTER TABLE `base_third_app_official_menu`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_third_app_official_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_auth`
--

LOCK TABLES `base_auth` WRITE;
/*!40000 ALTER TABLE `base_auth`
    DISABLE KEYS */;
INSERT INTO `base_auth`
VALUES (240902143347004951, 0, '2024-09-02 14:33:48', 0, '2024-09-02 14:33:48', 1, '',
        '基础:菜单:新增修改', 'baseMenu:insertOrUpdate'),
       (240902143348004952, 0, '2024-09-02 14:33:48', 0, '2024-09-02 14:33:48', 1, '',
        '基础:菜单:列表查询', 'baseMenu:page'),
       (240902143348004953, 0, '2024-09-02 14:33:48', 0, '2024-09-02 14:33:48', 1, '',
        '基础:菜单:删除', 'baseMenu:deleteByIdSet'),
       (240902143348004954, 0, '2024-09-02 14:33:49', 0, '2024-09-02 14:33:49', 1, '',
        '基础:菜单:查看详情', 'baseMenu:infoById'),
       (240902143348004955, 0, '2024-09-02 14:33:49', 0, '2024-09-02 14:33:49', 1, '',
        '基础:菜单:下拉列表', 'baseMenu:dictList'),
       (240902143451004956, 0, '2024-09-02 14:34:51', 0, '2024-09-02 14:34:51', 1, '',
        '基础:角色:新增修改', 'baseRole:insertOrUpdate'),
       (240902143451004957, 0, '2024-09-02 14:34:51', 0, '2024-09-02 14:34:51', 1, '',
        '基础:角色:列表查询', 'baseRole:page'),
       (240902143451004958, 0, '2024-09-02 14:34:51', 0, '2024-09-02 14:34:51', 1, '',
        '基础:角色:删除', 'baseRole:deleteByIdSet'),
       (240902143451004959, 0, '2024-09-02 14:34:52', 0, '2024-09-02 14:34:52', 1, '',
        '基础:角色:查看详情', 'baseRole:infoById'),
       (240902143452004960, 0, '2024-09-02 14:34:52', 0, '2024-09-02 14:34:52', 1, '',
        '基础:角色:下拉列表', 'baseRole:dictList'),
       (240902143513004961, 0, '2024-09-02 14:35:13', 0, '2024-09-02 14:35:13', 1, '',
        '基础:权限:新增修改', 'baseAuth:insertOrUpdate'),
       (240902143513004962, 0, '2024-09-02 14:35:13', 0, '2024-09-02 14:35:13', 1, '',
        '基础:权限:列表查询', 'baseAuth:page'),
       (240902143513004963, 0, '2024-09-02 14:35:14', 0, '2024-09-02 14:35:14', 1, '',
        '基础:权限:删除', 'baseAuth:deleteByIdSet'),
       (240902143513004964, 0, '2024-09-02 14:35:14', 0, '2024-09-02 14:35:14', 1, '',
        '基础:权限:查看详情', 'baseAuth:infoById'),
       (240902143514004965, 0, '2024-09-02 14:35:14', 0, '2024-09-02 14:35:14', 1, '',
        '基础:权限:下拉列表', 'baseAuth:dictList'),
       (240902143532004966, 0, '2024-09-02 14:35:33', 0, '2024-09-02 14:35:33', 1, '',
        '基础:用户:新增修改', 'baseUser:insertOrUpdate'),
       (240902143532004967, 0, '2024-09-02 14:35:33', 0, '2024-09-02 14:35:33', 1, '',
        '基础:用户:列表查询', 'baseUser:page'),
       (240902143533004968, 0, '2024-09-02 14:35:33', 0, '2024-09-02 14:35:33', 1, '',
        '基础:用户:删除', 'baseUser:deleteByIdSet'),
       (240902143533004969, 0, '2024-09-02 14:35:33', 0, '2024-09-02 14:35:33', 1, '',
        '基础:用户:查看详情', 'baseUser:infoById'),
       (240902143533004970, 0, '2024-09-02 14:35:33', 0, '2024-09-02 17:04:46', 1, '',
        '基础:用户:下拉列表', 'baseUser:dictList'),
       (241015142748002311, 0, '2024-10-15 14:27:48', 0, '2024-10-15 14:28:17', 1, '',
        '基础:参数:新增修改', 'baseParam:insertOrUpdate'),
       (241015142749002312, 0, '2024-10-15 14:27:49', 0, '2024-10-15 14:28:42', 1, '',
        '基础:参数:列表查询', 'baseParam:page'),
       (241015142749002313, 0, '2024-10-15 14:27:50', 0, '2024-10-15 14:28:37', 1, '',
        '基础:参数:删除', 'baseParam:deleteByIdSet'),
       (241015142750002314, 0, '2024-10-15 14:27:50', 0, '2024-10-15 14:28:30', 1, '',
        '基础:参数:查看详情', 'baseParam:infoById'),
       (241015142751002315, 0, '2024-10-15 14:27:51', 0, '2024-10-15 14:28:24', 1, '',
        '基础:参数:下拉列表', 'baseParam:dictList'),
       (241015142855002316, 0, '2024-10-15 14:28:56', 0, '2024-10-15 14:28:56', 1, '',
        '基础:字典:新增修改', 'baseDict:insertOrUpdate'),
       (241015142855002317, 0, '2024-10-15 14:28:56', 0, '2024-10-15 14:28:56', 1, '',
        '基础:字典:列表查询', 'baseDict:page'),
       (241015142856002318, 0, '2024-10-15 14:28:57', 0, '2024-10-15 14:28:57', 1, '',
        '基础:字典:删除', 'baseDict:deleteByIdSet'),
       (241015142857002319, 0, '2024-10-15 14:28:57', 0, '2024-10-15 14:28:57', 1, '',
        '基础:字典:查看详情', 'baseDict:infoById'),
       (241015142857002320, 0, '2024-10-15 14:28:58', 0, '2024-10-15 14:28:58', 1, '',
        '基础:字典:下拉列表', 'baseDict:dictList'),
       (241015143600002321, 0, '2024-10-15 14:36:00', 0, '2024-10-15 14:36:00', 1, '',
        '基础:区域:新增修改', 'baseArea:insertOrUpdate'),
       (241015143600002322, 0, '2024-10-15 14:36:01', 0, '2024-10-15 14:36:01', 1, '',
        '基础:区域:列表查询', 'baseArea:page'),
       (241015143601002323, 0, '2024-10-15 14:36:01', 0, '2024-10-15 14:36:01', 1, '',
        '基础:区域:删除', 'baseArea:deleteByIdSet'),
       (241015143602002324, 0, '2024-10-15 14:36:02', 0, '2024-10-15 14:36:02', 1, '',
        '基础:区域:查看详情', 'baseArea:infoById'),
       (241015143602002325, 0, '2024-10-15 14:36:03', 0, '2024-10-15 14:36:03', 1, '',
        '基础:区域:下拉列表', 'baseArea:dictList'),
       (241015143619002326, 0, '2024-10-15 14:36:19', 0, '2024-10-15 14:36:19', 1, '',
        '基础:部门:新增修改', 'baseDept:insertOrUpdate'),
       (241015143619002327, 0, '2024-10-15 14:36:20', 0, '2024-10-15 14:36:20', 1, '',
        '基础:部门:列表查询', 'baseDept:page'),
       (241015143620002328, 0, '2024-10-15 14:36:21', 0, '2024-10-15 14:36:21', 1, '',
        '基础:部门:删除', 'baseDept:deleteByIdSet'),
       (241015143621002329, 0, '2024-10-15 14:36:21', 0, '2024-10-15 14:36:21', 1, '',
        '基础:部门:查看详情', 'baseDept:infoById'),
       (241015143621002330, 0, '2024-10-15 14:36:22', 0, '2024-10-15 14:36:22', 1, '',
        '基础:部门:下拉列表', 'baseDept:dictList'),
       (241015143631002331, 0, '2024-10-15 14:36:31', 0, '2024-10-15 14:36:31', 1, '',
        '基础:岗位:新增修改', 'basePost:insertOrUpdate'),
       (241015143632002332, 0, '2024-10-15 14:36:32', 0, '2024-10-15 14:36:32', 1, '',
        '基础:岗位:列表查询', 'basePost:page'),
       (241015143632002333, 0, '2024-10-15 14:36:33', 0, '2024-10-15 14:36:33', 1, '',
        '基础:岗位:删除', 'basePost:deleteByIdSet'),
       (241015143633002334, 0, '2024-10-15 14:36:34', 0, '2024-10-15 14:36:34', 1, '',
        '基础:岗位:查看详情', 'basePost:infoById'),
       (241015143634002335, 0, '2024-10-15 14:36:34', 0, '2024-10-15 14:36:34', 1, '',
        '基础:岗位:下拉列表', 'basePost:dictList'),
       (241125092606001883, 240902154357005281, '2024-11-25 09:26:06', 240902154357005281,
        '2024-11-25 09:26:06', 1, '', '基础:请求:列表查询', 'baseRequest:page'),
       (241125092636001887, 240902154357005281, '2024-11-25 09:26:37', 240902154357005281,
        '2024-11-25 09:26:43', 1, '', '基础:请求详情:查看详情', 'baseRequestInfo:infoById'),
       (250915080423066977, 0, '2025-09-15 08:04:24', 0, '2025-09-15 08:04:24', 1, '',
        '基础:角色:身份:管理员', 'baseRoleKey:admin');
/*!40000 ALTER TABLE `base_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_live_room_user`
--

LOCK TABLES `base_live_room_user` WRITE;
/*!40000 ALTER TABLE `base_live_room_user`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_live_room_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_session_content`
--

LOCK TABLES `base_im_session_content` WRITE;
/*!40000 ALTER TABLE `base_im_session_content`
    DISABLE KEYS */;
INSERT INTO `base_im_session_content`
VALUES (250428084957020324, 0, '2025-04-28 08:49:57', 1, 250427140812019548, '0', 101,
        1745801384040, -1, 0),
       (250428085138020328, 0, '2025-04-28 08:51:38', 1, 250427140812019548, '1', 101,
        1745801384040, -1, 0),
       (250428093155020550, 0, '2025-04-28 09:31:55', 1, 250427140812019548, '2', 101,
        1745801384040, -1, 0),
       (250428094508020592, 240902154357005281, '2025-04-28 09:45:09', 1, 250427140812019548, '3',
        101, 1745801384040, -1, 0),
       (250428094522020597, 240902154357005281, '2025-04-28 09:45:22', 1, 250427140812019548, '4',
        101, 1745801384040, -1, 0),
       (250428101607020767, 240902154357005281, '2025-04-28 10:16:08', 1, 250427140812019548, '5',
        101, 1745801384040, -1, 0),
       (250428110114020928, 240902154357005281, '2025-04-28 11:01:15', 1, 250428105947020916, '6',
        101, 1745801384040, -1, 0),
       (250428110148020935, 0, '2025-04-28 11:01:48', 1, 250428105947020916, '7', 101,
        1745801384040, -1, 0),
       (250428110149020939, 0, '2025-04-28 11:01:49', 1, 250428105947020916, '7', 101,
        1745801384040, -1, 0),
       (250428110228020946, 0, '2025-04-28 11:02:29', 1, 250428105947020916, '8', 101,
        1745801384040, -1, 0),
       (250428110353020963, 240902154357005281, '2025-04-28 11:03:53', 1, 250428105947020916, '8',
        101, 1745801384040, -1, 0);
/*!40000 ALTER TABLE `base_im_session_content`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_apply_friend`
--

LOCK TABLES `base_im_apply_friend` WRITE;
/*!40000 ALTER TABLE `base_im_apply_friend`
    DISABLE KEYS */;
INSERT INTO `base_im_apply_friend`
VALUES (250428105627020894, 0, 240902154357005281, 201, '', '2025-04-28 11:03:40', '申请内容2',
        250428105947020916, '2025-04-28 10:56:27');
/*!40000 ALTER TABLE `base_im_apply_friend`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_email_configuration`
--

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
-- Dumping data for table `base_user_bank_card`
--

LOCK TABLES `base_user_bank_card` WRITE;
/*!40000 ALTER TABLE `base_user_bank_card`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_user_bank_card`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    `token`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '调用 api时，传递的 token，格式：uuid',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '令牌名',
    `last_use_time` datetime                                                      NOT NULL COMMENT '最近使用时间',
    `enable_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：令牌表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_api_token`
--

LOCK TABLES `base_api_token` WRITE;
/*!40000 ALTER TABLE `base_api_token`
    DISABLE KEYS */;
INSERT INTO `base_api_token`
VALUES (241125170023002915, 0, '2024-11-25 17:00:23', '2024-11-25 17:00:23',
        '6ac6006ec7274462968c725095da3097', '测试20241125165714', '2024-11-25 17:00:23', 1);
/*!40000 ALTER TABLE `base_api_token`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_dict`
--

LOCK TABLES `base_dict` WRITE;
/*!40000 ALTER TABLE `base_dict`
    DISABLE KEYS */;
INSERT INTO `base_dict`
VALUES (240924152139000555, 0, '2024-09-24 15:21:39', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictName', 1, -1, 10000, '569fb80fe8724348badf393947dfbd18'),
       (240924152155000556, 0, '2024-09-24 15:21:56', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem1', 2, 1, 10000, 'a27e31bf77f249a5af6adf93b77b48e7'),
       (240924152320000557, 0, '2024-09-24 15:23:20', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem2', 2, 2, 9900, '316d838f1145454ebc4518ce0b3c503a'),
       (240924152400000558, 0, '2024-09-24 15:24:01', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem3', 2, 3, 9800, '9686ef6b5e344c0596147468e816a819');
/*!40000 ALTER TABLE `base_dict`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_profit_share`
--

LOCK TABLES `base_profit_share` WRITE;
/*!40000 ALTER TABLE `base_profit_share`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_profit_share`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：群组表，用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_im_group_ref_user`
--

LOCK TABLES `base_im_group_ref_user` WRITE;
/*!40000 ALTER TABLE `base_im_group_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_im_group_ref_user`
VALUES (250427140812019550, 0, '2025-04-27 14:08:13', 0, '2025-04-27 14:08:13', 250427140812019547,
        0, '', 0),
       (250428092408020545, 0, '2025-04-28 09:24:09', 0, '2025-04-28 09:24:09', 250428090536020427,
        0, '', 0),
       (250428094455020589, 0, '2025-04-28 09:44:56', 0, '2025-04-28 09:44:56', 250427140812019547,
        240902154357005281, '', 0);
/*!40000 ALTER TABLE `base_im_group_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_post_ref_user`
--

LOCK TABLES `base_post_ref_user` WRITE;
/*!40000 ALTER TABLE `base_post_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_post_ref_user`
VALUES (240912142923006178, 240902163618005502);
/*!40000 ALTER TABLE `base_post_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_user_info`
--

DROP TABLE IF EXISTS `base_user_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_info`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '该用户的 uuid，本系统目前只有即时聊天使用该字段，备注：不能重复',
    `nickname`         varchar(50)                                                   NOT NULL COMMENT '昵称',
    `bio`              varchar(100)                                                  NOT NULL COMMENT '个人简介',
    `avatar_file_id`   bigint                                                        NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `sign_up_type`     int                                                           NOT NULL COMMENT '注册终端',
    `last_active_time` datetime                                                      NOT NULL COMMENT '最近活跃时间',
    `last_ip`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最近 ip',
    `last_region`      varchar(200)                                                  NOT NULL COMMENT '最近 ip所处区域',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：用户基本信息表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_user_info`
--

LOCK TABLES `base_user_info` WRITE;
/*!40000 ALTER TABLE `base_user_info`
    DISABLE KEYS */;
INSERT INTO `base_user_info`
VALUES (0, 'admin', 'admin', '666', -1, 101, '2025-10-22 14:15:19', '0:0:0:0:0:0:0:1',
        'ipv6#0:0:0:0:0:0:0:1'),
       (240902154357005281, '36870faf0934422399872a0260c1596a', 'nickname_kar1', 'kar1_bio1',
        241122093038000730, 101, '2025-09-22 16:32:49', '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1'),
       (240902163618005502, '91a518a5234249a8a312bbc8fb6571c2', 'nickname_kar2', '', -1, 101,
        '2025-09-22 16:17:47', '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1');
/*!40000 ALTER TABLE `base_user_info`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_role_ref_user`
--

LOCK TABLES `base_role_ref_user` WRITE;
/*!40000 ALTER TABLE `base_role_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_role_ref_user`
VALUES (240830160154004401, 240902154357005281);
/*!40000 ALTER TABLE `base_role_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：岗位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_post`
--

LOCK TABLES `base_post` WRITE;
/*!40000 ALTER TABLE `base_post`
    DISABLE KEYS */;
INSERT INTO `base_post`
VALUES (240912142923006178, 0, '2024-09-12 14:29:24', 0, '2024-09-12 14:35:22', 1, '', '董事长', 0,
        10000, 'd6a99286846f4f80b54e3a0850fa5871'),
       (240912142941006179, 0, '2024-09-12 14:29:41', 0, '2024-09-12 14:32:17', 1, '', '技术总监',
        240912143207006180, 9900, 'e4ea09a6368041039ba770f3e85c40d6'),
       (240912143207006180, 0, '2024-09-12 14:32:07', 0, '2024-09-12 14:32:07', 1, '', '总经理',
        240912142923006178, 10000, '805aabfbf98f4eb8a68a4ca330dbac11'),
       (240912143236006181, 0, '2024-09-12 14:32:37', 0, '2024-09-12 14:32:37', 1, '', '财务总监',
        240912143207006180, 9800, '6d9539541128475a866d18848e2507cf'),
       (240912143418006182, 0, '2024-09-12 14:34:18', 0, '2024-09-12 14:34:18', 1, '', '架构师',
        240912142941006179, 10000, 'a2951a71f3c241b880d9b9c58e8fe7b1'),
       (240912143431006183, 0, '2024-09-12 14:34:32', 0, '2024-09-12 14:34:32', 1, '',
        '实习java开发', 240912143418006182, 10000, '099090ecff7448b1ad80bcc010259c7d'),
       (240912143442006184, 0, '2024-09-12 14:34:43', 0, '2024-09-12 14:34:43', 1, '',
        '初级java开发', 240912143418006182, 9900, 'ceff63789e684e95a0ab6a822b9045e9'),
       (240912143451006185, 0, '2024-09-12 14:34:52', 0, '2024-09-12 14:34:52', 1, '',
        '中级java开发', 240912143418006182, 9800, '4b4664ada30f47a1ad93abfd3170b080'),
       (240912143501006186, 0, '2024-09-12 14:35:02', 0, '2024-09-12 14:35:02', 1, '',
        '高级java开发', 240912143418006182, 9700, '7f46aa5721924d6a8c6ddf9f3b70eb8a'),
       (240912143538006187, 0, '2024-09-12 14:35:39', 0, '2024-09-12 14:35:39', 1, '', '副总经理',
        240912143207006180, 10000, '0b9f4d3f3e514981ae15adfdfabe0752'),
       (240912143648006188, 0, '2024-09-12 14:36:48', 0, '2024-09-12 14:36:48', 1, '', '运维组长',
        240912142941006179, 9900, 'b0b3424a25334dee90efe3e3b9c49bf2'),
       (240912143703006189, 0, '2024-09-12 14:37:04', 0, '2024-09-12 14:37:04', 1, '', '实习运维',
        240912143648006188, 10000, '5de3b49c33bf4a048d38953cb6864b1d'),
       (240912143711006190, 0, '2024-09-12 14:37:11', 0, '2024-09-12 14:37:11', 1, '', '初级运维',
        240912143648006188, 9900, '2799598daa984f6cb18637eed56c9e8f'),
       (240912143718006191, 0, '2024-09-12 14:37:19', 0, '2024-09-12 14:37:19', 1, '', '中级运维',
        240912143648006188, 9800, '156a1134eebf4f00a00d9084de49dd29'),
       (240912143724006192, 0, '2024-09-12 14:37:25', 0, '2024-09-12 14:37:25', 1, '', '高级运维',
        240912143648006188, 9700, '83e65a53731249fea3d62e68b836eace');
/*!40000 ALTER TABLE `base_post`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_search_history`
--

LOCK TABLES `base_im_search_history` WRITE;
/*!40000 ALTER TABLE `base_im_search_history`
    DISABLE KEYS */;
INSERT INTO `base_im_search_history`
VALUES (251022100548073195, '2025-10-22 10:05:48', 0, '444444444'),
       (251022100703073210, '2025-10-22 10:07:03', 0, '111122'),
       (251022100708073212, '2025-10-22 10:07:08', 0, '111122333'),
       (251022100842073220, '2025-10-22 10:08:42', 0, '3333333'),
       (251022100844073222, '2025-10-22 10:08:44', 0, '4444444'),
       (251022100845073224, '2025-10-22 10:08:46', 0, '55555555'),
       (251022100849073229, '2025-10-22 10:08:50', 0, '6666666'),
       (251022100858073232, '2025-10-22 10:08:59', 0, 'aaaaaaaaaa'),
       (251022100904073234, '2025-10-22 10:09:05', 0, 'bbbbbbbbbbb'),
       (251022100908073236, '2025-10-22 10:09:09', 0, 'zzzzzzzz'),
       (251022100910073238, '2025-10-22 10:09:11', 0, 'xxxxxxxxxxx'),
       (251022100911073240, '2025-10-22 10:09:12', 0, 'cccccccccc'),
       (251022100913073242, '2025-10-22 10:09:14', 0, 'vvvvvvvvv'),
       (251022100915073244, '2025-10-22 10:09:15', 0, 'bbbbbbb'),
       (251022100917073246, '2025-10-22 10:09:17', 0, 'nnnnnnnnn'),
       (251022100918073248, '2025-10-22 10:09:19', 0, 'mmmmmmmmmm'),
       (251022100920073250, '2025-10-22 10:09:21', 0, 'aaaaaaa'),
       (251022100922073254, '2025-10-22 10:09:22', 0, 'ssssssss'),
       (251022100924073256, '2025-10-22 10:09:24', 0, 'ddddddddd'),
       (251022100925073258, '2025-10-22 10:09:25', 0, 'fffffffff'),
       (251022100927073260, '2025-10-22 10:09:28', 0, 'ggggggg'),
       (251022100929073262, '2025-10-22 10:09:30', 0, 'hhhhhh'),
       (251022100931073264, '2025-10-22 10:09:31', 0, 'j'),
       (251022100934073266, '2025-10-22 10:09:34', 0, 'kkkkkkkkkkkk'),
       (251022100936073268, '2025-10-22 10:09:37', 0, 'llllllllllll'),
       (251022101332073305, '2025-10-22 10:13:33', 0,
        '111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111'),
       (251022102246073432, '2025-10-22 10:22:46', 0, '555555'),
       (251022102436073466, '2025-10-22 10:24:36', 0, 'admin'),
       (251022102449073470, '2025-10-22 10:24:50', 0, '555'),
       (251022102539073490, '2025-10-22 10:25:40', 0, '1233333333333'),
       (251022102557073503, '2025-10-22 10:25:57', 0, '5'),
       (251022102837073524, '2025-10-22 10:28:38', 0, '11111'),
       (251022103219073549, '2025-10-22 10:32:19', 0, '12'),
       (251022103224073551, '2025-10-22 10:32:24', 0, '1222222'),
       (251022103226073553, '2025-10-22 10:32:26', 0, '12222223'),
       (251022103228073555, '2025-10-22 10:32:29', 0, '122222233333'),
       (251022103231073557, '2025-10-22 10:32:32', 0, '1222222333334'),
       (251022103338073575, '2025-10-22 10:33:38', 0, '111'),
       (251022103341073577, '2025-10-22 10:33:41', 0, '11122222'),
       (251022103344073579, '2025-10-22 10:33:44', 0, '1112222233333333'),
       (251022103351073584, '2025-10-22 10:33:51', 0, '4444'),
       (251022104629073699, '2025-10-22 10:46:30', 0, '11'),
       (251022104713073731, '2025-10-22 10:47:14', 0, '1'),
       (251022104724073748, '2025-10-22 10:47:24', 0, '123123'),
       (251022104725073750, '2025-10-22 10:47:25', 0, '123123123'),
       (251022104726073752, '2025-10-22 10:47:27', 0, '1231231231'),
       (251022104727073754, '2025-10-22 10:47:27', 0, '12312312313'),
       (251022104727073756, '2025-10-22 10:47:28', 0, '12312312313123'),
       (251022104728073758, '2025-10-22 10:47:29', 0, '123123123131231'),
       (251022104729073760, '2025-10-22 10:47:29', 0, '123123123131231111111'),
       (251022105249073810, '2025-10-22 10:52:49', 0, '666'),
       (251022105417073842, '2025-10-22 10:54:17', 0, '测试1111'),
       (251022105755073873, '2025-10-22 10:57:56', 0, '123'),
       (251022105805073875, '2025-10-22 10:58:06', 0, '测试'),
       (251022111848073911, '2025-10-22 11:18:48', 0, 'kar');
/*!40000 ALTER TABLE `base_im_search_history`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_user_configuration`
--

LOCK TABLES `base_user_configuration` WRITE;
/*!40000 ALTER TABLE `base_user_configuration`
    DISABLE KEYS */;
INSERT INTO `base_user_configuration`
VALUES (1, 1, 1, 1, 1, 1);
/*!40000 ALTER TABLE `base_user_configuration`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    `status`          int                                                           NOT NULL COMMENT '状态：101 申请中 201 已通过 301 已拒绝',
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
-- Dumping data for table `base_im_apply_group`
--

LOCK TABLES `base_im_apply_group` WRITE;
/*!40000 ALTER TABLE `base_im_apply_group`
    DISABLE KEYS */;
INSERT INTO `base_im_apply_group`
VALUES (250428090536020427, 0, 250427140812019547, 201, '', '2025-04-28 09:05:36', '申请内容',
        250427140812019548, '2025-04-28 09:05:36'),
       (250428093242020554, 240902154357005281, 250427140812019547, 201, '', '2025-04-28 09:44:52',
        '申请内容3', 250427140812019548, '2025-04-28 09:32:42');
/*!40000 ALTER TABLE `base_im_apply_group`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_session_ref_user`
--

LOCK TABLES `base_im_session_ref_user` WRITE;
/*!40000 ALTER TABLE `base_im_session_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_im_session_ref_user`
VALUES (250427140812019549, 250427140812019548, 0, 1745734092949, 1, '', '', 250427140812019547,
        201, '测试群1', 0),
       (250427143204019771, 250427143204019770, 0, 1745735524674, 0, '', '', 250427143204019769,
        201, '测试群1', 0),
       (250428094116020581, 250427140812019548, 240902154357005281, 1745804476548, 1, '', '',
        250427140812019547, 201, '测试群1', 0),
       (250428105947020919, 250428105947020916, 0, 1745809187750, 1, '', '', 240902154357005281,
        101, 'nickname_kar1', 0),
       (250428105947020920, 250428105947020916, 240902154357005281, 1745809187750, 1, '', '', 0,
        101, 'admin', 0);
/*!40000 ALTER TABLE `base_im_session_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_pay`
--

LOCK TABLES `base_pay` WRITE;
/*!40000 ALTER TABLE `base_pay`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_pay`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_third_app`
--

LOCK TABLES `base_third_app` WRITE;
/*!40000 ALTER TABLE `base_third_app`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_third_app`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：文件存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_file_storage_configuration`
--

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
-- Dumping data for table `base_sql_slow`
--

--
-- Table structure for table `base_user_wallet`
--

DROP TABLE IF EXISTS `base_user_wallet`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_wallet`
(
    `id`                         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `create_id`                  bigint                                                        NOT NULL,
    `create_time`                datetime                                                      NOT NULL,
    `update_id`                  bigint                                                        NOT NULL,
    `update_time`                datetime                                                      NOT NULL,
    `enable_flag`                tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `remark`                     varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `withdrawable_money`         decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱',
    `withdrawable_pre_use_money` decimal(13, 3)                                                NOT NULL COMMENT '可提现的钱：预使用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：子表：用户钱包表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_user_wallet`
--

LOCK TABLES `base_user_wallet` WRITE;
/*!40000 ALTER TABLE `base_user_wallet`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_user_wallet`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_im_block`
--

DROP TABLE IF EXISTS `base_im_block`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_im_block`
(
    `id`          bigint   NOT NULL,
    `create_id`   bigint   NOT NULL,
    `create_time` datetime NOT NULL,
    `user_id`     bigint   NOT NULL COMMENT '用户主键 id',
    `session_id`  bigint   NOT NULL COMMENT '会话主键 id，默认为 -1',
    `source_id`   bigint   NOT NULL COMMENT '来源 id',
    `source_type` int      NOT NULL COMMENT '来源类型：101 好友 201 群组',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：拉黑表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_im_block`
--

LOCK TABLES `base_im_block` WRITE;
/*!40000 ALTER TABLE `base_im_block`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_im_block`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_live_room`
--

LOCK TABLES `base_live_room` WRITE;
/*!40000 ALTER TABLE `base_live_room`
    DISABLE KEYS */;
INSERT INTO `base_live_room`
VALUES (250506160235021405, 0, '测试房间1', '2025-05-06 16:02:35', '841891');
/*!40000 ALTER TABLE `base_live_room`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_live_room_replay`
--

LOCK TABLES `base_live_room_replay` WRITE;
/*!40000 ALTER TABLE `base_live_room_replay`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_live_room_replay`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_im_session_content_ref_user`
--

LOCK TABLES `base_im_session_content_ref_user` WRITE;
/*!40000 ALTER TABLE `base_im_session_content_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_im_session_content_ref_user`
VALUES (250428084957020325, 250427140812019548, 250428084957020324, 0, 1),
       (250428085138020329, 250427140812019548, 250428085138020328, 0, 1),
       (250428093155020551, 250427140812019548, 250428093155020550, 0, 1),
       (250428094508020593, 250427140812019548, 250428094508020592, 0, 1),
       (250428094508020594, 250427140812019548, 250428094508020592, 240902154357005281, 1),
       (250428094522020598, 250427140812019548, 250428094522020597, 0, 1),
       (250428094522020599, 250427140812019548, 250428094522020597, 240902154357005281, 1),
       (250428101607020768, 250427140812019548, 250428101607020767, 0, 1),
       (250428101607020769, 250427140812019548, 250428101607020767, 240902154357005281, 1),
       (250428110114020929, 250428105947020916, 250428110114020928, 0, 1),
       (250428110114020930, 250428105947020916, 250428110114020928, 240902154357005281, 1),
       (250428110148020936, 250428105947020916, 250428110148020935, 0, 1),
       (250428110148020937, 250428105947020916, 250428110148020935, 240902154357005281, 1),
       (250428110149020940, 250428105947020916, 250428110149020939, 0, 1),
       (250428110149020941, 250428105947020916, 250428110149020939, 240902154357005281, 1),
       (250428110228020947, 250428105947020916, 250428110228020946, 0, 1),
       (250428110228020948, 250428105947020916, 250428110228020946, 240902154357005281, 1),
       (250428110353020964, 250428105947020916, 250428110353020963, 0, 1),
       (250428110353020965, 250428105947020916, 250428110353020963, 240902154357005281, 1);
/*!40000 ALTER TABLE `base_im_session_content_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `base_dept`
--

LOCK TABLES `base_dept` WRITE;
/*!40000 ALTER TABLE `base_dept`
    DISABLE KEYS */;
INSERT INTO `base_dept`
VALUES (240912141649006163, 0, '2024-09-12 14:16:49', 0, '2024-09-12 14:16:55', 1, '', '总部', 0,
        10000, 'ec1deda0ce664917840099b32c93bdbb'),
       (240912141714006164, 0, '2024-09-12 14:17:15', 0, '2024-09-12 14:17:51', 1, '', '开发部',
        240912141649006163, 10000, '8f5b5f6033964483b0aa5700c3ff5d4f'),
       (240912141722006165, 0, '2024-09-12 14:17:23', 0, '2024-09-12 14:17:55', 1, '', '测试部',
        240912141649006163, 9900, 'ec0b2769f1c84affa447f69d2cdb6c99'),
       (240912141729006166, 0, '2024-09-12 14:17:30', 0, '2024-09-12 14:17:57', 1, '', '运维部',
        240912141649006163, 9800, 'd783c4648138445b9d52febfec8b76dd'),
       (240912141822006167, 0, '2024-09-12 14:18:23', 0, '2024-09-12 14:18:23', 1, '', '地球开发部',
        240912141714006164, 10000, 'caae0135e7ad4cefa9dfe6f170b231b4'),
       (240912141831006168, 0, '2024-09-12 14:18:32', 0, '2024-09-12 14:18:32', 1, '', '中国开发部',
        240912141822006167, 10000, 'ab34925f044e4cd6a93177454ebe251d'),
       (240912141840006169, 0, '2024-09-12 14:18:41', 0, '2024-09-12 14:21:12', 1, '', '美国开发部',
        240912141822006167, 9900, '08adc670a1954e16ac1874206c1f7ee6'),
       (240912141903006170, 0, '2024-09-12 14:19:04', 0, '2024-09-12 14:19:04', 1, '', '北京开发部',
        240912141831006168, 10000, 'ecc3e6ac95644bb1a516e42f89f8c783'),
       (240912141910006171, 0, '2024-09-12 14:19:11', 0, '2024-09-12 14:19:11', 1, '', '上海开发部',
        240912141831006168, 9900, 'd2a10744f27e4f1295e057d52b719263'),
       (240912141918006172, 0, '2024-09-12 14:19:18', 0, '2024-09-12 14:19:18', 1, '', '广州开发部',
        240912141831006168, 9800, '9c48634392994d3597903a9268d527a2'),
       (240912141925006173, 0, '2024-09-12 14:19:26', 0, '2024-09-12 14:19:26', 1, '', '深圳开发部',
        240912141831006168, 9700, '1594010b9c694a38abb9f2a26f2c764e'),
       (240912141945006174, 0, '2024-09-12 14:19:46', 0, '2024-09-12 14:20:54', 1, '', '纽约开发部',
        240912141840006169, 10000, 'f8c798d821e04adba1ba935ed15a6ca0'),
       (240912141954006175, 0, '2024-09-12 14:19:55', 0, '2024-09-12 14:20:54', 1, '',
        '洛杉矶开发部', 240912141840006169, 9900, 'e6b12874fc3144c0920b1513d191304e'),
       (240912142001006176, 0, '2024-09-12 14:20:02', 0, '2024-09-12 14:20:54', 1, '',
        '芝加哥开发部', 240912141840006169, 9800, '3f141a23e1284d73b6a0e4df2613dc02'),
       (240912142008006177, 0, '2024-09-12 14:20:08', 0, '2024-09-12 14:20:54', 1, '',
        '华盛顿开发部', 240912141840006169, 9700, 'eb032d1093b6457888844dbbf4ab5cea');
/*!40000 ALTER TABLE `base_dept`
    ENABLE KEYS */;
UNLOCK TABLES;


CREATE TABLE `base_request`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `uri`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的uri',
    `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
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


CREATE TABLE `base_request_info`
(
    `id`             bigint                                                        NOT NULL,
    `uri`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的uri',
    `cost_ms`        bigint                                                        NOT NULL COMMENT '耗时（毫秒）',
    `success_flag`   tinyint(1)                                                    NOT NULL COMMENT '请求是否成功',
    `error_msg`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '失败信息',
    `request_param`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '请求的参数',
    `response_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '请求返回的值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：请求详情表，主表：请求表';

/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2025-10-22 14:15:41
