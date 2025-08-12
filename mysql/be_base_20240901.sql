-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: karopendev.top    Database: be_base_20240901
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
-- Table structure for table `base_user_info_1`
--

DROP TABLE IF EXISTS `base_user_info_1`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_info_1`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不能重复',
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
-- Dumping data for table `base_user_info_1`
--

LOCK TABLES `base_user_info_1` WRITE;
/*!40000 ALTER TABLE `base_user_info_1`
    DISABLE KEYS */;
INSERT INTO `base_user_info_1`
VALUES (240902154357005281, '36870faf0934422399872a0260c1596a', 'nickname_kar1', 'kar1_bio1',
        241122093038000730, 101, '2025-08-11 16:32:57', '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1'),
       (250516171249037345, '24409ecc042b405b886307cd4c4bf650', '用户昵称YKE86D', '', -1, 101,
        '2025-05-16 17:19:53', '106.91.91.80', '中国|0|重庆|重庆市|电信'),
       (250523115114038175, '63529a5087554026a0bb496ec31198a0', '用户昵称CKSV94', '', -1, 302,
        '2025-06-21 11:10:14', '127.0.0.1', '0|0|0|内网IP|内网IP');
/*!40000 ALTER TABLE `base_user_info_1`
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
    `belong_id` bigint NOT NULL COMMENT '归属者用户主键 id（拥有全部权限）',
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

LOCK TABLES `base_file` WRITE;
/*!40000 ALTER TABLE `base_file`
    DISABLE KEYS */;
INSERT INTO `base_file`
VALUES (250115170959009112, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        'be-private-bucket', 'file-system/0/7c12995cd1b44217a1e40ea7e4920f34.png',
        'user.08803b0d_compressed.png', '7c12995cd1b44217a1e40ea7e4920f34.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115170959009113, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        'be-private-bucket', 'file-system/0/855ea4610b1a4f8392522db1f7e7b252.png',
        '可爱卡通头像 (2).png', '855ea4610b1a4f8392522db1f7e7b252.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115170959009114, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        'be-private-bucket', 'file-system/0/3f3ecb1b1d5f443698befae621f2f028.jpg', 'user.jpg',
        '3f3ecb1b1d5f443698befae621f2f028.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115170959009115, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        'be-private-bucket', 'file-system/0/4e75b4e7bf8d41b1ab23b0c31bc36540.png',
        '可爱卡通头像 (1).png', '4e75b4e7bf8d41b1ab23b0c31bc36540.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115170959009116, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        'be-private-bucket', 'file-system/0/676991086a3e4bb18abc0d598139c1d7.png',
        '可爱卡通头像.png', '676991086a3e4bb18abc0d598139c1d7.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171011009143, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        'be-private-bucket', 'file-system/0/e06d0abc29ff477c89e622037aef1e95.png',
        '可爱卡通头像 (1).png', 'e06d0abc29ff477c89e622037aef1e95.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171011009144, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        'be-private-bucket', 'file-system/0/239bd837d2bb4873a52c77c32862df37.png',
        'user.08803b0d_compressed.png', '239bd837d2bb4873a52c77c32862df37.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171011009145, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        'be-private-bucket', 'file-system/0/1f7b4496d4f44b2680f13453e499511a.jpg', 'user.jpg',
        '1f7b4496d4f44b2680f13453e499511a.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171011009146, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        'be-private-bucket', 'file-system/0/fc0a02b8ef524369839b9b9e27ba937e.png',
        '可爱卡通头像.png', 'fc0a02b8ef524369839b9b9e27ba937e.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171011009147, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        'be-private-bucket', 'file-system/0/1529af61adff40c1929ad1f6929cc653.png',
        '可爱卡通头像 (2).png', '1529af61adff40c1929ad1f6929cc653.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171026009175, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '', 0,
        'be-private-bucket', 'file-system/0/c0b7e18970c349f6a475b93096ad2a7b.jpg', 'user.jpg',
        'c0b7e18970c349f6a475b93096ad2a7b.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171026009176, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '', 0,
        'be-private-bucket', 'file-system/0/360929823410486c9771ff76332df675.png',
        '可爱卡通头像 (1).png', '360929823410486c9771ff76332df675.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171026009177, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '', 0,
        'be-private-bucket', 'file-system/0/d71f9f1e2d034da594cf2a6e7ce929e8.png',
        '可爱卡通头像 (2).png', 'd71f9f1e2d034da594cf2a6e7ce929e8.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171026009178, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '', 0,
        'be-private-bucket', 'file-system/0/69eb9c01f80141b1a833e63fbe3a3ac0.png',
        '可爱卡通头像.png', '69eb9c01f80141b1a833e63fbe3a3ac0.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171026009179, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '', 0,
        'be-private-bucket', 'file-system/0/f17ff4c897bf4371a0c967c3a98592e9.png',
        'user.08803b0d_compressed.png', 'f17ff4c897bf4371a0c967c3a98592e9.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171036009206, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '', 0,
        'be-private-bucket', 'file-system/0/55ce08419ba74c10b099e3001a43d5a8.jpg', 'user.jpg',
        '55ce08419ba74c10b099e3001a43d5a8.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171036009207, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '', 0,
        'be-private-bucket', 'file-system/0/6c44190982a442fea5dc1523cc3cbdb7.png',
        '可爱卡通头像.png', '6c44190982a442fea5dc1523cc3cbdb7.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171036009208, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '', 0,
        'be-private-bucket', 'file-system/0/668dd6e3495c4244b4dc638b61cf8331.png',
        '可爱卡通头像 (2).png', '668dd6e3495c4244b4dc638b61cf8331.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171036009209, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '', 0,
        'be-private-bucket', 'file-system/0/6b7c2de8171c4fc6ae39c7e786aa46b5.png',
        '可爱卡通头像 (1).png', '6b7c2de8171c4fc6ae39c7e786aa46b5.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171036009210, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '', 0,
        'be-private-bucket', 'file-system/0/ca2dc3d390854b6b864fa3252f30f875.png',
        'user.08803b0d_compressed.png', 'ca2dc3d390854b6b864fa3252f30f875.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171143009243, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        'be-private-bucket', 'file-system/0/6da5bbf5ee05445388ad5659d471362c.jpg', 'user.jpg',
        '6da5bbf5ee05445388ad5659d471362c.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171143009244, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        'be-private-bucket', 'file-system/0/b388d09791ef4834a61c70089580eebf.png',
        'user.08803b0d_compressed.png', 'b388d09791ef4834a61c70089580eebf.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171143009245, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        'be-private-bucket', 'file-system/0/d620269654dc44c79ee0ef7d4ca354e1.png',
        '可爱卡通头像.png', 'd620269654dc44c79ee0ef7d4ca354e1.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171143009246, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        'be-private-bucket', 'file-system/0/6f40c136088d4f689b1e4739a2905576.png',
        '可爱卡通头像 (1).png', '6f40c136088d4f689b1e4739a2905576.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171143009247, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        'be-private-bucket', 'file-system/0/18ec806417084c0ba15d1e908b10b1b3.png',
        '可爱卡通头像 (2).png', '18ec806417084c0ba15d1e908b10b1b3.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171225009270, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/d11041d67dad4127a8b8613a920bef13.png',
        'user.08803b0d_compressed.png', 'd11041d67dad4127a8b8613a920bef13.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171225009271, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/ddea014b193447ddbdaf716b8d799cc0.png',
        '可爱卡通头像 (2).png', 'ddea014b193447ddbdaf716b8d799cc0.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171225009272, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/9b88abf33f7b44fd86a360d7efbc685a.jpg', 'user.jpg',
        '9b88abf33f7b44fd86a360d7efbc685a.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171225009273, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/b94a6a1e0a694faaba156e54dc84c546.png',
        '可爱卡通头像 (1).png', 'b94a6a1e0a694faaba156e54dc84c546.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171225009274, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/f00138baeb3c4d3ab983cda4d6dffb36.png',
        '可爱卡通头像.png', 'f00138baeb3c4d3ab983cda4d6dffb36.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171225009275, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/785c988108074686b20938defaaedec5.png',
        '可爱卡通头像 (1).png', '785c988108074686b20938defaaedec5.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171225009276, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/915596c4d01b4e41921268cf76829c13.png',
        'user.08803b0d_compressed.png', '915596c4d01b4e41921268cf76829c13.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171225009277, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/032003ad315740439305c34434bb04ba.jpg', 'user.jpg',
        '032003ad315740439305c34434bb04ba.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171225009278, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/4ef6f1b3a600490f824f77c3448641b1.png',
        '可爱卡通头像.png', '4ef6f1b3a600490f824f77c3448641b1.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171225009279, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/ec096d4332b5415f915331fc7883b8e6.png',
        '可爱卡通头像 (2).png', 'ec096d4332b5415f915331fc7883b8e6.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171225009280, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/cd1dae8ec22f4b1bbdbfd10581e145f7.jpg', 'user.jpg',
        'cd1dae8ec22f4b1bbdbfd10581e145f7.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171225009281, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/a467fc5da1d24bf28f5cc743ae9b2a49.png',
        '可爱卡通头像 (1).png', 'a467fc5da1d24bf28f5cc743ae9b2a49.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171225009282, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/27bc87cd574447f5830b58248def7448.png',
        '可爱卡通头像 (2).png', '27bc87cd574447f5830b58248def7448.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171225009283, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/0d121dfb22fb4be4b125e7e6f0a3ce5c.png',
        '可爱卡通头像.png', '0d121dfb22fb4be4b125e7e6f0a3ce5c.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171225009284, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/de6062280c9f40df94399717a9e43040.png',
        'user.08803b0d_compressed.png', 'de6062280c9f40df94399717a9e43040.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171225009285, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/69ef8e9e5b824e5191272b3d2509f659.jpg', 'user.jpg',
        '69ef8e9e5b824e5191272b3d2509f659.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171225009286, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/5214a1bebaf341b5b19f55e33ff2d5a6.png',
        '可爱卡通头像.png', '5214a1bebaf341b5b19f55e33ff2d5a6.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171225009287, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/a33bf1171f0a4c7982d4b15dc2c9359c.png',
        '可爱卡通头像 (2).png', 'a33bf1171f0a4c7982d4b15dc2c9359c.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171225009288, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/c2855cea63714465b1dafe5e98416dbf.png',
        '可爱卡通头像 (1).png', 'c2855cea63714465b1dafe5e98416dbf.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171225009289, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/c9964b0b9a3f40e38ffeb94ebf9b6fdf.png',
        'user.08803b0d_compressed.png', 'c9964b0b9a3f40e38ffeb94ebf9b6fdf.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171225009290, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/7920769469884634a011808f1d99fe79.jpg', 'user.jpg',
        '7920769469884634a011808f1d99fe79.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171225009291, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/eb209ae566f74481b4c6c92b6f512da8.png',
        'user.08803b0d_compressed.png', 'eb209ae566f74481b4c6c92b6f512da8.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171225009292, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/151a4cfe50604ae6a6c8f221d42fc49d.png',
        '可爱卡通头像.png', '151a4cfe50604ae6a6c8f221d42fc49d.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171225009293, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/945ef44e406645698c3303161226b4a8.png',
        '可爱卡通头像 (1).png', '945ef44e406645698c3303161226b4a8.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171225009294, 0, '2025-01-15 17:12:28', 0, '2025-01-15 17:12:28', 1, '', 0,
        'be-private-bucket', 'file-system/0/6fc0cd6d28dd4fa3b18eacfdd6dd0456.png',
        '可爱卡通头像 (2).png', '6fc0cd6d28dd4fa3b18eacfdd6dd0456.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009303, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/b62f9b969c6543db8de1c9195c865ed8.png',
        'user.08803b0d_compressed.png', 'b62f9b969c6543db8de1c9195c865ed8.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009304, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/65f6923c03aa415b87b746fc5db6c41f.png',
        '可爱卡通头像 (2).png', '65f6923c03aa415b87b746fc5db6c41f.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009305, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/22647550ff2e4890a7acc424e6057254.jpg', 'user.jpg',
        '22647550ff2e4890a7acc424e6057254.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009306, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/9cc4eca9d30442f4a3831c0a549ead32.png',
        '可爱卡通头像 (1).png', '9cc4eca9d30442f4a3831c0a549ead32.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009307, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/bd5dda7be7444be99fe2bc14dbdf5022.png',
        '可爱卡通头像.png', 'bd5dda7be7444be99fe2bc14dbdf5022.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009308, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/359c2f3b1b8542a0b5b21639567277ca.png',
        '可爱卡通头像 (1).png', '359c2f3b1b8542a0b5b21639567277ca.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009309, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/ccc07d77376d4da6af24f6fe7105cfc6.png',
        'user.08803b0d_compressed.png', 'ccc07d77376d4da6af24f6fe7105cfc6.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009310, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/2fd5390b365547cc8ffa2f31dba63581.jpg', 'user.jpg',
        '2fd5390b365547cc8ffa2f31dba63581.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009311, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/430b0837c6a64556afa4b5908d3ef2ec.png',
        '可爱卡通头像.png', '430b0837c6a64556afa4b5908d3ef2ec.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009312, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/fb35d4c0eeae4af68babd0767ac39cff.png',
        '可爱卡通头像 (2).png', 'fb35d4c0eeae4af68babd0767ac39cff.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009313, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/ff166a13726840b6b9cec36f1d4b8b24.jpg', 'user.jpg',
        'ff166a13726840b6b9cec36f1d4b8b24.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009314, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/49da141fbc844c558a8f7c868b13d365.png',
        '可爱卡通头像 (1).png', '49da141fbc844c558a8f7c868b13d365.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009315, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/385098eccf134835b57eac38fe4b2382.png',
        '可爱卡通头像 (2).png', '385098eccf134835b57eac38fe4b2382.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009316, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/05c456e2896c402ca0f3d3767e5fa830.png',
        '可爱卡通头像.png', '05c456e2896c402ca0f3d3767e5fa830.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009317, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/84fc5406af154e8396786386ca3980ad.png',
        'user.08803b0d_compressed.png', '84fc5406af154e8396786386ca3980ad.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009318, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/bed2f53b7a004a7eace3fd350b6352ef.jpg', 'user.jpg',
        'bed2f53b7a004a7eace3fd350b6352ef.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009319, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/ceaf91f97f714e23bff5d5968fcdd1e2.png',
        '可爱卡通头像.png', 'ceaf91f97f714e23bff5d5968fcdd1e2.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009320, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/acd09c0558b7405e873390db6461a4d3.png',
        '可爱卡通头像 (2).png', 'acd09c0558b7405e873390db6461a4d3.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009321, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/bf8331eb39ae4d20a93fd56e82fc35bb.png',
        '可爱卡通头像 (1).png', 'bf8331eb39ae4d20a93fd56e82fc35bb.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009322, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/227ccd93450f4f5bbe62b04ba34fdd50.png',
        'user.08803b0d_compressed.png', '227ccd93450f4f5bbe62b04ba34fdd50.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009323, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/1965c5820bc5498194120cdf72328e4b.jpg', 'user.jpg',
        '1965c5820bc5498194120cdf72328e4b.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009324, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/a04ba41ac36348b0989de44d0ca703c9.png',
        'user.08803b0d_compressed.png', 'a04ba41ac36348b0989de44d0ca703c9.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009325, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/0cd5836997ee47708ec70cda4f4ef1fe.png',
        '可爱卡通头像.png', '0cd5836997ee47708ec70cda4f4ef1fe.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009326, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/e6f5235df4d34248a6540f64c80139fb.png',
        '可爱卡通头像 (1).png', 'e6f5235df4d34248a6540f64c80139fb.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009327, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/e1b0295760614479a9482c0f2ead6fbb.png',
        '可爱卡通头像 (2).png', 'e1b0295760614479a9482c0f2ead6fbb.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009328, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/511374bfd00445fe9804e253565a39e6.png',
        'user.08803b0d_compressed.png', '511374bfd00445fe9804e253565a39e6.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009329, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/88c87820b70d4acba7d3556cc2a0b8e3.png',
        '可爱卡通头像 (2).png', '88c87820b70d4acba7d3556cc2a0b8e3.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009330, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/3d7ca96a6ef3474c940a82134039308c.jpg', 'user.jpg',
        '3d7ca96a6ef3474c940a82134039308c.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009331, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/14a60c0c6b354541b5c7aa96b5757c20.png',
        '可爱卡通头像 (1).png', '14a60c0c6b354541b5c7aa96b5757c20.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009332, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/21d62daea9a346299d562299075156e9.png',
        '可爱卡通头像.png', '21d62daea9a346299d562299075156e9.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009333, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/f29582109f0b4dd3b30c19c31c8d687b.png',
        '可爱卡通头像 (1).png', 'f29582109f0b4dd3b30c19c31c8d687b.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009334, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/51064ce16ecf483883eaae8def07d4d9.png',
        'user.08803b0d_compressed.png', '51064ce16ecf483883eaae8def07d4d9.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009335, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/dce6e8c3329e4dad84d3b32f4ec613c1.jpg', 'user.jpg',
        'dce6e8c3329e4dad84d3b32f4ec613c1.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009336, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/9be68588148a41af891164ae8f96bd0f.png',
        '可爱卡通头像.png', '9be68588148a41af891164ae8f96bd0f.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009337, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/c2a534795df14742939f90caaf22918c.png',
        '可爱卡通头像 (2).png', 'c2a534795df14742939f90caaf22918c.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009338, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/64e1f6b64b464700a4cd9f4a432e7121.jpg', 'user.jpg',
        '64e1f6b64b464700a4cd9f4a432e7121.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009339, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/3c3d006b5359419ba959a83d6b6cb685.png',
        '可爱卡通头像 (1).png', '3c3d006b5359419ba959a83d6b6cb685.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009340, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/1ae32066752e45b2963ac00183175c49.png',
        '可爱卡通头像 (2).png', '1ae32066752e45b2963ac00183175c49.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009341, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/64b26c8ec0684614866b761f2902de3e.png',
        '可爱卡通头像.png', '64b26c8ec0684614866b761f2902de3e.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009342, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/f9969d36fb69414e8df06fbaba6a16e2.png',
        'user.08803b0d_compressed.png', 'f9969d36fb69414e8df06fbaba6a16e2.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009343, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/d82841aab7b4470dafc1573219f28576.jpg', 'user.jpg',
        'd82841aab7b4470dafc1573219f28576.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009344, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/97f3b0bf2f7a4a68b97706362ebac67d.png',
        '可爱卡通头像.png', '97f3b0bf2f7a4a68b97706362ebac67d.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009345, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/2a20e22ee46340148c27cc4dbdb18f12.png',
        '可爱卡通头像 (2).png', '2a20e22ee46340148c27cc4dbdb18f12.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250115171240009346, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/c0f6be97f46d49be84cf8c9bbed7cf1a.png',
        '可爱卡通头像 (1).png', 'c0f6be97f46d49be84cf8c9bbed7cf1a.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009347, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/8b256c03228f46fb94c2d8b17e575c28.png',
        'user.08803b0d_compressed.png', '8b256c03228f46fb94c2d8b17e575c28.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009348, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/094e64ae6491429482b13a2d2654675a.jpg', 'user.jpg',
        '094e64ae6491429482b13a2d2654675a.jpg', 'jpg', '', 801, 1, 201, 0, 101, 'user.jpg', 0,
        11768, -1, '|0|', 0),
       (250115171240009349, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/b8cce87042034551a723c05ff8b98ad9.png',
        'user.08803b0d_compressed.png', 'b8cce87042034551a723c05ff8b98ad9.png', 'png', '', 801, 1,
        201, 0, 101, 'user.08803b0d_compressed.png', 0, 2499, -1, '|0|', 0),
       (250115171240009350, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/5877830cb4504c73a57c8cada3ae02be.png',
        '可爱卡通头像.png', '5877830cb4504c73a57c8cada3ae02be.png', 'png', '', 801, 1, 201, 0, 101,
        '可爱卡通头像.png', 0, 1203565, -1, '|0|', 0),
       (250115171240009351, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/f7a41ca011fc49fc82cdc7335bdd68d3.png',
        '可爱卡通头像 (1).png', 'f7a41ca011fc49fc82cdc7335bdd68d3.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (1).png', 0, 1207589, -1, '|0|', 0),
       (250115171240009352, 0, '2025-01-15 17:12:47', 0, '2025-01-15 17:12:47', 1, '', 0,
        'be-private-bucket', 'file-system/0/7dd75ca044414973a33c75d639f6005c.png',
        '可爱卡通头像 (2).png', '7dd75ca044414973a33c75d639f6005c.png', 'png', '', 801, 1, 201, 0,
        101, '可爱卡通头像 (2).png', 0, 873098, -1, '|0|', 0),
       (250509091258031038, 0, '2025-05-09 09:12:58', 0, '2025-05-09 09:12:58', 1, '', 0,
        'be-public-bucket', 'temp-file/0/16e8259fa54646e3bd791f5b73665fa3.ts',
        'd8119c3b72134c96a2d9db202bd8559f.ts', '16e8259fa54646e3bd791f5b73665fa3.ts', 'ts', '', 701,
        1, 201, 0, 101, 'd8119c3b72134c96a2d9db202bd8559f.ts', 1, 1163344, -1, '|0|', 0),
       (250509092224031127, 0, '2025-05-09 09:22:25', 0, '2025-05-09 09:22:25', 1, '', 0,
        'be-public-bucket', 'temp-file/0/5764e85074e84db0ba7739a0f1581742.ts',
        'b67985f03735427a8357ed5c0664899b.ts', '5764e85074e84db0ba7739a0f1581742.ts', 'ts', '', 701,
        1, 201, 0, 101, 'b67985f03735427a8357ed5c0664899b.ts', 1, 998844, -1, '|0|', 0),
       (250509092339031172, 0, '2025-05-09 09:23:40', 0, '2025-05-09 09:23:40', 1, '', 0,
        'be-public-bucket', 'temp-file/0/b5dc0e294c3e49db8bcd606808ae7d4c.ts',
        '15996bc753a64c848b79f401ff6da511.ts', 'b5dc0e294c3e49db8bcd606808ae7d4c.ts', 'ts', '', 701,
        1, 201, 0, 101, '15996bc753a64c848b79f401ff6da511.ts', 1, 647660, -1, '|0|', 0),
       (250509092349031192, 0, '2025-05-09 09:23:50', 0, '2025-05-09 09:23:50', 1, '', 0,
        'be-public-bucket', 'temp-file/0/4e42adf09537459aa25608eea6ddcf9d.ts',
        'c4f569e0812b407cb1184e1c6d3963dc.ts', '4e42adf09537459aa25608eea6ddcf9d.ts', 'ts', '', 701,
        1, 201, 0, 101, 'c4f569e0812b407cb1184e1c6d3963dc.ts', 1, 587500, -1, '|0|', 0),
       (250509092609031243, 0, '2025-05-09 09:26:09', 0, '2025-05-09 09:26:09', 1, '', 0,
        'be-public-bucket', 'temp-file/0/d5ec478fdec8410281c1f5408269cba7.ts',
        'f43939af4eaf455a8b6e73434b266678.ts', 'd5ec478fdec8410281c1f5408269cba7.ts', 'ts', '', 701,
        1, 201, 0, 101, 'f43939af4eaf455a8b6e73434b266678.ts', 1, 953724, -1, '|0|', 0),
       (250509092915031302, 0, '2025-05-09 09:29:16', 0, '2025-05-09 09:29:16', 1, '', 0,
        'be-public-bucket', 'temp-file/0/175c0ea8dbed433a97371e2597126158.ts',
        '89ca03a00c3746da8feda2ad1b18d170.ts', '175c0ea8dbed433a97371e2597126158.ts', 'ts', '', 701,
        1, 201, 0, 101, '89ca03a00c3746da8feda2ad1b18d170.ts', 1, 768168, -1, '|0|', 0),
       (250509093317031351, 0, '2025-05-09 09:33:17', 0, '2025-05-09 09:33:17', 1, '', 0,
        'be-public-bucket', 'temp-file/0/eabb5ad62ec94819a2d41b72dd89d1ac.ts',
        '367ef437a3794f97b204ea38982d3a5f.ts', 'eabb5ad62ec94819a2d41b72dd89d1ac.ts', 'ts', '', 701,
        1, 201, 0, 101, '367ef437a3794f97b204ea38982d3a5f.ts', 1, 756512, -1, '|0|', 0),
       (250509093327031373, 0, '2025-05-09 09:33:28', 0, '2025-05-09 09:33:28', 1, '', 0,
        'be-public-bucket', 'temp-file/0/69636e601f3244698e77c04e736b2fee.ts',
        '4ca06265e4cd4135bc38905267b8c39e.ts', '69636e601f3244698e77c04e736b2fee.ts', 'ts', '', 701,
        1, 201, 0, 101, '4ca06265e4cd4135bc38905267b8c39e.ts', 1, 615700, -1, '|0|', 0),
       (250509093338031396, 0, '2025-05-09 09:33:38', 0, '2025-05-09 09:33:38', 1, '', 0,
        'be-public-bucket', 'temp-file/0/0269856bab9448bda2da785ce09a7821.ts',
        '6a3423c5ab3c45449f59a3a037c7fe4c.ts', '0269856bab9448bda2da785ce09a7821.ts', 'ts', '', 701,
        1, 201, 0, 101, '6a3423c5ab3c45449f59a3a037c7fe4c.ts', 1, 614384, -1, '|0|', 0),
       (250509093539031433, 0, '2025-05-09 09:35:39', 0, '2025-05-09 09:35:39', 1, '', 0,
        'be-public-bucket', 'temp-file/0/775a56d857a046778c6864ba6c37276c.ts',
        'cf31f02a41ed4800ae74e67e5e5f67f5.ts', '775a56d857a046778c6864ba6c37276c.ts', 'ts', '', 701,
        1, 201, 0, 101, 'cf31f02a41ed4800ae74e67e5e5f67f5.ts', 1, 628296, -1, '|0|', 0),
       (250509093549031456, 0, '2025-05-09 09:35:50', 0, '2025-05-09 09:35:50', 1, '', 0,
        'be-public-bucket', 'temp-file/0/e3f89fb6cf2e4358a9a780fda9310e64.ts',
        'd9644fe77b344b08aaac3063160438eb.ts', 'e3f89fb6cf2e4358a9a780fda9310e64.ts', 'ts', '', 701,
        1, 201, 0, 101, 'd9644fe77b344b08aaac3063160438eb.ts', 1, 620588, -1, '|0|', 0),
       (250509093600031479, 0, '2025-05-09 09:36:00', 0, '2025-05-09 09:36:00', 1, '', 0,
        'be-public-bucket', 'temp-file/0/6cec2eda9e0f4a068cd37eeb691392e0.ts',
        '014d1555df1f4e20862857992434a1b6.ts', '6cec2eda9e0f4a068cd37eeb691392e0.ts', 'ts', '', 701,
        1, 201, 0, 101, '014d1555df1f4e20862857992434a1b6.ts', 1, 540124, -1, '|0|', 0),
       (250509094020031526, 0, '2025-05-09 09:40:21', 0, '2025-05-09 09:40:21', 1, '', 0,
        'be-public-bucket', 'temp-file/0/0e8e09e7443144ac9ff37fe27e3ae267.ts',
        'e69f9bdeff2a47c3bc442e0a84bfaff7.ts', '0e8e09e7443144ac9ff37fe27e3ae267.ts', 'ts', '', 701,
        1, 201, 0, 101, 'e69f9bdeff2a47c3bc442e0a84bfaff7.ts', 1, 815920, -1, '|0|', 0),
       (250509094049031549, 0, '2025-05-09 09:40:49', 0, '2025-05-09 09:40:49', 1, '', 0,
        'be-public-bucket', 'temp-file/0/1126c5f3c0ac417883e92edb8f6e1d60.ts',
        'fb2f88c9cf6b4fb08b4e5f0221c55cff.ts', '1126c5f3c0ac417883e92edb8f6e1d60.ts', 'ts', '', 701,
        1, 201, 0, 101, 'fb2f88c9cf6b4fb08b4e5f0221c55cff.ts', 1, 760272, -1, '|0|', 0),
       (250509103356031777, 0, '2025-05-09 10:33:57', 0, '2025-05-09 10:33:57', 1, '', 0,
        'be-public-bucket', 'temp-file/0/3c571b99e5ea474c989152f8764be71a.ts',
        '2061d150e68248e6bb424de547f35e06.ts', '3c571b99e5ea474c989152f8764be71a.ts', 'ts', '', 701,
        1, 201, 0, 101, '2061d150e68248e6bb424de547f35e06.ts', 1, 929284, -1, '|0|', 0),
       (250509103406031800, 0, '2025-05-09 10:34:07', 0, '2025-05-09 10:34:07', 1, '', 0,
        'be-public-bucket', 'temp-file/0/6611c691b8194f44b886720926db9bcf.ts',
        'e16db732f1824e7eb670bf3ddef0b8a8.ts', '6611c691b8194f44b886720926db9bcf.ts', 'ts', '', 701,
        1, 201, 0, 101, 'e16db732f1824e7eb670bf3ddef0b8a8.ts', 1, 595960, -1, '|0|', 0),
       (250509103532031856, 0, '2025-05-09 10:35:32', 0, '2025-05-09 10:35:32', 1, '', 0,
        'be-public-bucket', 'temp-file/0/ff7d0fb3e61341a680e63f1ede417b65.ts',
        '089d1a018c1f461f82b417b88396855b.ts', 'ff7d0fb3e61341a680e63f1ede417b65.ts', 'ts', '', 701,
        1, 201, 0, 101, '089d1a018c1f461f82b417b88396855b.ts', 1, 813100, -1, '|0|', 0),
       (250509103542031880, 0, '2025-05-09 10:35:42', 0, '2025-05-09 10:35:42', 1, '', 0,
        'be-public-bucket', 'temp-file/0/e958bbc01c684000a82df6d15193598d.ts',
        '320c8792827e442eab0bb0cde2e4279d.ts', 'e958bbc01c684000a82df6d15193598d.ts', 'ts', '', 701,
        1, 201, 0, 101, '320c8792827e442eab0bb0cde2e4279d.ts', 1, 639952, -1, '|0|', 0),
       (250509103552031903, 0, '2025-05-09 10:35:53', 0, '2025-05-09 10:35:53', 1, '', 0,
        'be-public-bucket', 'temp-file/0/59d88740c5d64e09984b22cb06f253f0.ts',
        '8829ca6cefe04d04af7f0b8dac0d83f0.ts', '59d88740c5d64e09984b22cb06f253f0.ts', 'ts', '', 701,
        1, 201, 0, 101, '8829ca6cefe04d04af7f0b8dac0d83f0.ts', 1, 493124, -1, '|0|', 0),
       (250509134609032131, 0, '2025-05-09 13:46:10', 0, '2025-05-09 13:46:10', 1, '', 0,
        'be-public-bucket', 'temp-file/0/b75e8e343a23413489ee0803b590450d.ts',
        '6ed7308e74654e48aad8c4b3c66522f2.ts', 'b75e8e343a23413489ee0803b590450d.ts', 'ts', '', 701,
        1, 201, 0, 101, '6ed7308e74654e48aad8c4b3c66522f2.ts', 1, 652924, -1, '|0|', 0),
       (250509134621032156, 0, '2025-05-09 13:46:21', 0, '2025-05-09 13:46:21', 1, '', 0,
        'be-public-bucket', 'temp-file/0/f33903e416814650b57f583197ddb00f.ts',
        '64b8cbd2b89c4e3ea9882d5b90b09d57.ts', 'f33903e416814650b57f583197ddb00f.ts', 'ts', '', 701,
        1, 201, 0, 101, '64b8cbd2b89c4e3ea9882d5b90b09d57.ts', 1, 720792, -1, '|0|', 0),
       (250509134632032178, 0, '2025-05-09 13:46:32', 0, '2025-05-09 13:46:32', 1, '', 0,
        'be-public-bucket', 'temp-file/0/9e8f152bfa6148e19c96f2697c43dc33.ts',
        '3599ea81ab8c403d8d5679cd22bf8c95.ts', '9e8f152bfa6148e19c96f2697c43dc33.ts', 'ts', '', 701,
        1, 201, 0, 101, '3599ea81ab8c403d8d5679cd22bf8c95.ts', 1, 657060, -1, '|0|', 0),
       (250509135511032243, 0, '2025-05-09 13:55:11', 0, '2025-05-09 13:55:11', 1, '', 0,
        'be-public-bucket', 'temp-file/0/b6710f3dc3e34f8199d703ed89d292b1.ts',
        'aacf485f72f741a5ba365c0c28ae91e5.ts', 'b6710f3dc3e34f8199d703ed89d292b1.ts', 'ts', '', 701,
        1, 201, 0, 101, 'aacf485f72f741a5ba365c0c28ae91e5.ts', 1, 840924, -1, '|0|', 0),
       (250509135521032267, 0, '2025-05-09 13:55:22', 0, '2025-05-09 13:55:22', 1, '', 0,
        'be-public-bucket', 'temp-file/0/767ecf1b642d4d389ec3f8f2ff75096d.ts',
        '3c38deafb2b8470a88d61f9e49313247.ts', '767ecf1b642d4d389ec3f8f2ff75096d.ts', 'ts', '', 701,
        1, 201, 0, 101, '3c38deafb2b8470a88d61f9e49313247.ts', 1, 756888, -1, '|0|', 0),
       (250509135615032312, 0, '2025-05-09 13:56:16', 0, '2025-05-09 13:56:16', 1, '', 0,
        'be-public-bucket', 'temp-file/0/45693045a3c64add8284bd202c475616.ts',
        'e3c5869f416345c48c00891d491f33b2.ts', '45693045a3c64add8284bd202c475616.ts', 'ts', '', 701,
        1, 201, 0, 101, 'e3c5869f416345c48c00891d491f33b2.ts', 1, 818928, -1, '|0|', 0),
       (250509135637032333, 0, '2025-05-09 13:56:38', 0, '2025-05-09 13:56:38', 1, '', 0,
        'be-public-bucket', 'temp-file/0/b80504c3364e4c70bb39402d091f874d.ts',
        '8d4c620a76d54868b016f8391adcc2c0.ts', 'b80504c3364e4c70bb39402d091f874d.ts', 'ts', '', 701,
        1, 201, 0, 101, '8d4c620a76d54868b016f8391adcc2c0.ts', 1, 908416, -1, '|0|', 0),
       (250509141125032456, 0, '2025-05-09 14:11:26', 0, '2025-05-09 14:11:26', 1, '', 0,
        'be-public-bucket', 'temp-file/0/4696450b327943c5a3b2bd610e973d9d.ts',
        'c976ccce91754f82837435e8e86aaac7.ts', '4696450b327943c5a3b2bd610e973d9d.ts', 'ts', '', 701,
        1, 201, 0, 101, 'c976ccce91754f82837435e8e86aaac7.ts', 1, 1124616, -1, '|0|', 0),
       (250509141136032481, 0, '2025-05-09 14:11:37', 0, '2025-05-09 14:11:37', 1, '', 0,
        'be-public-bucket', 'temp-file/0/ed7e6fd73a2f4b5dbc68cc0fdba7a76d.ts',
        '1645d26fe6df4808b8674415fe2a49b8.ts', 'ed7e6fd73a2f4b5dbc68cc0fdba7a76d.ts', 'ts', '', 701,
        1, 201, 0, 101, '1645d26fe6df4808b8674415fe2a49b8.ts', 1, 715152, -1, '|0|', 0),
       (250509141147032505, 0, '2025-05-09 14:11:48', 0, '2025-05-09 14:11:48', 1, '', 0,
        'be-public-bucket', 'temp-file/0/7ca2f57902734eff9dfa94eee9c1ccaf.ts',
        '3bcf10df9e634abbba444f770ea5ae7e.ts', '7ca2f57902734eff9dfa94eee9c1ccaf.ts', 'ts', '', 701,
        1, 201, 0, 101, '3bcf10df9e634abbba444f770ea5ae7e.ts', 1, 671536, -1, '|0|', 0),
       (250509141158032530, 0, '2025-05-09 14:11:59', 0, '2025-05-09 14:11:59', 1, '', 0,
        'be-public-bucket', 'temp-file/0/5a369a6607ea470392e9065559cd56f1.ts',
        '93469168c9064157a48fb2392cd0c167.ts', '5a369a6607ea470392e9065559cd56f1.ts', 'ts', '', 701,
        1, 201, 0, 101, '93469168c9064157a48fb2392cd0c167.ts', 1, 667024, -1, '|0|', 0),
       (250509141209032554, 0, '2025-05-09 14:12:09', 0, '2025-05-09 14:12:09', 1, '', 0,
        'be-public-bucket', 'temp-file/0/d7495118478148ef99462a15d19e5c7e.ts',
        '9d5727d3bb784ab7bcb00b8f36b38f78.ts', 'd7495118478148ef99462a15d19e5c7e.ts', 'ts', '', 701,
        1, 201, 0, 101, '9d5727d3bb784ab7bcb00b8f36b38f78.ts', 1, 639200, -1, '|0|', 0),
       (250509141229032592, 0, '2025-05-09 14:12:30', 0, '2025-05-09 14:12:30', 1, '', 0,
        'be-public-bucket', 'temp-file/0/f91df4743ee7423a8b71886dd72f8424.ts',
        '5f14445b6be04aa88b5da6586505fb6c.ts', 'f91df4743ee7423a8b71886dd72f8424.ts', 'ts', '', 701,
        1, 201, 0, 101, '5f14445b6be04aa88b5da6586505fb6c.ts', 1, 755008, -1, '|0|', 0),
       (250509141241032616, 0, '2025-05-09 14:12:41', 0, '2025-05-09 14:12:41', 1, '', 0,
        'be-public-bucket', 'temp-file/0/296744dcf6884e579aeff6982444ee8e.ts',
        '8750e98828de4856b01c3a1817ac087a.ts', '296744dcf6884e579aeff6982444ee8e.ts', 'ts', '', 701,
        1, 201, 0, 101, '8750e98828de4856b01c3a1817ac087a.ts', 1, 758016, -1, '|0|', 0),
       (250509141301032655, 0, '2025-05-09 14:13:02', 0, '2025-05-09 14:13:02', 1, '', 0,
        'be-public-bucket', 'temp-file/0/0f4355086a404d419c0ecb246e02e479.ts',
        'abf36840329a4cf99e76bc55b2e57955.ts', '0f4355086a404d419c0ecb246e02e479.ts', 'ts', '', 701,
        1, 201, 0, 101, 'abf36840329a4cf99e76bc55b2e57955.ts', 1, 705376, -1, '|0|', 0),
       (250509141312032679, 0, '2025-05-09 14:13:13', 0, '2025-05-09 14:13:13', 1, '', 0,
        'be-public-bucket', 'temp-file/0/afba5fd2a76e4ca99022234334da85f8.ts',
        'c2a55ab5bcc4401788e9cb1065ced457.ts', 'afba5fd2a76e4ca99022234334da85f8.ts', 'ts', '', 701,
        1, 201, 0, 101, 'c2a55ab5bcc4401788e9cb1065ced457.ts', 1, 699548, -1, '|0|', 0),
       (250509141323032698, 0, '2025-05-09 14:13:23', 0, '2025-05-09 14:13:23', 1, '', 0,
        'be-public-bucket', 'temp-file/0/84b60af2468d45aba48d5cd3b4c49687.ts',
        '059a2255c85f4ea8a934a855c8cbbd99.ts', '84b60af2468d45aba48d5cd3b4c49687.ts', 'ts', '', 701,
        1, 201, 0, 101, '059a2255c85f4ea8a934a855c8cbbd99.ts', 1, 611376, -1, '|0|', 0),
       (250509141743032769, 0, '2025-05-09 14:17:43', 0, '2025-05-09 14:17:43', 1, '', 0,
        'be-public-bucket', 'temp-file/0/cafb9c5f01fe4e7da89223c36b7b0126.ts',
        '3af9acd3b5fa4b39af675ddf32e9f66c.ts', 'cafb9c5f01fe4e7da89223c36b7b0126.ts', 'ts', '', 701,
        1, 201, 0, 101, '3af9acd3b5fa4b39af675ddf32e9f66c.ts', 1, 954476, -1, '|0|', 0),
       (250509141753032794, 0, '2025-05-09 14:17:54', 0, '2025-05-09 14:17:54', 1, '', 0,
        'be-public-bucket', 'temp-file/0/cc052db3294b404abf880afbca9ac979.ts',
        'c9cac05ef6a24ebba9fa43bb9489aed2.ts', 'cc052db3294b404abf880afbca9ac979.ts', 'ts', '', 701,
        1, 201, 0, 101, 'c9cac05ef6a24ebba9fa43bb9489aed2.ts', 1, 601976, -1, '|0|', 0),
       (250509141918032840, 0, '2025-05-09 14:19:19', 0, '2025-05-09 14:19:19', 1, '', 0,
        'be-public-bucket', 'temp-file/0/0bf6b5852c314a21bcf24f03b754ac40.ts',
        '732ac1d10ddd401f9a99e814a5de8eda.ts', '0bf6b5852c314a21bcf24f03b754ac40.ts', 'ts', '', 701,
        1, 201, 0, 101, '732ac1d10ddd401f9a99e814a5de8eda.ts', 1, 940940, -1, '|0|', 0),
       (250509141929032865, 0, '2025-05-09 14:19:29', 0, '2025-05-09 14:19:29', 1, '', 0,
        'be-public-bucket', 'temp-file/0/9f32a8b048884b70a1b7bb10f0599526.ts',
        '115728d674a34b7bb8fb7d214b4515f8.ts', '9f32a8b048884b70a1b7bb10f0599526.ts', 'ts', '', 701,
        1, 201, 0, 101, '115728d674a34b7bb8fb7d214b4515f8.ts', 1, 640328, -1, '|0|', 0),
       (250509142008032900, 0, '2025-05-09 14:20:08', 0, '2025-05-09 14:20:08', 1, '', 0,
        'be-public-bucket', 'temp-file/0/15515ea83555470c904bf33b2922be0f.ts',
        '1e9b6b5d015e44ef9b3a81053e512176.ts', '15515ea83555470c904bf33b2922be0f.ts', 'ts', '', 701,
        1, 201, 0, 101, '1e9b6b5d015e44ef9b3a81053e512176.ts', 1, 704436, -1, '|0|', 0),
       (250509142018032922, 0, '2025-05-09 14:20:19', 0, '2025-05-09 14:20:19', 1, '', 0,
        'be-public-bucket', 'temp-file/0/0f77260b5b87417dbef9665db6bd30d7.ts',
        'ec7ef2a8581540c49edc50282a7ab60f.ts', '0f77260b5b87417dbef9665db6bd30d7.ts', 'ts', '', 701,
        1, 201, 0, 101, 'ec7ef2a8581540c49edc50282a7ab60f.ts', 1, 579604, -1, '|0|', 0),
       (250509142029032946, 0, '2025-05-09 14:20:29', 0, '2025-05-09 14:20:29', 1, '', 0,
        'be-public-bucket', 'temp-file/0/ed39475a3e854e2590f3cb2a6b4d18fb.ts',
        'bf9fd4d819d8473194978d5dbefba76d.ts', 'ed39475a3e854e2590f3cb2a6b4d18fb.ts', 'ts', '', 701,
        1, 201, 0, 101, 'bf9fd4d819d8473194978d5dbefba76d.ts', 1, 626792, -1, '|0|', 0),
       (250509142203033001, 0, '2025-05-09 14:22:04', 0, '2025-05-09 14:22:04', 1, '', 0,
        'be-public-bucket', 'temp-file/0/a0890a87235649b2abfaf80e6e1dc80c.ts',
        '7a6a8741eb084ec78aad380b795cc75f.ts', 'a0890a87235649b2abfaf80e6e1dc80c.ts', 'ts', '', 701,
        1, 201, 0, 101, '7a6a8741eb084ec78aad380b795cc75f.ts', 1, 680372, -1, '|0|', 0),
       (250509142214033024, 0, '2025-05-09 14:22:14', 0, '2025-05-09 14:22:14', 1, '', 0,
        'be-public-bucket', 'temp-file/0/e99a41547d0b4080812a31606ef222d5.ts',
        'f599cd2cccaf4c8ba4ac817209767251.ts', 'e99a41547d0b4080812a31606ef222d5.ts', 'ts', '', 701,
        1, 201, 0, 101, 'f599cd2cccaf4c8ba4ac817209767251.ts', 1, 664768, -1, '|0|', 0),
       (250509142224033048, 0, '2025-05-09 14:22:25', 0, '2025-05-09 14:22:25', 1, '', 0,
        'be-public-bucket', 'temp-file/0/6a75cf68a7e441099a60eb2729e2a7a5.ts',
        'bf66f5b015d84b50a735bc14b031447a.ts', '6a75cf68a7e441099a60eb2729e2a7a5.ts', 'ts', '', 701,
        1, 201, 0, 101, 'bf66f5b015d84b50a735bc14b031447a.ts', 1, 622092, -1, '|0|', 0),
       (250509142348033084, 0, '2025-05-09 14:23:49', 0, '2025-05-09 14:23:49', 1, '', 0,
        'be-public-bucket', 'temp-file/0/e24fb846ad1e40a8a02636284b6f4090.ts',
        '9abdbe48497c4d90bfc5a6555a989f53.ts', 'e24fb846ad1e40a8a02636284b6f4090.ts', 'ts', '', 701,
        1, 201, 0, 101, '9abdbe48497c4d90bfc5a6555a989f53.ts', 1, 620400, -1, '|0|', 0),
       (250509142359033107, 0, '2025-05-09 14:23:59', 0, '2025-05-09 14:23:59', 1, '', 0,
        'be-public-bucket', 'temp-file/0/7ac55908bbc04ae9967afe11da64b6d7.ts',
        '9b2e758394a0419aabd02c3910135f75.ts', '7ac55908bbc04ae9967afe11da64b6d7.ts', 'ts', '', 701,
        1, 201, 0, 101, '9b2e758394a0419aabd02c3910135f75.ts', 1, 718724, -1, '|0|', 0),
       (250509142419033137, 0, '2025-05-09 14:24:19', 0, '2025-05-09 14:24:19', 1, '', 0,
        'be-public-bucket', 'temp-file/0/90addf14622845749afe67460960156a.ts',
        '38ff1cd047f8414983bfc60304a737c7.ts', '90addf14622845749afe67460960156a.ts', 'ts', '', 701,
        1, 201, 0, 101, '38ff1cd047f8414983bfc60304a737c7.ts', 1, 834720, -1, '|0|', 0),
       (250509142515033180, 0, '2025-05-09 14:25:16', 0, '2025-05-09 14:25:16', 1, '', 0,
        'be-public-bucket', 'temp-file/0/27c2ad8f468c47ffa24a99f95688a54f.ts',
        '00d3baefb2f448df9483dc25032caecb.ts', '27c2ad8f468c47ffa24a99f95688a54f.ts', 'ts', '', 701,
        1, 201, 0, 101, '00d3baefb2f448df9483dc25032caecb.ts', 1, 884164, -1, '|0|', 0),
       (250513151706033502, 0, '2025-05-13 15:17:07', 0, '2025-05-13 15:17:07', 1, '', 0,
        'be-public-bucket', 'temp-file/0/8cd01b8bd70d4619aa7717706f6eb1e4.ts',
        '2184e3fd476c44eaad39d3cc6462c9d4.ts', '8cd01b8bd70d4619aa7717706f6eb1e4.ts', 'ts', '', 701,
        1, 201, 0, 101, '2184e3fd476c44eaad39d3cc6462c9d4.ts', 1, 28012, -1, '|0|', 0),
       (250513152122033834, 0, '2025-05-13 15:21:23', 0, '2025-05-13 15:21:23', 1, '', 0,
        'be-public-bucket', 'temp-file/0/1516053a9163485ba93726b53c5c94e2.ts',
        '1c75fde1169e49a3a715972616d5debd.ts', '1516053a9163485ba93726b53c5c94e2.ts', 'ts', '', 701,
        1, 201, 0, 101, '1c75fde1169e49a3a715972616d5debd.ts', 1, 54708, -1, '|0|', 0);
/*!40000 ALTER TABLE `base_file`
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
-- Table structure for table `base_user_1`
--

DROP TABLE IF EXISTS `base_user_1`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_1`
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
-- Dumping data for table `base_user_1`
--

LOCK TABLES `base_user_1` WRITE;
/*!40000 ALTER TABLE `base_user_1`
    DISABLE KEYS */;
INSERT INTO `base_user_1`
VALUES (240902154357005281, 0, '2024-09-02 15:43:57', 0, '2024-11-27 16:27:31', 1, '',
        'fc3727dc1a1c43c0a0a9fec4d4650225/4b4e73f56f4d2a54efe1ecc780261a79162f76ee9fd5ab04d1c6ac4b78e9974ddc3a5c9fe1088eb545779a89f64afd21bcd293871991e41500d184e812e1fdf8',
        'dimensional_logic@foxmail.com', 'kar1', '', '', '', ''),
       (250516171249037345, -1, '2025-05-16 17:12:49', -1, '2025-05-16 17:12:49', 1, '',
        'fee2ce5908cf4db2989f2b317d73bedd/9d6b4a359d6759b9a2c4f9da2ad65ac571826d0fc48b6e3fe9b5a78e4a317a43dcc1338f64f29027239253da654ce28e95f5e87ac9fc8033111bec593c7aa089',
        '', 'kar3', '', '', '', ''),
       (250523115114038175, -1, '2025-05-23 11:51:15', -1, '2025-05-23 11:51:15', 1, '',
        '3fca1d5e88164fc88d5fa9258483eea7/232eb56cd58710efd07e1a9726f0e88e237f8cd1c3ca2d36a80eaff798a3ecd53b7eacee4f0fbc37f0cbf23bb1bdd21ad64f8fb72305fda113686a1d2a99475b',
        '', 'kar4', '', '', '', '');
/*!40000 ALTER TABLE `base_user_1`
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
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不能重复',
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
/*!40000 ALTER TABLE `base_user_info`
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
VALUES (1, 1, 1, 1);
/*!40000 ALTER TABLE `base_user_configuration`
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
-- Table structure for table `base_live_room_user`
--

DROP TABLE IF EXISTS `base_live_room_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_live_room_user`
(
    `id`                 bigint   NOT NULL COMMENT '主键 id',
    `room_id`            bigint   NOT NULL COMMENT '实时房间主键 id',
    `user_id`            bigint   NOT NULL COMMENT '用户主键 id',
    `socket_ref_user_id` bigint   NOT NULL COMMENT '套接字关联用户主键 id',
    `create_time`        datetime NOT NULL COMMENT '创建时间',
    `first_blob_str`     longtext NOT NULL COMMENT '第一个 blob，用于播放时，需要先传递该 blob',
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
INSERT INTO `base_live_room_user`
VALUES (250811160710123905, 250506160235021405, 0, 250811160203121540, '2025-08-11 16:07:10',
        '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'),
       (250811160721123975, 250506160235021405, 240902154357005281, 250811160209121564,
        '2025-08-11 16:07:21',
        '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');
/*!40000 ALTER TABLE `base_live_room_user`
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
       (240830160154004401, 241125092636001887);
/*!40000 ALTER TABLE `base_role_ref_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base_user_info_0`
--

DROP TABLE IF EXISTS `base_user_info_0`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_info_0`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不能重复',
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
-- Dumping data for table `base_user_info_0`
--

LOCK TABLES `base_user_info_0` WRITE;
/*!40000 ALTER TABLE `base_user_info_0`
    DISABLE KEYS */;
INSERT INTO `base_user_info_0`
VALUES (0, 'admin', 'admin', '666', 250103150802002454, 101, '2025-08-11 16:11:39',
        '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1'),
       (240902163618005502, '91a518a5234249a8a312bbc8fb6571c2', 'nickname_kar2', '', -1, 101,
        '2024-11-25 10:57:42', '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1');
/*!40000 ALTER TABLE `base_user_info_0`
    ENABLE KEYS */;
UNLOCK TABLES;

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
VALUES (250506160235021405, 0, '测试房间1', '2025-05-06 16:02:35', '841891'),
       (250612215755101754, 240902154357005281, 'test', '2025-06-12 21:57:56', '234643');
/*!40000 ALTER TABLE `base_live_room`
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
        '2024-11-25 09:26:43', 1, '', '基础:请求详情:查看详情', 'baseRequestInfo:infoById');
/*!40000 ALTER TABLE `base_auth`
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
       (250516090553035041, 0, '2025-05-16 09:05:53', 0, '2025-05-16 09:16:04', 1, '', '实时房间',
        '/live', 'ep:video-camera', 0, 1, 0, '', '', 9500, 'Live'),
       (250516091519035092, 0, '2025-05-16 09:15:20', 0, '2025-05-16 09:15:27', 1, '', '房间管理',
        '/live/self/liveRoomSelf', 'ri:settings-2-fill', 250516090553035041, 1, 0, '', '', 9400,
        'BaseLiveRoomSelf'),
       (250516091721035107, 0, '2025-05-16 09:17:21', 0, '2025-05-16 09:17:21', 1, '', '当前房间',
        '/live/self/liveRoomUserSelf', 'ri:play-circle-fill', 250516090553035041, 1, 0, '', '',
        9300, 'BaseLiveRoomUserSelf');
/*!40000 ALTER TABLE `base_menu`
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
       (240830160647004402, 250516090553035041),
       (240830160647004402, 250516091519035092),
       (240830160647004402, 250516091721035107);
/*!40000 ALTER TABLE `base_role_ref_menu`
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

LOCK TABLES `base_sql_slow` WRITE;
/*!40000 ALTER TABLE `base_sql_slow`
    DISABLE KEYS */;
INSERT INTO `base_sql_slow`
VALUES (250508092624028342, -1, '2025-05-08 09:26:22',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1754,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250508092624028343, -1, '2025-05-08 09:26:22',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.deleteByIds', 'DELETE', 327,
        'DELETE FROM base_socket WHERE id IN ( 250507161128023261 )'),
       (250508100428028953, -1, '2025-05-08 10:04:25',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2339,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250508102541029162, -1, '2025-05-08 10:25:39',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1596,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250508103802029275, -1, '2025-05-08 10:38:00',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1389,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250508103802029276, -1, '2025-05-08 10:38:01',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 291,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002))'),
       (250508104742029382, -1, '2025-05-08 10:47:40',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1460,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250508143559029492, -1, '2025-05-08 14:35:55',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6687,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250508143559029493, -1, '2025-05-08 14:35:56',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 208,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250508150120029602, -1, '2025-05-08 15:01:17',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2497,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509084448029712, -1, '2025-05-09 08:44:44',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 8273,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250509084450029822, -1, '2025-05-09 08:44:48',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1643,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509085653030532, -1, '2025-05-09 08:56:51',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1653,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509090131030642, -1, '2025-05-09 09:01:29',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1462,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509090357030755, -1, '2025-05-09 09:03:55',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1403,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509090925030862, -1, '2025-05-09 09:09:23',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1863,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509090925030863, -1, '2025-05-09 09:09:24',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 243,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002))'),
       (250509091108030972, -1, '2025-05-09 09:11:06',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1834,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509092137031082, -1, '2025-05-09 09:21:35',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2150,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250509134533031992, -1, '2025-05-09 13:45:30',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5437,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250509134533031993, -1, '2025-05-09 13:45:31',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 219,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250509084445029711) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250509134550032102, -1, '2025-05-09 13:45:48',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1783,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250513135806033212, -1, '2025-05-13 13:58:02',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 8887,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250513135806033213, -1, '2025-05-13 13:58:03',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 217,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250509134531031991) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250513144934033322, -1, '2025-05-13 14:49:30',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 4876,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250513151353033432, -1, '2025-05-13 15:13:50',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 12806,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250513151358033433, -1, '2025-05-13 15:13:53',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 235,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-13 15:13:42\' AND enable_flag = true AND status = PUBLICITY)'),
       (250513151358033434, -1, '2025-05-13 15:13:53',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 214,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-13 14:43:52\')'),
       (250513151413033435, -1, '2025-05-13 15:14:12',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper.selectList', 'SELECT', 211,
        'SELECT id,room_id,create_time,create_ts,data,create_id,media_type FROM base_live_room_data ORDER BY create_ts ASC'),
       (250513151418033436, -1, '2025-05-13 15:14:14',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 253,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-13 15:14:03\' AND enable_flag = true AND status = PUBLICITY)'),
       (250513151549033446, 0, '2025-05-13 15:15:44',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 229,
        'SELECT id,scheme,host,port,path,type,mac_address,enable_flag,remark,create_id,create_time,update_id,update_time FROM base_socket WHERE (type = WEB_SOCKET AND enable_flag = true)'),
       (250513151930033735, -1, '2025-05-13 15:19:25',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 249,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250513151918033716, 0, \'2025-05-13 15:19:18\', \'/base/liveRoomData/addData\', 193, \'新增数据\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'WEB_SOCKET\' )'),
       (250513154755033914, -1, '2025-05-13 15:47:50',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 477,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-13 15:47:39\' AND enable_flag = true AND status = PUBLICITY)'),
       (250513154755033915, -1, '2025-05-13 15:47:53',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper.selectList', 'SELECT', 210,
        'SELECT id,room_id,create_time,create_ts,data,create_id,media_type FROM base_live_room_data ORDER BY create_ts ASC'),
       (250513154820033917, -1, '2025-05-13 15:48:16',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 458,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-13 15:48:05\' AND enable_flag = true AND status = PUBLICITY)'),
       (250513164404034050, -1, '2025-05-13 16:44:02',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper.selectList', 'SELECT', 609,
        'SELECT id,room_id,create_time,create_ts,data,create_id,media_type FROM base_live_room_data ORDER BY create_ts ASC'),
       (250513220913034142, -1, '2025-05-13 22:09:10',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5782,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250513220913034143, -1, '2025-05-13 22:09:11',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 207,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250513135803033211) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250515100115034252, -1, '2025-05-15 10:01:12',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 9248,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250515100116034253, -1, '2025-05-15 10:01:13',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 221,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250513220911034141) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250515100120034254, -1, '2025-05-15 10:01:15',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 205,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-15 09:31:14\')'),
       (250515134126034362, -1, '2025-05-15 13:41:23',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5828,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250515134154034471, -1, '2025-05-15 13:41:49',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        6069,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515134154034472, -1, '2025-05-15 13:41:49',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 5981,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-15 13:11:42\')'),
       (250515134154034473, -1, '2025-05-15 13:41:49',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT',
        5982,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-15 13:41:32\' AND enable_flag = true AND status = PUBLICITY)'),
       (250515134656034582, -1, '2025-05-15 13:46:52',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 3773,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250515134720034583, -1, '2025-05-15 13:47:18',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        377,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515140758034692, -1, '2025-05-15 14:07:55',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 3540,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250515140818034694, -1, '2025-05-15 14:08:13',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        344,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515140818034695, -1, '2025-05-15 14:08:13',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 307,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-15 14:08:02\' AND enable_flag = true AND status = PUBLICITY)'),
       (250515140823034696, -1, '2025-05-15 14:08:20',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        626,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515140828034697, -1, '2025-05-15 14:08:23',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        214,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515140838034698, -1, '2025-05-15 14:08:36',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        291,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515143510034699, -1, '2025-05-15 14:35:08',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        686,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515144005034700, -1, '2025-05-15 14:40:00',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        243,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515144101034701, -1, '2025-05-15 14:41:00',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        261,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515144517034702, -1, '2025-05-15 14:45:14',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        534,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250515144754034703, -1, '2025-05-15 14:47:52',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT',
        2206,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-15 14:47:39\' AND enable_flag = true AND status = PUBLICITY)'),
       (250515151756034704, -1, '2025-05-15 15:17:50',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 329,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-15 15:17:39\' AND enable_flag = true AND status = PUBLICITY)'),
       (250515151955034705, -1, '2025-05-15 15:19:53',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 319,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-15 15:19:42\' AND enable_flag = true AND status = PUBLICITY)'),
       (250515152925034802, -1, '2025-05-15 15:29:21',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 13538,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516090114034912, -1, '2025-05-16 09:01:11',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 8209,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250516090114034913, -1, '2025-05-16 09:01:12',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 205,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250515134123034361) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250516090356035022, -1, '2025-05-16 09:03:53',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 7023,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516090441035031, -1, '2025-05-16 09:04:38',
        'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.selectList', 'SELECT', 298,
        'SELECT avatar_file_id,nickname,bio FROM base_user_info WHERE (id = 0)'),
       (250516090441035032, -1, '2025-05-16 09:04:38',
        'com.kar20240901.be.base.web.mapper.base.BaseUserMapper.selectList', 'SELECT', 254,
        'SELECT email,password,username,phone,wx_open_id,create_time,wx_app_id FROM base_user WHERE (id = 0)'),
       (250516091212035072, -1, '2025-05-16 09:12:09',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        251,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250516091523035094, 0, '2025-05-16 09:15:20',
        'com.kar20240901.be.base.web.mapper.base.BaseMenuMapper.selectCount', 'SELECT', 233,
        'SELECT COUNT( * ) AS total FROM base_menu WHERE (uuid = \'BaseLiveRoomSelf\')'),
       (250516091809035125, 0, '2025-05-16 09:18:05',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper.selectList', 'SELECT', 421,
        'SELECT id, belong_id, name, create_time, code FROM base_live_room WHERE (belong_id = 0) ORDER BY create_time DESC LIMIT 15'),
       (250516091809035126, 0, '2025-05-16 09:18:05',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper.selectList', 'SELECT', 421,
        'SELECT id, belong_id, name, create_time, code FROM base_live_room WHERE (belong_id = 0) ORDER BY create_time DESC LIMIT 15'),
       (250516091950035158, -1, '2025-05-16 09:19:47',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        1687,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250516142718035332, -1, '2025-05-16 14:27:15',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5925,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250516142718035333, -1, '2025-05-16 14:27:16',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 518,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250516090111034911) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250516143508035442, -1, '2025-05-16 14:35:05',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5076,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516144510035726, -1, '2025-05-16 14:45:07',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 250,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-16 14:15:07\')'),
       (250516153245035752, -1, '2025-05-16 15:32:40',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 4211,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516153636035862, -1, '2025-05-16 15:36:31',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 3546,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516153907035972, -1, '2025-05-16 15:39:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 3811,
        'SELECT id FROM base_socket WHERE (mac_address = \'fa-26-3e-68-87-1d\' AND port = 8002)'),
       (250516153927036006, 0, '2025-05-16 15:39:25',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 268,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250516154330036195, -1, '2025-05-16 15:43:27',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 223,
        'SELECT user_id,socket_ref_user_id FROM base_live_room_user WHERE (id IN (250516153921035979))'),
       (250516155007036282, -1, '2025-05-16 15:50:05',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2218,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250516155337036392, -1, '2025-05-16 15:53:35',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1472,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250516164404037002, -1, '2025-05-16 16:44:00',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5379,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250516170417037112, -1, '2025-05-16 17:04:13',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6558,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250516170417037113, -1, '2025-05-16 17:04:14',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 265,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250516164401037001) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250516170506037222, -1, '2025-05-16 17:05:03',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5345,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250516170511037223, -1, '2025-05-16 17:05:05',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 285,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-16 16:35:04\')'),
       (250516170511037224, -1, '2025-05-16 17:05:06',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        575,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250516170511037225, -1, '2025-05-16 17:05:06',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 482,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-16 17:04:55\' AND enable_flag = true AND status = PUBLICITY)'),
       (250516171336037370, 250516171249037345, '2025-05-16 17:13:33',
        'com.kar20240901.be.base.web.mapper.base.BaseMenuMapper.getMenuListByUserId', 'SELECT', 278,
        'SELECT a.id AS id, a.pid AS pid, a.name AS name, a.path AS path, a.uuid AS uuid, a.redirect AS redirect, a.router AS router, a.show_flag AS showFlag, a.icon AS icon, a.order_no AS orderNo FROM base_menu a WHERE a.enable_flag = TRUE AND a.id IN (SELECT subA.menu_id FROM base_role_ref_menu subA WHERE subA.role_id IN ( ( SELECT subSubA.role_id as roleId FROM base_role_ref_user subSubA WHERE subSubA.user_id = 250516171249037345 ) UNION ALL ( SELECT subSubA.id as roleId FROM base_role subSubA WHERE subSubA.enable_flag = true AND subSubA.default_flag = true ) )) ORDER BY a.order_no DESC , a.id'),
       (250523103800037732, -1, '2025-05-23 10:37:57',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6013,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523103800037733, -1, '2025-05-23 10:37:58',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 497,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250516170503037221) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250523103805037734, -1, '2025-05-23 10:38:00',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 466,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 10:07:59\')'),
       (250523103805037735, -1, '2025-05-23 10:38:00',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        805,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523103805037736, -1, '2025-05-23 10:38:00',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 848,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 10:37:49\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523113823038042, -1, '2025-05-23 11:38:20',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5494,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523113828038043, -1, '2025-05-23 11:38:23',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        369,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523113828038044, -1, '2025-05-23 11:38:23',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 325,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 11:08:22\')'),
       (250523113828038045, -1, '2025-05-23 11:38:23',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 456,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 11:38:12\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523114812038152, -1, '2025-05-23 11:48:08',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5693,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523114812038153, -1, '2025-05-23 11:48:10',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 262,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250523113821038041) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250523114817038154, -1, '2025-05-23 11:48:11',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 261,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 11:18:10\')'),
       (250523114817038155, -1, '2025-05-23 11:48:11',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        375,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523114817038156, -1, '2025-05-23 11:48:11',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 332,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 11:48:00\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523115127038186, -1, '2025-05-23 11:51:23',
        'com.kar20240901.be.base.web.mapper.base.BaseAuthMapper.getAuthSetByUserId', 'SELECT', 268,
        'SELECT a.auth FROM base_auth a WHERE a.enable_flag = TRUE AND a.id IN (SELECT subA.auth_id FROM base_role_ref_auth subA WHERE subA.role_id IN ( ( SELECT subSubA.role_id as roleId FROM base_role_ref_user subSubA WHERE subSubA.user_id = 250523115114038175 ) UNION ALL ( SELECT subSubA.id as roleId FROM base_role subSubA WHERE subSubA.enable_flag = true AND subSubA.default_flag = true ) ))'),
       (250523160342039862, -1, '2025-05-23 16:03:38',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 4813,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523160342039863, -1, '2025-05-23 16:03:39',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 213,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250523114809038151) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250523160347039864, -1, '2025-05-23 16:03:41',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 381,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 15:33:40\')'),
       (250523160347039865, -1, '2025-05-23 16:03:41',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        544,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523160347039866, -1, '2025-05-23 16:03:41',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 415,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 16:03:30\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523165426040082, -1, '2025-05-23 16:54:24',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5249,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523165431040083, -1, '2025-05-23 16:54:25',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 480,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250523165349039971) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250523165431040084, -1, '2025-05-23 16:54:26',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        629,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523165431040085, -1, '2025-05-23 16:54:26',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 359,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 16:24:24\')'),
       (250523165431040086, -1, '2025-05-23 16:54:26',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 713,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 16:54:14\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523165736040192, -1, '2025-05-23 16:57:31',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 7637,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250523165736040193, -1, '2025-05-23 16:57:31',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        837,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250523165736040194, -1, '2025-05-23 16:57:32',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 754,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-23 16:57:20\' AND enable_flag = true AND status = PUBLICITY)'),
       (250523165736040195, -1, '2025-05-23 16:57:32',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 686,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-23 16:27:30\')'),
       (250526090738040302, -1, '2025-05-26 09:07:35',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5594,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250526090738040303, -1, '2025-05-26 09:07:36',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 272,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250523165732040191) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250526090743040304, -1, '2025-05-26 09:07:37',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 223,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-26 08:37:36\')'),
       (250526090743040305, -1, '2025-05-26 09:07:37',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        398,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250526090743040306, -1, '2025-05-26 09:07:37',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 352,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-26 09:07:26\' AND enable_flag = true AND status = PUBLICITY)'),
       (250526093321040412, -1, '2025-05-26 09:33:19',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5762,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250526093321040413, -1, '2025-05-26 09:33:20',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 460,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250526090735040301) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250526093326040414, -1, '2025-05-26 09:33:21',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 368,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-05-26 09:03:20\')'),
       (250526093326040415, -1, '2025-05-26 09:33:21',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        586,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250526093326040416, -1, '2025-05-26 09:33:21',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 555,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-26 09:33:10\' AND enable_flag = true AND status = PUBLICITY)'),
       (250526094239040522, -1, '2025-05-26 09:42:36',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5655,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250526094239040523, -1, '2025-05-26 09:42:37',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 219,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250526093319040411) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250526094244040529, -1, '2025-05-26 09:42:38',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        390,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250526094244040530, -1, '2025-05-26 09:42:38',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 298,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-05-26 09:42:27\' AND enable_flag = true AND status = PUBLICITY)'),
       (250530090342043932, -1, '2025-05-30 09:03:41',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1951,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601083302044042, -1, '2025-06-01 08:32:59',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5985,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250601083307044043, -1, '2025-06-01 08:33:01',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 365,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-01 08:03:00\')'),
       (250601083307044044, -1, '2025-06-01 08:33:01',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        371,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601083307044045, -1, '2025-06-01 08:33:01',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 380,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-01 08:32:50\' AND enable_flag = true AND status = PUBLICITY)'),
       (250601084002044152, -1, '2025-06-01 08:40:00',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1645,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601091745049962, -1, '2025-06-01 09:17:43',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 4678,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601091750049966, 0, '2025-06-01 09:17:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 368,
        'SELECT id,scheme,host,port,path,type,mac_address,enable_flag,remark,create_id,create_time,update_id,update_time FROM base_socket WHERE (type = WEB_SOCKET AND enable_flag = true)'),
       (250601091750049967, -1, '2025-06-01 09:17:48',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        256,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601092027049987, -1, '2025-06-01 09:20:17',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        4235,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601092027049988, -1, '2025-06-01 09:20:20',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT',
        5510,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-01 09:19:59\' AND enable_flag = true AND status = PUBLICITY)'),
       (250601092027049989, -1, '2025-06-01 09:20:20',
        'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.updateById', 'UPDATE', 5510,
        'UPDATE base_user_info SET last_active_time=\'2025-06-01 09:19:50\', last_ip=\'0:0:0:0:0:0:0:1\', last_region=\'ipv6#0:0:0:0:0:0:0:1\' WHERE id=0'),
       (250601092027049990, -1, '2025-06-01 09:20:20',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 4234,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250601091950049986, 0, \'2025-06-01 09:19:50\', \'/base/liveRoomUserSelf/page\', 601, \'分页排序查询\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'\' )'),
       (250601092109049991, -1, '2025-06-01 09:20:51',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestInfoMapper.insert', 'INSERT', 16131,
        'INSERT INTO base_request_info ( id, uri, cost_ms, success_flag, error_msg, request_param, response_value ) VALUES ( 250601091950049986, \'/base/liveRoomUserSelf/page\', 601, true, \'\', \'{\"BaseLiveRoomUserSelfPageDTO\":\"{\\\"roomId\\\":250506160235021405,\\\"current\\\":1,\\\"pageSize\\\":15}\"}\', \'{\"code\":200,\"msg\":\"操作成功\",\"receive\":true,\"data\":{\"records\":[{\"id\":250601091949049984,\"userId\":0,\"socketRefUserId\":250601091938049983,\"createTime\":1748740789000,\"nickname\":\"admin\"}],\"total\":1,\"size\":15,\"current\":1},\"service\":\"be-base-web-20240901\"}\' )'),
       (250601092113049992, -1, '2025-06-01 09:20:51',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        16137,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601092508049993, -1, '2025-06-01 09:21:16',
        'com.kar20240901.be.base.web.mapper.sql.BaseSqlSlowMapper.insert', 'INSERT', 1291,
        'INSERT INTO base_sql_slow ( id, create_id, create_time, name, type, cost_ms, sql_content ) VALUES ( 250601092109049991, -1, \'2025-06-01 09:20:51\', \'com.kar20240901.be.base.web.mapper.request.BaseRequestInfoMapper.insert\', \'INSERT\', 16131, \'INSERT INTO base_request_info ( id, uri, cost_ms, success_flag, error_msg, request_param, response_value ) VALUES ( 250601091950049986, \'/base/liveRoomUserSelf/page\', 601, true, \'\', \'{\"BaseLiveRoomUserSelfPageDTO\":\"{\\\"roomId\\\":250506160235021405,\\\"current\\\":1,\\\"pageSize\\\":15}\"}\', \'{\"code\":200,\"msg\":\"操作成功\",\"receive\":true,\"data\":{\"records\":[{\"id\":250601091949049984,\"userId\":0,\"socketRefUserId\":250601091938049983,\"createTime\":1748740789000,\"nickname\":\"admin\"}],\"total\":1,\"size\":15,\"current\":1},\"service\":\"be-base-web-20240901\"}\' )\' )'),
       (250601094547050072, -1, '2025-06-01 09:45:46',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1282,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601094621050182, -1, '2025-06-01 09:46:19',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1214,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601095044050292, -1, '2025-06-01 09:50:42',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1296,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601095349050402, -1, '2025-06-01 09:53:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1262,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601095446050415, -1, '2025-06-01 09:54:20',
        'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.updateById', 'UPDATE', 2593,
        'UPDATE base_user_info SET last_active_time=\'2025-06-01 09:54:03\', last_ip=\'0:0:0:0:0:0:0:1\', last_region=\'ipv6#0:0:0:0:0:0:0:1\' WHERE id=0'),
       (250601095449050417, -1, '2025-06-01 09:54:20',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 2592,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250601095403050413, 0, \'2025-06-01 09:54:03\', \'/base/liveRoomData/addData\', 421, \'新增数据\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'WEB_SOCKET\' )'),
       (250601095457050418, -1, '2025-06-01 09:54:49',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        2507,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601095457050419, -1, '2025-06-01 09:54:56',
        'com.kar20240901.be.base.web.mapper.sql.BaseSqlSlowMapper.insert', 'INSERT', 2502,
        'INSERT INTO base_sql_slow ( id, create_id, create_time, name, type, cost_ms, sql_content ) VALUES ( 250601095446050415, -1, \'2025-06-01 09:54:20\', \'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.updateById\', \'UPDATE\', 2593, \'UPDATE base_user_info SET last_active_time=\'2025-06-01 09:54:03\', last_ip=\'0:0:0:0:0:0:0:1\', last_region=\'ipv6#0:0:0:0:0:0:0:1\' WHERE id=0\' )'),
       (250601095502050420, -1, '2025-06-01 09:54:56',
        'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.updateById', 'UPDATE', 6217,
        'UPDATE base_user_info SET last_active_time=\'2025-06-01 09:54:20\', last_ip=\'0:0:0:0:0:0:0:1\', last_region=\'ipv6#0:0:0:0:0:0:0:1\' WHERE id=0'),
       (250601095502050421, -1, '2025-06-01 09:54:56',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestInfoMapper.insert', 'INSERT', 6216,
        'INSERT INTO base_request_info ( id, uri, cost_ms, success_flag, error_msg, request_param, response_value ) VALUES ( 250601095400050410, \'/base/liveRoomUser/addUser\', 347, true, \'\', \'{\"uri\":\"/base/liveRoomUser/addUser\",\"data\":{\"id\":\"250506160235021405\",\"belongId\":\"0\",\"name\":\"测试房间1\",\"createTime\":\"2025-05-06 16:02:35\",\"code\":\"841891\"}}\', \'{\"uri\":\"/base/liveRoomUser/addUser\",\"data\":\"250506160235021405\",\"code\":200,\"service\":\"be-base-web-20240901\"}\' )'),
       (250601095502050422, -1, '2025-06-01 09:54:56',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 6215,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250601095420050414, 0, \'2025-06-01 09:54:20\', \'/netty/webSocket/heartBeat\', 2, \'心跳检测\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'WEB_SOCKET\' )'),
       (250601102436060448, -1, '2025-06-01 10:24:30',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        9131,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250601103459060540, -1, '2025-06-01 10:34:53',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 2699,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250601103443060533, 0, \'2025-06-01 10:34:43\', \'/base/liveRoomSelf/page\', 204, \'分页排序查询\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'\' )'),
       (250601104406060712, -1, '2025-06-01 10:44:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2147,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250601105042067005, 250523115114038175, '2025-06-01 10:50:39',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 210,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250605095053073822, -1, '2025-06-05 09:50:50',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5369,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250605095058073823, -1, '2025-06-05 09:50:52',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 385,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-05 09:20:51\')'),
       (250605095058073824, -1, '2025-06-05 09:50:52',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 498,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-05 09:50:41\' AND enable_flag = true AND status = PUBLICITY)'),
       (250605095058073825, -1, '2025-06-05 09:50:52',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        564,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610084905073932, -1, '2025-06-10 08:49:02',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5773,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250610084905073933, -1, '2025-06-10 08:49:03',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 304,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250605095050073821) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250610084910073934, -1, '2025-06-10 08:49:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.insert', 'INSERT', 248,
        'INSERT INTO base_socket ( id, scheme, host, port, path, type, mac_address, enable_flag, remark, create_id, create_time, update_id, update_time ) VALUES ( 250610084903073931, \'ws://\', \'119.45.23.254\', 80, \'/ws\', WEB_SOCKET, \'02-42-ac-15-50-01\', true, \'\', -1, \'2025-06-10 08:49:03\', -1, \'2025-06-10 08:49:03\' )'),
       (250610084910073935, -1, '2025-06-10 08:49:05',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 306,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-10 08:19:04\')'),
       (250610084910073936, -1, '2025-06-10 08:49:05',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        351,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610084910073937, -1, '2025-06-10 08:49:05',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 392,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-10 08:48:54\' AND enable_flag = true AND status = PUBLICITY)'),
       (250610085349074042, -1, '2025-06-10 08:53:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1951,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250610090817074279, -1, '2025-06-10 09:08:12',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        264,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610091233074290, -1, '2025-06-10 09:12:28',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 380,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-10 09:12:17\' AND enable_flag = true AND status = PUBLICITY)'),
       (250610145540082152, -1, '2025-06-10 14:55:37',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5687,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250610145545082153, -1, '2025-06-10 14:55:39',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 249,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-10 14:25:38\')'),
       (250610145545082154, -1, '2025-06-10 14:55:39',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        439,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610145545082155, -1, '2025-06-10 14:55:39',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 345,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-10 14:55:28\' AND enable_flag = true AND status = PUBLICITY)'),
       (250610224802082262, -1, '2025-06-10 22:48:00',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5535,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250610224807082263, -1, '2025-06-10 22:48:02',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 507,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250610145537082151) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250610224807082264, -1, '2025-06-10 22:48:02',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        555,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610224807082265, -1, '2025-06-10 22:48:02',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 417,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-10 22:18:00\')'),
       (250610224807082266, -1, '2025-06-10 22:48:02',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 623,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-10 22:47:50\' AND enable_flag = true AND status = PUBLICITY)'),
       (250610230212082372, -1, '2025-06-10 23:02:10',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2104,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250610230242082268, -1, '2025-06-10 23:02:38',
        'com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper.updateById', 'UPDATE', 278,
        'UPDATE base_user_info SET last_active_time=\'2025-06-10 23:02:32\', last_ip=\'106.91.90.7\', last_region=\'中国|0|重庆|重庆市|电信\' WHERE id=250523115114038175'),
       (250610231328097135, -1, '2025-06-10 23:13:24',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.insert', 'INSERT', 264,
        'INSERT INTO base_request ( id, create_id, create_time, uri, cost_ms, name, category, ip, region, success_flag, type ) VALUES ( 250610231317096766, 250523115114038175, \'2025-06-10 23:13:17\', \'/base/liveRoomData/addData\', 107, \'新增数据\', PC_BROWSER_WINDOWS, \'0:0:0:0:0:0:0:1\', \'ipv6#0:0:0:0:0:0:0:1\', true, \'WEB_SOCKET\' )'),
       (250610231358098048, 0, '2025-06-10 23:13:54',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 209,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250610231358098049, 250523115114038175, '2025-06-10 23:13:54',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 208,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250610231409098331, 0, '2025-06-10 23:14:04',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 201,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250610231409098332, 250523115114038175, '2025-06-10 23:14:04',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.selectList', 'SELECT', 235,
        'SELECT socket_ref_user_id,user_id FROM base_live_room_user WHERE (room_id = 250506160235021405)'),
       (250610232757101382, -1, '2025-06-10 23:27:55',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5215,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250610232757101383, -1, '2025-06-10 23:27:56',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 229,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250610232802101384, -1, '2025-06-10 23:27:57',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 397,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-10 22:57:56\')'),
       (250610232802101385, -1, '2025-06-10 23:27:57',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        580,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250610232802101386, -1, '2025-06-10 23:27:57',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 571,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-10 23:27:46\' AND enable_flag = true AND status = PUBLICITY)'),
       (250612094143101492, -1, '2025-06-12 09:41:40',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5515,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250612094148101493, -1, '2025-06-12 09:41:42',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 323,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-12 09:11:41\')'),
       (250612094148101494, -1, '2025-06-12 09:41:42',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        404,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250612094148101495, -1, '2025-06-12 09:41:42',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 386,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-12 09:41:31\' AND enable_flag = true AND status = PUBLICITY)'),
       (250612094413101602, -1, '2025-06-12 09:44:10',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5584,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250612094418101603, -1, '2025-06-12 09:44:12',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 441,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-12 09:14:11\')'),
       (250612094418101604, -1, '2025-06-12 09:44:12',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        527,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250612094418101605, -1, '2025-06-12 09:44:12',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 620,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-12 09:44:01\' AND enable_flag = true AND status = PUBLICITY)'),
       (250612215610101712, -1, '2025-06-12 21:56:07',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6163,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250612215610101713, -1, '2025-06-12 21:56:08',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 259,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250612094410101601) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250612215615101715, -1, '2025-06-12 21:56:09',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 363,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-12 21:26:08\')'),
       (250612215615101716, -1, '2025-06-12 21:56:09',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        455,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250612215615101717, -1, '2025-06-12 21:56:09',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 400,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-12 21:55:58\' AND enable_flag = true AND status = PUBLICITY)'),
       (250612215750101752, -1, '2025-06-12 21:57:44',
        'com.kar20240901.be.base.web.mapper.base.BaseAuthMapper.getAuthSetByUserId', 'SELECT', 229,
        'SELECT a.auth FROM base_auth a WHERE a.enable_flag = TRUE AND a.id IN (SELECT subA.auth_id FROM base_role_ref_auth subA WHERE subA.role_id IN ( ( SELECT subSubA.role_id as roleId FROM base_role_ref_user subSubA WHERE subSubA.user_id = 240902154357005281 ) UNION ALL ( SELECT subSubA.id as roleId FROM base_role subSubA WHERE subSubA.enable_flag = true AND subSubA.default_flag = true ) ))'),
       (250613161101101822, -1, '2025-06-13 16:10:59',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2181,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250613161121101932, -1, '2025-06-13 16:11:17',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6659,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250613161121101933, -1, '2025-06-13 16:11:18',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 236,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250612215608101711) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250613161126101934, -1, '2025-06-13 16:11:20',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 370,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-13 15:41:19\')'),
       (250613161126101935, -1, '2025-06-13 16:11:20',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        471,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250613161126101936, -1, '2025-06-13 16:11:20',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 449,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-13 16:11:09\' AND enable_flag = true AND status = PUBLICITY)'),
       (250616111539102042, -1, '2025-06-16 11:15:37',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5452,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250616111544102043, -1, '2025-06-16 11:15:38',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 894,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250613161118101931) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250616111544102044, -1, '2025-06-16 11:15:39',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 628,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-16 10:45:38\')'),
       (250616111544102045, -1, '2025-06-16 11:15:39',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        856,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250616111544102046, -1, '2025-06-16 11:15:39',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 988,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-16 11:15:28\' AND enable_flag = true AND status = PUBLICITY)'),
       (250616135354102152, -1, '2025-06-16 13:53:51',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5982,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250616135359102153, -1, '2025-06-16 13:53:54',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 283,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-16 13:23:53\')'),
       (250616135359102154, -1, '2025-06-16 13:53:54',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        468,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250616135359102155, -1, '2025-06-16 13:53:54',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 415,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-16 13:53:43\' AND enable_flag = true AND status = PUBLICITY)'),
       (250616151912102262, -1, '2025-06-16 15:19:10',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5949,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250616151917102263, -1, '2025-06-16 15:19:11',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        554,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250616151917102264, -1, '2025-06-16 15:19:12',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 391,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-16 14:49:10\')'),
       (250616151917102265, -1, '2025-06-16 15:19:12',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 438,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-16 15:19:00\' AND enable_flag = true AND status = PUBLICITY)'),
       (250617084455102572, -1, '2025-06-17 08:44:52',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5909,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250617084455102573, -1, '2025-06-17 08:44:53',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 223,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250616151911102261) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250617084500102574, -1, '2025-06-17 08:44:54',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 351,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-17 08:14:53\')'),
       (250617084500102575, -1, '2025-06-17 08:44:54',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        398,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250617084500102576, -1, '2025-06-17 08:44:54',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 446,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-17 08:44:43\' AND enable_flag = true AND status = PUBLICITY)'),
       (250618084509102682, -1, '2025-06-18 08:45:06',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6354,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250618084509102683, -1, '2025-06-18 08:45:08',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 248,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250617084452102571) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250618084514102684, -1, '2025-06-18 08:45:08',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        369,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250618084514102685, -1, '2025-06-18 08:45:08',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.insert', 'INSERT', 208,
        'INSERT INTO base_socket ( id, scheme, host, port, path, type, mac_address, enable_flag, remark, create_id, create_time, update_id, update_time ) VALUES ( 250618084507102681, \'ws://\', \'1.13.156.65\', 80, \'/ws\', WEB_SOCKET, \'02-42-ac-15-50-01\', true, \'\', -1, \'2025-06-18 08:45:07\', -1, \'2025-06-18 08:45:07\' )'),
       (250618084514102686, -1, '2025-06-18 08:45:08',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 429,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-18 08:44:57\' AND enable_flag = true AND status = PUBLICITY)'),
       (250618084514102687, -1, '2025-06-18 08:45:08',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 365,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-18 08:15:07\')'),
       (250618090606103092, -1, '2025-06-18 09:06:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6030,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250618090606103093, -1, '2025-06-18 09:06:05',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 204,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250618090611103094, -1, '2025-06-18 09:06:05',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.insert', 'INSERT', 295,
        'INSERT INTO base_socket ( id, scheme, host, port, path, type, mac_address, enable_flag, remark, create_id, create_time, update_id, update_time ) VALUES ( 250618090604103091, \'ws://\', \'1.13.156.65\', 80, \'/ws\', WEB_SOCKET, \'02-42-ac-15-50-01\', true, \'\', -1, \'2025-06-18 09:06:04\', -1, \'2025-06-18 09:06:04\' )'),
       (250618090611103095, -1, '2025-06-18 09:06:06',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 316,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-18 08:36:05\')'),
       (250618090611103096, -1, '2025-06-18 09:06:06',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        483,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250618090611103097, -1, '2025-06-18 09:06:06',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 400,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-18 09:05:55\' AND enable_flag = true AND status = PUBLICITY)'),
       (250618134838103202, -1, '2025-06-18 13:48:35',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5809,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250618134838103203, -1, '2025-06-18 13:48:36',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 345,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250618090604103091) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250618134843103204, -1, '2025-06-18 13:48:37',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 388,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-18 13:18:37\')'),
       (250618134843103205, -1, '2025-06-18 13:48:38',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 434,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-18 13:48:27\' AND enable_flag = true AND status = PUBLICITY)'),
       (250618134843103206, -1, '2025-06-18 13:48:38',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        580,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250618141358106579, 0, '2025-06-18 14:13:55',
        'com.kar20240901.be.base.web.mapper.file.BaseFileMapper.selectList', 'SELECT', 409,
        'SELECT id, create_time, file_size, show_file_name, pid, type, (CASE WHEN type = 101 THEN NULL WHEN type = 201 THEN (SELECT COALESCE(SUM(file_size), 0) FROM base_file subA WHERE subA.pid_path_str LIKE CONCAT(\'%|\', base_file.id, \'|%\')) ELSE NULL END) AS folderSize, upload_flag FROM base_file WHERE (belong_id = 0 AND pid = 0 AND upload_type = FILE_SYSTEM) ORDER BY create_time DESC'),
       (250620083559106712, -1, '2025-06-20 08:35:56',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5452,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250620083559106713, -1, '2025-06-20 08:35:57',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 253,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250618134836103201) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250620083604106714, -1, '2025-06-20 08:35:58',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 325,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-20 08:05:57\')'),
       (250620083604106715, -1, '2025-06-20 08:35:58',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 324,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-20 08:35:47\' AND enable_flag = true AND status = PUBLICITY)'),
       (250620083604106716, -1, '2025-06-20 08:35:58',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        330,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250620085001106822, -1, '2025-06-20 08:49:59',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1618,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250620085346106823, -1, '2025-06-20 08:53:44',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        219,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250621103747107732, -1, '2025-06-21 10:37:45',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5632,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250621103747107733, -1, '2025-06-21 10:37:46',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 270,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250620083557106711) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250621103752107734, -1, '2025-06-21 10:37:47',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        487,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250621103752107735, -1, '2025-06-21 10:37:47',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 501,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-06-21 10:37:36\' AND enable_flag = true AND status = PUBLICITY)'),
       (250621103752107736, -1, '2025-06-21 10:37:47',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 336,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-06-21 10:07:46\')'),
       (250621103752107737, -1, '2025-06-21 10:37:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.insert', 'INSERT', 225,
        'INSERT INTO base_socket ( id, scheme, host, port, path, type, mac_address, enable_flag, remark, create_id, create_time, update_id, update_time ) VALUES ( 250621103746107731, \'ws://\', \'175.27.234.66\', 80, \'/ws\', WEB_SOCKET, \'02-42-ac-15-50-01\', true, \'\', -1, \'2025-06-21 10:37:46\', -1, \'2025-06-21 10:37:46\' )'),
       (250621104011107842, -1, '2025-06-21 10:40:09',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 4546,
        'SELECT id FROM base_socket WHERE (mac_address = \'c0-b6-f9-a6-57-2f\' AND port = 8002)'),
       (250621104201107852, 0, '2025-06-21 10:41:59',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 204,
        'SELECT id,scheme,host,port,path,type,mac_address,enable_flag,remark,create_id,create_time,update_id,update_time FROM base_socket WHERE (type = WEB_SOCKET AND enable_flag = true)'),
       (250621105137107952, -1, '2025-06-21 10:51:36',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2626,
        'SELECT id FROM base_socket WHERE (mac_address = \'c0-b6-f9-a6-57-2f\' AND port = 8002)'),
       (250621105158107964, 0, '2025-06-21 10:51:53',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 259,
        'SELECT id,scheme,host,port,path,type,mac_address,enable_flag,remark,create_id,create_time,update_id,update_time FROM base_socket WHERE (type = WEB_SOCKET AND enable_flag = true)'),
       (250621105158107965, -1, '2025-06-21 10:51:53',
        'com.kar20240901.be.base.web.mapper.base.BaseUserMapper.selectList', 'SELECT', 206,
        'SELECT email,password,username,phone,wx_open_id,create_time,wx_app_id FROM base_user WHERE (id = 0)'),
       (250621110248115077, -1, '2025-06-21 11:02:45',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        227,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250621110323115080, -1, '2025-06-21 11:03:20',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        273,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250704085255117462, -1, '2025-07-04 08:52:52',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6049,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250704085255117463, -1, '2025-07-04 08:52:53',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 394,
        'DELETE FROM base_socket_ref_user WHERE ((mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250704085300117464, -1, '2025-07-04 08:52:54',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 492,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-07-04 08:22:53\')'),
       (250704085300117465, -1, '2025-07-04 08:52:55',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        580,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250704085300117466, -1, '2025-07-04 08:52:55',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 745,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-07-04 08:52:43\' AND enable_flag = true AND status = PUBLICITY)'),
       (250723082508117572, -1, '2025-07-23 08:25:05',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6033,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250723082508117573, -1, '2025-07-23 08:25:07',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 392,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250704085253117461) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250723082513117574, -1, '2025-07-23 08:25:07',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 242,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-07-23 07:55:07\')'),
       (250723082513117575, -1, '2025-07-23 08:25:08',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        448,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250723082513117576, -1, '2025-07-23 08:25:08',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 362,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-07-23 08:24:57\' AND enable_flag = true AND status = PUBLICITY)'),
       (250723084950117682, -1, '2025-07-23 08:49:49',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1342,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723085349117795, -1, '2025-07-23 08:53:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1900,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723085755117902, -1, '2025-07-23 08:57:54',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1160,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723090721118012, -1, '2025-07-23 09:07:20',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1204,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723092221118122, -1, '2025-07-23 09:22:19',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1093,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723092737118232, -1, '2025-07-23 09:27:35',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1136,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723103229118389, 0, '2025-07-23 10:32:26',
        'com.kar20240901.be.base.web.mapper.file.BaseFileMapper.selectList', 'SELECT', 809,
        'SELECT id, create_time, file_size, show_file_name, pid, type, (CASE WHEN type = 101 THEN NULL WHEN type = 201 THEN (SELECT COALESCE(SUM(file_size), 0) FROM base_file subA WHERE subA.pid_path_str LIKE CONCAT(\'%|\', base_file.id, \'|%\')) ELSE NULL END) AS folderSize, upload_flag FROM base_file WHERE (belong_id = 0 AND pid = 0 AND upload_type = FILE_SYSTEM) ORDER BY create_time DESC'),
       (250723103948118442, -1, '2025-07-23 10:39:47',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 1024,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250723104410118552, -1, '2025-07-23 10:44:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6096,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250723104410118553, -1, '2025-07-23 10:44:05',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        551,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250723104410118554, -1, '2025-07-23 10:44:05',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 402,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-07-23 10:14:03\')'),
       (250723104410118555, -1, '2025-07-23 10:44:05',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 555,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-07-23 10:43:53\' AND enable_flag = true AND status = PUBLICITY)'),
       (250723104825118574, 0, '2025-07-23 10:48:19',
        'com.kar20240901.be.base.web.mapper.request.BaseRequestMapper.selectList', 'SELECT', 212,
        'SELECT COUNT(1) AS count,(SUM(cost_ms) DIV COUNT(1)) AS avgMs FROM base_request WHERE (cost_ms > 1)'),
       (250811091345118662, -1, '2025-08-11 09:13:42',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 6784,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250811091345118663, -1, '2025-08-11 09:13:44',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 342,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250723104405118551) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250811091350118664, -1, '2025-08-11 09:13:44',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        394,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250811091350118665, -1, '2025-08-11 09:13:44',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 327,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-08-11 09:13:33\' AND enable_flag = true AND status = PUBLICITY)'),
       (250811091350118666, -1, '2025-08-11 09:13:44',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 280,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-08-11 08:43:43\')'),
       (250811155407118772, -1, '2025-08-11 15:54:04',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5138,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250811155412118773, -1, '2025-08-11 15:54:06',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 560,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250811091343118661) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250811155412118774, -1, '2025-08-11 15:54:06',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        642,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL'),
       (250811155412118775, -1, '2025-08-11 15:54:07',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 498,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-08-11 15:24:05\')'),
       (250811155412118776, -1, '2025-08-11 15:54:07',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 680,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-08-11 15:53:55\' AND enable_flag = true AND status = PUBLICITY)'),
       (250811155449118882, -1, '2025-08-11 15:54:46',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 2846,
        'SELECT id FROM base_socket WHERE (mac_address = \'00-e0-4c-68-08-fc\' AND port = 8002)'),
       (250812083926125092, -1, '2025-08-12 08:39:23',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper.selectList', 'SELECT', 5568,
        'SELECT id FROM base_socket WHERE (mac_address = \'02-42-ac-15-50-01\' AND port = 80)'),
       (250812083926125093, -1, '2025-08-12 08:39:24',
        'com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper.delete', 'DELETE', 252,
        'DELETE FROM base_socket_ref_user WHERE (socket_id IN (250811155406118771) OR (mac_address = \'02-42-ac-15-50-01\' AND port = 80))'),
       (250812083931125094, -1, '2025-08-12 08:39:25',
        'com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper.selectList', 'SELECT', 383,
        'SELECT id FROM base_user_wallet WHERE (withdrawable_pre_use_money > 0 AND update_time <= \'2025-08-12 08:09:24\')'),
       (250812083931125095, -1, '2025-08-12 08:39:25',
        'com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper.selectCount', 'SELECT', 396,
        'SELECT COUNT( * ) AS total FROM base_bulletin WHERE (publish_time >= \'2025-08-12 08:39:14\' AND enable_flag = true AND status = PUBLICITY)'),
       (250812083931125096, -1, '2025-08-12 08:39:25',
        'com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper.checkRoomUser', 'SELECT',
        531,
        'SELECT a.id FROM base_live_room_user a LEFT JOIN base_socket_ref_user b ON b.id = a.socket_ref_user_id WHERE b.id IS NULL');
/*!40000 ALTER TABLE `base_sql_slow`
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
    `dict_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典 key（不能重复），字典项要冗余这个 key，目的：方便操作',
    `name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典/字典项 名',
    `type`     int                                                          NOT NULL COMMENT '字典类型：1 字典 2 字典项',
    `value`    int                                                          NOT NULL COMMENT '字典项 value（数字 123...）备注：字典为 -1',
    `order_no` int                                                          NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '该参数的 uuid',
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

LOCK TABLES `base_socket_ref_user` WRITE;
/*!40000 ALTER TABLE `base_socket_ref_user`
    DISABLE KEYS */;
INSERT INTO `base_socket_ref_user`
VALUES (250811160203121540, 0, '2025-08-11 16:02:05', 0, '2025-08-11 16:02:05', 1, '', 0,
        250811155447118881, 'admin', 'ws://', 'localhost', 8002, '/ws', '00-e0-4c-68-08-fc', 201,
        101, '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1', 101),
       (250811160209121564, 240902154357005281, '2025-08-11 16:02:15', 240902154357005281,
        '2025-08-11 16:02:15', 1, '', 240902154357005281, 250811155447118881, 'nickname_kar1',
        'ws://', 'localhost', 8002, '/ws', '00-e0-4c-68-08-fc', 201, 101, '0:0:0:0:0:0:0:1',
        'ipv6#0:0:0:0:0:0:0:1', 101);
/*!40000 ALTER TABLE `base_socket_ref_user`
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

LOCK TABLES `base_file_auth` WRITE;
/*!40000 ALTER TABLE `base_file_auth`
    DISABLE KEYS */;
INSERT INTO `base_file_auth`
VALUES (250115150615007618, 0, '2025-01-15 15:06:16', 0, '2025-01-15 15:06:16', 1, '',
        250115150615007615, 0, 1, 1),
       (250115150615007619, 0, '2025-01-15 15:06:16', 0, '2025-01-15 15:06:16', 1, '',
        250115150615007614, 0, 1, 1),
       (250115150615007620, 0, '2025-01-15 15:06:16', 0, '2025-01-15 15:06:16', 1, '',
        250115150615007613, 0, 1, 1),
       (250115150615007621, 0, '2025-01-15 15:06:16', 0, '2025-01-15 15:06:16', 1, '',
        250115150615007616, 0, 1, 1),
       (250115150615007622, 0, '2025-01-15 15:06:16', 0, '2025-01-15 15:06:16', 1, '',
        250115150615007617, 0, 1, 1),
       (250115150627007663, 0, '2025-01-15 15:06:28', 0, '2025-01-15 15:06:28', 1, '',
        250115150627007658, 0, 1, 1),
       (250115150628007664, 0, '2025-01-15 15:06:28', 0, '2025-01-15 15:06:28', 1, '',
        250115150627007659, 0, 1, 1),
       (250115150628007665, 0, '2025-01-15 15:06:28', 0, '2025-01-15 15:06:28', 1, '',
        250115150627007660, 0, 1, 1),
       (250115150628007666, 0, '2025-01-15 15:06:28', 0, '2025-01-15 15:06:28', 1, '',
        250115150627007661, 0, 1, 1),
       (250115150628007667, 0, '2025-01-15 15:06:28', 0, '2025-01-15 15:06:28', 1, '',
        250115150627007662, 0, 1, 1),
       (250115150641007709, 0, '2025-01-15 15:06:42', 0, '2025-01-15 15:06:42', 1, '',
        250115150641007706, 0, 1, 1),
       (250115150641007710, 0, '2025-01-15 15:06:42', 0, '2025-01-15 15:06:42', 1, '',
        250115150641007704, 0, 1, 1),
       (250115150641007711, 0, '2025-01-15 15:06:42', 0, '2025-01-15 15:06:42', 1, '',
        250115150641007705, 0, 1, 1),
       (250115150641007712, 0, '2025-01-15 15:06:42', 0, '2025-01-15 15:06:42', 1, '',
        250115150641007708, 0, 1, 1),
       (250115150641007713, 0, '2025-01-15 15:06:42', 0, '2025-01-15 15:06:42', 1, '',
        250115150641007707, 0, 1, 1),
       (250115150702007758, 0, '2025-01-15 15:07:03', 0, '2025-01-15 15:07:03', 1, '',
        250115150702007755, 0, 1, 1),
       (250115150702007759, 0, '2025-01-15 15:07:03', 0, '2025-01-15 15:07:03', 1, '',
        250115150702007753, 0, 1, 1),
       (250115150702007760, 0, '2025-01-15 15:07:03', 0, '2025-01-15 15:07:03', 1, '',
        250115150702007754, 0, 1, 1),
       (250115150702007761, 0, '2025-01-15 15:07:03', 0, '2025-01-15 15:07:03', 1, '',
        250115150702007756, 0, 1, 1),
       (250115150702007762, 0, '2025-01-15 15:07:03', 0, '2025-01-15 15:07:03', 1, '',
        250115150702007757, 0, 1, 1),
       (250115150741007811, 0, '2025-01-15 15:07:41', 0, '2025-01-15 15:07:41', 1, '',
        250115150741007806, 0, 1, 1),
       (250115150741007812, 0, '2025-01-15 15:07:41', 0, '2025-01-15 15:07:41', 1, '',
        250115150741007807, 0, 1, 1),
       (250115150741007813, 0, '2025-01-15 15:07:41', 0, '2025-01-15 15:07:41', 1, '',
        250115150741007808, 0, 1, 1),
       (250115150741007814, 0, '2025-01-15 15:07:41', 0, '2025-01-15 15:07:41', 1, '',
        250115150741007809, 0, 1, 1),
       (250115150741007815, 0, '2025-01-15 15:07:41', 0, '2025-01-15 15:07:41', 1, '',
        250115150741007810, 0, 1, 1),
       (250115150816007862, 0, '2025-01-15 15:08:17', 0, '2025-01-15 15:08:17', 1, '',
        250115150816007857, 0, 1, 1),
       (250115150816007863, 0, '2025-01-15 15:08:17', 0, '2025-01-15 15:08:17', 1, '',
        250115150816007858, 0, 1, 1),
       (250115150816007864, 0, '2025-01-15 15:08:17', 0, '2025-01-15 15:08:17', 1, '',
        250115150816007859, 0, 1, 1),
       (250115150816007865, 0, '2025-01-15 15:08:17', 0, '2025-01-15 15:08:17', 1, '',
        250115150816007860, 0, 1, 1),
       (250115150816007866, 0, '2025-01-15 15:08:17', 0, '2025-01-15 15:08:17', 1, '',
        250115150816007861, 0, 1, 1),
       (250115151104007961, 0, '2025-01-15 15:11:04', 0, '2025-01-15 15:11:04', 1, '',
        250115151104007956, 0, 1, 1),
       (250115151104007962, 0, '2025-01-15 15:11:04', 0, '2025-01-15 15:11:04', 1, '',
        250115151104007958, 0, 1, 1),
       (250115151104007963, 0, '2025-01-15 15:11:04', 0, '2025-01-15 15:11:04', 1, '',
        250115151104007959, 0, 1, 1),
       (250115151104007964, 0, '2025-01-15 15:11:04', 0, '2025-01-15 15:11:04', 1, '',
        250115151104007957, 0, 1, 1),
       (250115151104007965, 0, '2025-01-15 15:11:04', 0, '2025-01-15 15:11:04', 1, '',
        250115151104007960, 0, 1, 1),
       (250115151142008015, 0, '2025-01-15 15:11:43', 0, '2025-01-15 15:11:43', 1, '',
        250115151142008009, 0, 1, 1),
       (250115151142008016, 0, '2025-01-15 15:11:43', 0, '2025-01-15 15:11:43', 1, '',
        250115151142008010, 0, 1, 1),
       (250115151142008017, 0, '2025-01-15 15:11:43', 0, '2025-01-15 15:11:43', 1, '',
        250115151142008011, 0, 1, 1),
       (250115151142008018, 0, '2025-01-15 15:11:43', 0, '2025-01-15 15:11:43', 1, '',
        250115151142008012, 0, 1, 1),
       (250115151142008019, 0, '2025-01-15 15:11:43', 0, '2025-01-15 15:11:43', 1, '',
        250115151142008013, 0, 1, 1),
       (250115151323008071, 0, '2025-01-15 15:13:23', 0, '2025-01-15 15:13:23', 1, '',
        250115151323008066, 0, 1, 1),
       (250115151323008072, 0, '2025-01-15 15:13:23', 0, '2025-01-15 15:13:23', 1, '',
        250115151323008067, 0, 1, 1),
       (250115151323008073, 0, '2025-01-15 15:13:23', 0, '2025-01-15 15:13:23', 1, '',
        250115151323008068, 0, 1, 1),
       (250115151323008074, 0, '2025-01-15 15:13:23', 0, '2025-01-15 15:13:23', 1, '',
        250115151323008069, 0, 1, 1),
       (250115151323008075, 0, '2025-01-15 15:13:23', 0, '2025-01-15 15:13:23', 1, '',
        250115151323008070, 0, 1, 1),
       (250115151339008105, 0, '2025-01-15 15:13:39', 0, '2025-01-15 15:13:39', 1, '',
        250115151338008104, 0, 1, 1),
       (250115151339008118, 0, '2025-01-15 15:13:39', 0, '2025-01-15 15:13:39', 1, '',
        250115151339008114, 0, 1, 1),
       (250115151339008119, 0, '2025-01-15 15:13:39', 0, '2025-01-15 15:13:39', 1, '',
        250115151339008115, 0, 1, 1),
       (250115151339008120, 0, '2025-01-15 15:13:39', 0, '2025-01-15 15:13:39', 1, '',
        250115151339008117, 0, 1, 1),
       (250115151339008121, 0, '2025-01-15 15:13:39', 0, '2025-01-15 15:13:39', 1, '',
        250115151339008116, 0, 1, 1),
       (250115164355008865, 0, '2025-01-15 16:43:55', 0, '2025-01-15 16:43:55', 1, '',
        250115164355008861, 0, 1, 1),
       (250115164355008866, 0, '2025-01-15 16:43:55', 0, '2025-01-15 16:43:55', 1, '',
        250115164355008860, 0, 1, 1),
       (250115164355008867, 0, '2025-01-15 16:43:55', 0, '2025-01-15 16:43:55', 1, '',
        250115164355008862, 0, 1, 1),
       (250115164355008868, 0, '2025-01-15 16:43:55', 0, '2025-01-15 16:43:55', 1, '',
        250115164355008863, 0, 1, 1),
       (250115164355008869, 0, '2025-01-15 16:43:55', 0, '2025-01-15 16:43:55', 1, '',
        250115164355008864, 0, 1, 1),
       (250115171000009117, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '',
        250115170959009114, 0, 1, 1),
       (250115171000009118, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '',
        250115170959009112, 0, 1, 1),
       (250115171000009119, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '',
        250115170959009113, 0, 1, 1),
       (250115171000009120, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '',
        250115170959009115, 0, 1, 1),
       (250115171000009121, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '',
        250115170959009116, 0, 1, 1),
       (250115171011009148, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '',
        250115171011009143, 0, 1, 1),
       (250115171011009149, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '',
        250115171011009145, 0, 1, 1),
       (250115171011009150, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '',
        250115171011009144, 0, 1, 1),
       (250115171011009151, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '',
        250115171011009146, 0, 1, 1),
       (250115171011009152, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '',
        250115171011009147, 0, 1, 1),
       (250115171026009180, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '',
        250115171026009175, 0, 1, 1),
       (250115171026009181, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '',
        250115171026009176, 0, 1, 1),
       (250115171026009182, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '',
        250115171026009177, 0, 1, 1),
       (250115171026009183, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '',
        250115171026009178, 0, 1, 1),
       (250115171026009184, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:26', 1, '',
        250115171026009179, 0, 1, 1),
       (250115171036009211, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '',
        250115171036009206, 0, 1, 1),
       (250115171036009212, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '',
        250115171036009207, 0, 1, 1),
       (250115171036009213, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '',
        250115171036009208, 0, 1, 1),
       (250115171036009214, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '',
        250115171036009209, 0, 1, 1),
       (250115171036009215, 0, '2025-01-15 17:10:37', 0, '2025-01-15 17:10:37', 1, '',
        250115171036009210, 0, 1, 1),
       (250115171143009248, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '',
        250115171143009243, 0, 1, 1),
       (250115171143009249, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '',
        250115171143009244, 0, 1, 1),
       (250115171143009250, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '',
        250115171143009245, 0, 1, 1),
       (250115171143009251, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '',
        250115171143009246, 0, 1, 1),
       (250115171143009252, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '',
        250115171143009247, 0, 1, 1);
/*!40000 ALTER TABLE `base_file_auth`
    ENABLE KEYS */;
UNLOCK TABLES;

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
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不允许修改',
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

LOCK TABLES `base_user_delete_log` WRITE;
/*!40000 ALTER TABLE `base_user_delete_log`
    DISABLE KEYS */;
INSERT INTO `base_user_delete_log`
VALUES (241127091209003814, 241127091209003814, '2024-11-27 09:13:57', 241127091209003814,
        '2024-11-27 09:13:57',
        '7535718fd25d4295b79deea899a51527/773257cb1303cb42b59a5cdc6152206de85827cfc0f823fdfa26e8a984b0da6aef0b3376079fd26b0779e0397a5bc3c1c13f55df5479424a0934de12c21befb3',
        '', 't2', '', '', '', 'b4b7546464ae4a0bab3a3111ae6c425a', '用户昵称HCHEVX', '',
        241127091243003832, '2024-11-27 09:12:09', 101),
       (241127091513003890, 241127091513003890, '2024-11-27 09:17:31', 241127091513003890,
        '2024-11-27 09:17:31', '', 'c1677664380@gmail.com', 't1', '', '', '',
        '1ab03b92572b4949b7b6ffb71d7d3db7', 't1', '', -1, '2024-11-27 09:15:14', 101),
       (241127092135003978, 0, '2024-11-27 09:21:47', 0, '2024-11-27 09:21:47',
        '4bbd8139b7ec4684964719b938b85382/a698dde11f9ba7d9e705ed0872d26bbfb187055f0582322462cffecb5f3c5b5ed27eac3246245247bbcb0ef9b1d96eedc75bf6094a771189deffc2d729f92db0',
        '', 't1', '', '', '', '1e6317dfa79840e5be51215b3dda744a', 't1', '', -1,
        '2024-11-27 09:21:35', 101),
       (241127092508003996, 0, '2024-11-27 09:25:39', 0, '2024-11-27 09:25:39', '', '', 'ttt', '',
        '', '', 'd2a81b581e4c48a5becb752b2d78aa8b', 'tttt', '', -1, '2024-11-27 09:25:08', 101),
       (241127092547004002, 0, '2024-11-27 09:26:02', 0, '2024-11-27 09:26:02', '', '', 't32t2', '',
        '', '', '8a4f10e6d6cc4925b2666a63c3d875ba', 't32t2', '', -1, '2024-11-27 09:25:47', 101);
/*!40000 ALTER TABLE `base_user_delete_log`
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
/*!40000 ALTER TABLE `base_user`
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
VALUES (240830160154004401, 0, '2024-08-30 16:01:55', 0, '2024-11-27 16:05:45', 1, '', '管理员',
        'admin', 0),
       (240830160647004402, 0, '2024-08-30 16:06:48', 240902154357005281, '2024-11-25 09:24:15', 1,
        '', '默认角色', 'default', 1);
/*!40000 ALTER TABLE `base_role`
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

LOCK TABLES `base_file_transfer` WRITE;
/*!40000 ALTER TABLE `base_file_transfer`
    DISABLE KEYS */;
INSERT INTO `base_file_transfer`
VALUES (250115170959009107, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:01', 1, '', 0,
        250115170959009116, 101, '676991086a3e4bb18abc0d598139c1d7.png', '可爱卡通头像.png',
        1203565, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/676991086a3e4bb18abc0d598139c1d7.png'),
       (250115170959009108, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:00', 1, '', 0,
        250115170959009114, 101, '3f3ecb1b1d5f443698befae621f2f028.jpg', 'user.jpg', 11768, 601, '',
        0, 0, 'be-private-bucket', 1, 201, 'file-system/0/3f3ecb1b1d5f443698befae621f2f028.jpg'),
       (250115170959009109, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:01', 1, '', 0,
        250115170959009112, 101, '7c12995cd1b44217a1e40ea7e4920f34.png',
        'user.08803b0d_compressed.png', 2499, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/7c12995cd1b44217a1e40ea7e4920f34.png'),
       (250115170959009110, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:01', 1, '', 0,
        250115170959009115, 101, '4e75b4e7bf8d41b1ab23b0c31bc36540.png', '可爱卡通头像 (1).png',
        1207589, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/4e75b4e7bf8d41b1ab23b0c31bc36540.png'),
       (250115170959009111, 0, '2025-01-15 17:10:00', 0, '2025-01-15 17:10:01', 1, '', 0,
        250115170959009113, 101, '855ea4610b1a4f8392522db1f7e7b252.png', '可爱卡通头像 (2).png',
        873098, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/855ea4610b1a4f8392522db1f7e7b252.png'),
       (250115171011009134, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        250115171011009143, 101, 'e06d0abc29ff477c89e622037aef1e95.png', '可爱卡通头像 (1).png',
        1207589, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/e06d0abc29ff477c89e622037aef1e95.png'),
       (250115171011009137, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        250115171011009145, 101, '1f7b4496d4f44b2680f13453e499511a.jpg', 'user.jpg', 11768, 601, '',
        0, 0, 'be-private-bucket', 1, 201, 'file-system/0/1f7b4496d4f44b2680f13453e499511a.jpg'),
       (250115171011009138, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        250115171011009144, 101, '239bd837d2bb4873a52c77c32862df37.png',
        'user.08803b0d_compressed.png', 2499, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/239bd837d2bb4873a52c77c32862df37.png'),
       (250115171011009141, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        250115171011009146, 101, 'fc0a02b8ef524369839b9b9e27ba937e.png', '可爱卡通头像.png',
        1203565, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/fc0a02b8ef524369839b9b9e27ba937e.png'),
       (250115171011009142, 0, '2025-01-15 17:10:12', 0, '2025-01-15 17:10:12', 1, '', 0,
        250115171011009147, 101, '1529af61adff40c1929ad1f6929cc653.png', '可爱卡通头像 (2).png',
        873098, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/1529af61adff40c1929ad1f6929cc653.png'),
       (250115171026009166, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:27', 1, '', 0,
        250115171026009175, 101, 'c0b7e18970c349f6a475b93096ad2a7b.jpg', 'user.jpg', 11768, 601, '',
        0, 0, 'be-private-bucket', 1, 201, 'file-system/0/c0b7e18970c349f6a475b93096ad2a7b.jpg'),
       (250115171026009169, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:27', 1, '', 0,
        250115171026009177, 101, 'd71f9f1e2d034da594cf2a6e7ce929e8.png', '可爱卡通头像 (2).png',
        873098, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/d71f9f1e2d034da594cf2a6e7ce929e8.png'),
       (250115171026009170, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:27', 1, '', 0,
        250115171026009176, 101, '360929823410486c9771ff76332df675.png', '可爱卡通头像 (1).png',
        1207589, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/360929823410486c9771ff76332df675.png'),
       (250115171026009173, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:27', 1, '', 0,
        250115171026009178, 101, '69eb9c01f80141b1a833e63fbe3a3ac0.png', '可爱卡通头像.png',
        1203565, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/69eb9c01f80141b1a833e63fbe3a3ac0.png'),
       (250115171026009174, 0, '2025-01-15 17:10:26', 0, '2025-01-15 17:10:27', 1, '', 0,
        250115171026009179, 101, 'f17ff4c897bf4371a0c967c3a98592e9.png',
        'user.08803b0d_compressed.png', 2499, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/f17ff4c897bf4371a0c967c3a98592e9.png'),
       (250115171036009201, 0, '2025-01-15 17:10:36', 0, '2025-01-15 17:10:37', 1, '', 0,
        250115171036009210, 101, 'ca2dc3d390854b6b864fa3252f30f875.png',
        'user.08803b0d_compressed.png', 2499, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/ca2dc3d390854b6b864fa3252f30f875.png'),
       (250115171036009202, 0, '2025-01-15 17:10:36', 0, '2025-01-15 17:10:37', 1, '', 0,
        250115171036009206, 101, '55ce08419ba74c10b099e3001a43d5a8.jpg', 'user.jpg', 11768, 601, '',
        0, 0, 'be-private-bucket', 1, 201, 'file-system/0/55ce08419ba74c10b099e3001a43d5a8.jpg'),
       (250115171036009203, 0, '2025-01-15 17:10:36', 0, '2025-01-15 17:10:37', 1, '', 0,
        250115171036009209, 101, '6b7c2de8171c4fc6ae39c7e786aa46b5.png', '可爱卡通头像 (1).png',
        1207589, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/6b7c2de8171c4fc6ae39c7e786aa46b5.png'),
       (250115171036009204, 0, '2025-01-15 17:10:36', 0, '2025-01-15 17:10:37', 1, '', 0,
        250115171036009208, 101, '668dd6e3495c4244b4dc638b61cf8331.png', '可爱卡通头像 (2).png',
        873098, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/668dd6e3495c4244b4dc638b61cf8331.png'),
       (250115171036009205, 0, '2025-01-15 17:10:36', 0, '2025-01-15 17:10:37', 1, '', 0,
        250115171036009207, 101, '6c44190982a442fea5dc1523cc3cbdb7.png', '可爱卡通头像.png',
        1203565, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/6c44190982a442fea5dc1523cc3cbdb7.png'),
       (250115171143009236, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        250115171143009243, 101, '6da5bbf5ee05445388ad5659d471362c.jpg', 'user.jpg', 11768, 601, '',
        0, 0, 'be-private-bucket', 1, 201, 'file-system/0/6da5bbf5ee05445388ad5659d471362c.jpg'),
       (250115171143009237, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        250115171143009245, 101, 'd620269654dc44c79ee0ef7d4ca354e1.png', '可爱卡通头像.png',
        1203565, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/d620269654dc44c79ee0ef7d4ca354e1.png'),
       (250115171143009238, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        250115171143009244, 101, 'b388d09791ef4834a61c70089580eebf.png',
        'user.08803b0d_compressed.png', 2499, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/b388d09791ef4834a61c70089580eebf.png'),
       (250115171143009241, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        250115171143009247, 101, '18ec806417084c0ba15d1e908b10b1b3.png', '可爱卡通头像 (2).png',
        873098, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/18ec806417084c0ba15d1e908b10b1b3.png'),
       (250115171143009242, 0, '2025-01-15 17:11:44', 0, '2025-01-15 17:11:44', 1, '', 0,
        250115171143009246, 101, '6f40c136088d4f689b1e4739a2905576.png', '可爱卡通头像 (1).png',
        1207589, 601, '', 0, 0, 'be-private-bucket', 1, 201,
        'file-system/0/6f40c136088d4f689b1e4739a2905576.png');
/*!40000 ALTER TABLE `base_file_transfer`
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

LOCK TABLES `base_live_room_data` WRITE;
/*!40000 ALTER TABLE `base_live_room_data`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_live_room_data`
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

LOCK TABLES `base_socket` WRITE;
/*!40000 ALTER TABLE `base_socket`
    DISABLE KEYS */;
INSERT INTO `base_socket`
VALUES (250811155447118881, -1, '2025-08-11 15:54:47', -1, '2025-08-11 15:54:47', 1, '', 'ws://',
        'localhost', 8002, '/ws', 201, '00-e0-4c-68-08-fc'),
       (250812083924125091, -1, '2025-08-12 08:39:25', -1, '2025-08-12 08:39:25', 1, '', 'ws://',
        '119.45.9.90', 80, '/ws', 201, '02-42-ac-15-50-01');
/*!40000 ALTER TABLE `base_socket`
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
INSERT INTO `base_dept_ref_user`
VALUES (240912141649006163, 240902163618005502);
/*!40000 ALTER TABLE `base_dept_ref_user`
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

LOCK TABLES `base_im_session` WRITE;
/*!40000 ALTER TABLE `base_im_session`
    DISABLE KEYS */;
INSERT INTO `base_im_session`
VALUES (250427140812019548, -1, 201, -1),
       (250427143204019770, -1, 201, -1),
       (250428105947020916, 250428105627020894, 101, -1);
/*!40000 ALTER TABLE `base_im_session`
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
-- Table structure for table `base_user_0`
--

DROP TABLE IF EXISTS `base_user_0`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_0`
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
-- Dumping data for table `base_user_0`
--

LOCK TABLES `base_user_0` WRITE;
/*!40000 ALTER TABLE `base_user_0`
    DISABLE KEYS */;
INSERT INTO `base_user_0`
VALUES (0, 0, '2024-09-12 09:59:59', 0, '2024-09-12 09:59:59', 1, '',
        '496845386b67489b96f72fc9e6554d45/d66790cd547e44d664a5e8b83e58ce0eef140ef49d82f5e81c6ae416aa0cd3881fedde41dc89ef468164b8021f1334bdd5d03dbd41d92b606166b51bc0ec5d4a',
        '', 'admin', '', '', '', ''),
       (240902163618005502, 240902154357005281, '2024-09-02 16:36:18', 0, '2024-09-24 15:32:32', 1,
        '',
        '16b275b3a5b549838c6399c0e9159995/5693d65214a99b53a947a4dd7af5b17a48d01cdee0207c949ea0e37acc0505dc3ce87673e1320b975cda4bc71ad29df26aed05a6dfb7063adbcbd86deed59c1b',
        '', 'kar2', '', '', '', '');
/*!40000 ALTER TABLE `base_user_0`
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

DROP TABLE IF EXISTS `base_request`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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

DROP TABLE IF EXISTS `base_request_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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

-- Dump completed on 2025-08-12  8:45:19
