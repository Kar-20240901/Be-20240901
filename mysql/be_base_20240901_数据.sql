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
        '基础:角色:身份:管理员', 'baseRoleKey:admin'),
       (251212143447111072, 0, '2025-12-12 14:34:48', 0, '2025-12-12 14:34:48', 1, '',
        '基础:用户删除日志:列表查询', 'baseUserDeleteLog:page'),
       (251212143448111074, 0, '2025-12-12 14:34:48', 0, '2025-12-12 14:34:48', 1, '',
        '基础:用户删除日志:删除', 'baseUserDeleteLog:deleteByIdSet'),
       (251212143448111076, 0, '2025-12-12 14:34:48', 0, '2025-12-12 14:34:48', 1, '',
        '基础:用户删除日志:查看详情', 'baseUserDeleteLog:infoById'),
       (251212143535111087, 0, '2025-12-12 14:35:35', 0, '2025-12-12 14:35:35', 1, '',
        '基础:令牌:新增修改', 'baseApiToken:insertOrUpdate'),
       (251212143535111089, 0, '2025-12-12 14:35:36', 0, '2025-12-12 14:35:36', 1, '',
        '基础:令牌:列表查询', 'baseApiToken:page'),
       (251212143536111091, 0, '2025-12-12 14:35:36', 0, '2025-12-12 14:35:36', 1, '',
        '基础:令牌:删除', 'baseApiToken:deleteByIdSet'),
       (251212143536111093, 0, '2025-12-12 14:35:36', 0, '2025-12-12 14:35:36', 1, '',
        '基础:令牌:查看详情', 'baseApiToken:infoById'),
       (251223143927130929, 0, '2025-12-23 14:39:28', 0, '2025-12-23 14:40:50', 1, '',
        '基础:文件:传输:列表查询', 'baseFileTransfer:page'),
       (251223143928130931, 0, '2025-12-23 14:39:28', 0, '2025-12-23 14:40:42', 1, '',
        '基础:文件:传输:删除', 'baseFileTransfer:deleteByIdSet'),
       (251223143928130933, 0, '2025-12-23 14:39:29', 0, '2025-12-23 14:40:55', 1, '',
        '基础:文件:传输:查看详情', 'baseFileTransfer:infoById');
/*!40000 ALTER TABLE `base_auth`
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
-- Dumping data for table `base_user_info`
--

LOCK TABLES `base_user_info` WRITE;
/*!40000 ALTER TABLE `base_user_info`
    DISABLE KEYS */;
INSERT INTO `base_user_info`
VALUES (0, 'adminuuid', 'admin', '666', 260213091343191592, 101, '2026-03-11 11:14:27',
        '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1');
/*!40000 ALTER TABLE `base_user_info`
    ENABLE KEYS */;
UNLOCK TABLES;

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
       (240830160154004401, 250915080423066977),
       (240830160154004401, 251212143447111072),
       (240830160647004402, 251212143447111072),
       (240830160154004401, 251212143448111074),
       (240830160154004401, 251212143448111076),
       (240830160154004401, 251212143535111087),
       (240830160647004402, 251212143535111087),
       (240830160154004401, 251212143535111089),
       (240830160647004402, 251212143535111089),
       (240830160154004401, 251212143536111091),
       (240830160647004402, 251212143536111091),
       (240830160154004401, 251212143536111093),
       (240830160647004402, 251212143536111093),
       (240830160154004401, 251223143927130929),
       (240830160647004402, 251223143927130929),
       (240830160154004401, 251223143928130931),
       (240830160647004402, 251223143928130931),
       (240830160154004401, 251223143928130933),
       (240830160647004402, 251223143928130933);
/*!40000 ALTER TABLE `base_role_ref_auth`
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
       (241125110642002342, 240902154357005281, '2024-11-25 11:06:43', 0, '2025-12-19 15:27:23', 1,
        '', '文件存储配置', '/file/fileStorageConfiguration', 'ri:folder-settings-fill',
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
/*!40000 ALTER TABLE `base_dept_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
VALUES (240830160154004401, 0, '2024-08-30 16:01:55', 0, '2025-12-12 14:37:54', 1, '', '管理员',
        'admin', 0),
       (240830160647004402, 0, '2024-08-30 16:06:48', 0, '2025-12-23 11:08:49', 1, '',
        '默认角色（不给用户赋值都可生效）', 'default', 1);
/*!40000 ALTER TABLE `base_role`
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
VALUES (0, 0, '2024-09-12 09:59:59', 0, '2025-12-12 14:29:37', 1, '',
        '51791182cd33494e8c8172469639b6d1/2ec129bd2d5914753d2dcee2fb2fc781bb65ef907e831435d2fec9f0afd7481971be9840167bb839b834c0d8ba536420515d39fcaf4144ba9a86c70c783f7c6',
        '', 'admin', '', '', '', '');
/*!40000 ALTER TABLE `base_user`
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
/*!40000 ALTER TABLE `base_post_ref_user`
    ENABLE KEYS */;
UNLOCK TABLES;

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
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2026-03-18 10:50:07
