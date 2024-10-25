-- --------------------------------------------------------
-- 主机:                           81.69.58.190
-- 服务器版本:                        8.4.2 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


-- 导出 be_base_20240901 的数据库结构
CREATE DATABASE IF NOT EXISTS `be_base_20240901` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION = 'N' */;
USE `be_base_20240901`;

-- 导出  表 be_base_20240901.base_area 结构
CREATE TABLE IF NOT EXISTS `base_area`
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

-- 正在导出表  be_base_20240901.base_area 的数据：~11 rows (大约)
INSERT INTO `base_area` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `pid`, `order_no`, `uuid`)
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

-- 导出  表 be_base_20240901.base_area_ref_user 结构
CREATE TABLE IF NOT EXISTS `base_area_ref_user`
(
    `area_id` bigint NOT NULL COMMENT '区域主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`area_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：区域表，用户表';

-- 正在导出表  be_base_20240901.base_area_ref_user 的数据：~3 rows (大约)
INSERT INTO `base_area_ref_user` (`area_id`, `user_id`)
VALUES (240912111901006052, 240902154357005281),
       (240912111901006052, 240902163618005502),
       (240912111901006052, 240912095958005834);

-- 导出  表 be_base_20240901.base_auth 结构
CREATE TABLE IF NOT EXISTS `base_auth`
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

-- 正在导出表  be_base_20240901.base_auth 的数据：~45 rows (大约)
INSERT INTO `base_auth` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `auth`)
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
        '基础:岗位:下拉列表', 'basePost:dictList');

-- 导出  表 be_base_20240901.base_dept 结构
CREATE TABLE IF NOT EXISTS `base_dept`
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

-- 正在导出表  be_base_20240901.base_dept 的数据：~15 rows (大约)
INSERT INTO `base_dept` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `pid`, `order_no`, `uuid`)
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

-- 导出  表 be_base_20240901.base_dept_ref_user 结构
CREATE TABLE IF NOT EXISTS `base_dept_ref_user`
(
    `dept_id` bigint NOT NULL COMMENT '部门主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`dept_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：部门表，用户表';

-- 正在导出表  be_base_20240901.base_dept_ref_user 的数据：~3 rows (大约)
INSERT INTO `base_dept_ref_user` (`dept_id`, `user_id`)
VALUES (240912141649006163, 240902154357005281),
       (240912141649006163, 240902163618005502),
       (240912141649006163, 240912095958005834);

-- 导出  表 be_base_20240901.base_dict 结构
CREATE TABLE IF NOT EXISTS `base_dict`
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

-- 正在导出表  be_base_20240901.base_dict 的数据：~4 rows (大约)
INSERT INTO `base_dict` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `dict_key`, `name`, `type`, `value`, `order_no`,
                         `uuid`)
VALUES (240924152139000555, 0, '2024-09-24 15:21:39', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictName', 1, -1, 10000, '569fb80fe8724348badf393947dfbd18'),
       (240924152155000556, 0, '2024-09-24 15:21:56', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem1', 2, 1, 10000, 'a27e31bf77f249a5af6adf93b77b48e7'),
       (240924152320000557, 0, '2024-09-24 15:23:20', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem2', 2, 2, 9900, '316d838f1145454ebc4518ce0b3c503a'),
       (240924152400000558, 0, '2024-09-24 15:24:01', 0, '2024-09-24 15:26:51', 1, '', 'dictKey',
        'dictItem3', 2, 3, 9800, '9686ef6b5e344c0596147468e816a819');

-- 导出  表 be_base_20240901.base_email_configuration 结构
CREATE TABLE IF NOT EXISTS `base_email_configuration`
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

-- 正在导出表  be_base_20240901.base_email_configuration 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_file 结构
CREATE TABLE IF NOT EXISTS `base_file`
(
    `id`               bigint                                                        NOT NULL,
    `create_id`        bigint                                                        NOT NULL,
    `create_time`      datetime                                                      NOT NULL,
    `update_id`        bigint                                                        NOT NULL,
    `update_time`      datetime                                                      NOT NULL,
    `enable_flag`      tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `belong_id`        bigint                                                        NOT NULL COMMENT '归属者用户主键 id（拥有全部权限）',
    `bucket_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '桶名，例如：be-bucket',
    `uri`              varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx',
    `origin_file_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件原始名（包含文件类型）',
    `new_file_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '新的文件名（包含文件类型），例如：uuid.xxx',
    `file_ext_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '文件类型（不含点），备注：这个是读取文件流的头部信息获得文件类型',
    `extra_json`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '额外信息（json格式）',
    `upload_type`      int                                                           NOT NULL COMMENT '文件上传类型：101 头像 201 文件系统-文件',
    `storage_type`     int                                                           NOT NULL COMMENT '存放文件的服务器类型：101 阿里云oss 201 minio ',
    `pid`              bigint                                                        NOT NULL COMMENT '上级文件夹的文件主键 id，默认为 0',
    `type`             int                                                           NOT NULL COMMENT '类型：1 文件夹 2 文件',
    `show_file_name`   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '展示用的文件名，默认为：原始文件名（包含文件类型）',
    `public_flag`      tinyint(1)                                                    NOT NULL COMMENT '是否公开访问：0 否 1 是',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：文件表';

-- 正在导出表  be_base_20240901.base_file 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_file_auth 结构
CREATE TABLE IF NOT EXISTS `base_file_auth`
(
    `id`          bigint                                                        NOT NULL,
    `tenant_id`   bigint                                                        NOT NULL COMMENT '租户 id',
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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：子表：文件操作权限表，主表：文件表';

-- 正在导出表  be_base_20240901.base_file_auth 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_file_storage_configuration 结构
CREATE TABLE IF NOT EXISTS `base_file_storage_configuration`
(
    `id`                       bigint                                                        NOT NULL,
    `tenant_id`                bigint                                                        NOT NULL COMMENT '租户 id',
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

-- 正在导出表  be_base_20240901.base_file_storage_configuration 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_menu 结构
CREATE TABLE IF NOT EXISTS `base_menu`
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

-- 正在导出表  be_base_20240901.base_menu 的数据：~10 rows (大约)
INSERT INTO `base_menu` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `path`, `icon`, `pid`, `show_flag`,
                         `link_flag`, `router`, `redirect`, `order_no`, `uuid`)
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
        '/base/area', 'ri:compass-3-fill', 1, 1, 0, '', '', 9600, 'BaseArea'),
       (240912093214005832, 0, '2024-09-12 09:32:14', 0, '2024-09-12 09:43:35', 1, '', '部门管理',
        '/base/dept', 'ri:cup-fill', 1, 1, 0, '', '', 9500, 'BaseDept'),
       (240912094324005833, 0, '2024-09-12 09:43:25', 0, '2024-09-12 09:43:38', 1, '', '岗位管理',
        '/base/post', 'ep:briefcase', 1, 1, 0, '', '', 9400, 'BasePost'),
       (240924112136000441, 0, '2024-09-24 11:21:37', 0, '2024-09-24 11:25:31', 1, '', '参数管理',
        '/base/param', 'ri:album-fill', 1, 1, 0, '', '', 9300, 'BaseParam'),
       (240924112334000442, 0, '2024-09-24 11:23:34', 0, '2024-09-24 11:26:08', 1, '', '字典管理',
        '/base/dict', 'ri:book-2-fill', 1, 1, 0, '', '', 9200, 'BaseDict');

-- 导出  表 be_base_20240901.base_param 结构
CREATE TABLE IF NOT EXISTS `base_param`
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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：主表：参数表';

-- 正在导出表  be_base_20240901.base_param 的数据：~1 rows (大约)
INSERT INTO `base_param` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                          `enable_flag`, `remark`, `name`, `value`, `uuid`)
VALUES (240924144747000551, 0, '2024-09-24 14:47:47', 0, '2024-09-24 14:47:47', 1, '',
        '默认允许后台登录', 'true', '3');

-- 导出  表 be_base_20240901.base_post 结构
CREATE TABLE IF NOT EXISTS `base_post`
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

-- 正在导出表  be_base_20240901.base_post 的数据：~15 rows (大约)
INSERT INTO `base_post` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `pid`, `order_no`, `uuid`)
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

-- 导出  表 be_base_20240901.base_post_ref_user 结构
CREATE TABLE IF NOT EXISTS `base_post_ref_user`
(
    `post_id` bigint NOT NULL COMMENT '岗位主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`post_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20230301：关联表：岗位表，用户表';

-- 正在导出表  be_base_20240901.base_post_ref_user 的数据：~3 rows (大约)
INSERT INTO `base_post_ref_user` (`post_id`, `user_id`)
VALUES (240912142923006178, 240902154357005281),
       (240912142923006178, 240902163618005502),
       (240912142923006178, 240912095958005834);

-- 导出  表 be_base_20240901.base_request 结构
CREATE TABLE IF NOT EXISTS `base_request`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `update_id`    bigint                                                        NOT NULL,
    `update_time`  datetime                                                      NOT NULL,
    `enable_flag`  tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '备注',
    `uri`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求的uri',
    `name`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口名（备用）',
    `cost_ms`      bigint                                                        NOT NULL COMMENT '耗时（毫秒）',
    `category`     int                                                           NOT NULL COMMENT '请求类别',
    `ip`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip',
    `region`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Ip2RegionUtil.getRegion() 获取到的 ip所处区域',
    `success_flag` tinyint(1)                                                    NOT NULL COMMENT '请求是否成功',
    `type`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '请求类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240901：主表：请求表';

-- 正在导出表  be_base_20240901.base_request 的数据：~31 rows (大约)
INSERT INTO `base_request` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                            `enable_flag`, `remark`, `uri`, `name`, `cost_ms`, `category`, `ip`,
                            `region`, `success_flag`, `type`)
VALUES (241024185212005612, 0, '2024-10-24 18:52:13', 0, '2024-10-24 18:52:13', 1, '',
        '/base/user/manageSignInFlag', '是否允许后台登录', 165, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185213005613, 0, '2024-10-24 18:52:13', 0, '2024-10-24 18:52:13', 1, '',
        '/base/menu/userSelfMenuList', '获取：当前用户绑定的菜单', 224, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185214005614, 0, '2024-10-24 18:52:15', 0, '2024-10-24 18:52:15', 1, '',
        '/base/role/dictList', '下拉列表', 224, 101, '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185214005615, 0, '2024-10-24 18:52:15', 0, '2024-10-24 18:52:15', 1, '',
        '/base/menu/tree', '查询：树结构', 206, 101, '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185235005616, 240902154357005281, '2024-10-24 18:52:36', 240902154357005281,
        '2024-10-24 18:52:36', 1, '', '/sign/userName/signIn/password', '账号密码登录', 585, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, 'SIGN_IN'),
       (241024185236005617, 240902154357005281, '2024-10-24 18:52:36', 240902154357005281,
        '2024-10-24 18:52:36', 1, '', '/base/user/manageSignInFlag', '是否允许后台登录', 127, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185236005618, 240902154357005281, '2024-10-24 18:52:37', 240902154357005281,
        '2024-10-24 18:52:37', 1, '', '/base/menu/userSelfMenuList', '获取：当前用户绑定的菜单', 223,
        101, '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185239005619, 240902154357005281, '2024-10-24 18:52:39', 240902154357005281,
        '2024-10-24 18:52:39', 1, '', '/base/menu/tree', '查询：树结构', 119, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185239005620, 240902154357005281, '2024-10-24 18:52:40', 240902154357005281,
        '2024-10-24 18:52:40', 1, '', '/base/role/dictList', '下拉列表', 80, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185240005621, 240902154357005281, '2024-10-24 18:52:41', 240902154357005281,
        '2024-10-24 18:52:41', 1, '', '/base/menu/infoById', '通过主键id，查看详情', 214, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185245005622, 240902163618005502, '2024-10-24 18:52:46', 240902163618005502,
        '2024-10-24 18:52:46', 1, '', '/sign/userName/signIn/password', '账号密码登录', 247, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, 'SIGN_IN'),
       (241024185245005623, 240902163618005502, '2024-10-24 18:52:46', 240902163618005502,
        '2024-10-24 18:52:46', 1, '', '/base/user/manageSignInFlag', '是否允许后台登录', 41, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185246005624, 240902163618005502, '2024-10-24 18:52:46', 240902163618005502,
        '2024-10-24 18:52:46', 1, '', '/base/menu/userSelfMenuList', '获取：当前用户绑定的菜单', 118,
        101, '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185248005625, 240902163618005502, '2024-10-24 18:52:49', 240902163618005502,
        '2024-10-24 18:52:49', 1, '', '/base/menu/tree', '查询：树结构', 120, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185248005626, 240902163618005502, '2024-10-24 18:52:49', 240902163618005502,
        '2024-10-24 18:52:49', 1, '', '/base/role/dictList', '下拉列表', 111, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185250005627, 240902163618005502, '2024-10-24 18:52:50', 240902163618005502,
        '2024-10-24 18:52:50', 1, '', '/base/menu/infoById', '通过主键id，查看详情', 158, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185253005628, 240902163618005502, '2024-10-24 18:52:53', 240902163618005502,
        '2024-10-24 18:52:53', 1, '', '/base/menu/dictTreeList', '下拉树形列表', 125, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185253005629, 240902163618005502, '2024-10-24 18:52:53', 240902163618005502,
        '2024-10-24 18:52:53', 1, '', '/base/user/dictList', '下拉列表', 210, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185253005630, 240902163618005502, '2024-10-24 18:52:53', 240902163618005502,
        '2024-10-24 18:52:53', 1, '', '/base/auth/dictList', '下拉列表', 131, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185253005631, 240902163618005502, '2024-10-24 18:52:53', 240902163618005502,
        '2024-10-24 18:52:53', 1, '', '/base/role/page', '分页排序查询', 316, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185255005632, 240902163618005502, '2024-10-24 18:52:56', 240902163618005502,
        '2024-10-24 18:52:56', 1, '', '/base/role/infoById', '通过主键id，查看详情', 376, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185303005633, 240902163618005502, '2024-10-24 18:53:03', 240902163618005502,
        '2024-10-24 18:53:03', 1, '', '/base/role/infoById', '通过主键id，查看详情', 349, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185333005634, 240902163618005502, '2024-10-24 18:53:34', 240902163618005502,
        '2024-10-24 18:53:34', 1, '', '/base/role/dictList', '下拉列表', 105, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185333005635, 240902163618005502, '2024-10-24 18:53:34', 240902163618005502,
        '2024-10-24 18:53:34', 1, '', '/base/user/page', '分页排序查询', 896, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185335005636, 240902163618005502, '2024-10-24 18:53:36', 240902163618005502,
        '2024-10-24 18:53:36', 1, '', '/base/user/infoById', '', 0, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 0, ''),
       (241024185637005722, 240902163618005502, '2024-10-24 18:56:38', 240902163618005502,
        '2024-10-24 18:56:38', 1, '', '/base/user/manageSignInFlag', '是否允许后台登录', 152, 101,
        '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185638005723, 240902163618005502, '2024-10-24 18:56:38', 240902163618005502,
        '2024-10-24 18:56:38', 1, '', '/base/menu/userSelfMenuList', '获取：当前用户绑定的菜单', 291,
        101, '127.0.0.1', '0|0|0|内网IP|内网IP', 1, ''),
       (241024185639005724, 240902163618005502, '2024-10-24 18:56:40', 240902163618005502,
        '2024-10-24 18:56:40', 1, '', '/base/role/dictList', '下拉列表', 169, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185639005725, 240902163618005502, '2024-10-24 18:56:40', 240902163618005502,
        '2024-10-24 18:56:40', 1, '', '/base/user/page', '分页排序查询', 899, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 1, ''),
       (241024185642005726, 240902163618005502, '2024-10-24 18:56:43', 240902163618005502,
        '2024-10-24 18:56:43', 1, '', '/base/user/infoById', '', 0, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 0, ''),
       (241024185656005727, 240902163618005502, '2024-10-24 18:56:57', 240902163618005502,
        '2024-10-24 18:56:57', 1, '', '/base/user/infoById', '', 0, 101, '127.0.0.1',
        '0|0|0|内网IP|内网IP', 0, '');

-- 导出  表 be_base_20240901.base_request_info 结构
CREATE TABLE IF NOT EXISTS `base_request_info`
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

-- 正在导出表  be_base_20240901.base_request_info 的数据：~31 rows (大约)
INSERT INTO `base_request_info` (`id`, `uri`, `cost_ms`, `success_flag`, `error_msg`,
                                 `request_param`, `response_value`)
VALUES (241024185212005612, '/base/user/manageSignInFlag', 165, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":true,"service":"be-base-web-20240901"}'),
       (241024185213005613, '/base/menu/userSelfMenuList', 224, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"id":1},{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442}],"service":"be-base-web-20240901"}'),
       (241024185214005614, '/base/role/dictList', 224, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240830160154004401,"name":"管理员"},{"id":240830160647004402,"name":"默认角色"}],"total":2,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185214005615, '/base/menu/tree', 206, 1, '',
        '{"BaseMenuPageDTO":"{\\"current\\":1,\\"pageSize\\":10}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"children":[{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2,"enableFlag":true},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532,"enableFlag":true},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641,"enableFlag":true},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971,"enableFlag":true},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831,"enableFlag":true},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832,"enableFlag":true},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833,"enableFlag":true},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441,"enableFlag":true},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442,"enableFlag":true}],"id":1,"enableFlag":true}],"service":"be-base-web-20240901"}'),
       (241024185235005616, '/sign/userName/signIn/password', 585, 1, '',
        '{"SignUserNameSignInPasswordDTO":"{\\"password\\":\\"CVXZHunOScuCut1Ho7xHtwBE149ba2t4f9geQLp/r2K63RbquBn8/psi9oM6/uev3BKpB2ReGjrV0Zg7PxkcFkfii/97XWKTiFhi3GnxLR7m0wUbxoYVzastpaI4nihqFg3ilbvciAnl++efZQBcb4XrZ814Vw0jS9121b7qTo0=\\",\\"username\\":\\"kar1\\"}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"jwt":"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3Mjk4NTM1NTYsInVzZXJJZCI6MjQwOTAyMTU0MzU3MDA1MjgxLCJ3eEFwcElkIjoiIiwid3hPcGVuSWQiOiIifQ.LIriOssWEO6VA2ZO_Bzn-OdtAWiW3robVhYtmjjaNr8","jwtExpireTs":1729852956304,"jwtRefreshToken":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjI0MDkwMjE1NDM1NzAwNTI4MSwid3hBcHBJZCI6IiIsInd4T3BlbklkIjoiIn0.ijKKD5vMJ7qftPBZlEtLS6FUxMzl43BNB4SjlSPVybM"},"service":"be-base-web-20240901"}'),
       (241024185236005617, '/base/user/manageSignInFlag', 127, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":true,"service":"be-base-web-20240901"}'),
       (241024185236005618, '/base/menu/userSelfMenuList', 223, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"id":1},{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442}],"service":"be-base-web-20240901"}'),
       (241024185239005619, '/base/menu/tree', 119, 1, '',
        '{"BaseMenuPageDTO":"{\\"current\\":1,\\"pageSize\\":10}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"children":[{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2,"enableFlag":true},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532,"enableFlag":true},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641,"enableFlag":true},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971,"enableFlag":true},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831,"enableFlag":true},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832,"enableFlag":true},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833,"enableFlag":true},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441,"enableFlag":true},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442,"enableFlag":true}],"id":1,"enableFlag":true}],"service":"be-base-web-20240901"}'),
       (241024185239005620, '/base/role/dictList', 80, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240830160154004401,"name":"管理员"},{"id":240830160647004402,"name":"默认角色"}],"total":2,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185240005621, '/base/menu/infoById', 214, 1, '', '{"NotNullId":"{\\"id\\":1}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"roleIdSet":[240830160647004402,240830160154004401],"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"linkFlag":false,"router":"","redirect":"","uuid":"Base","orderNo":10000,"id":1,"enableFlag":true,"remark":"","createId":0,"createTime":1723796295000,"updateId":240902154357005281,"updateTime":1725264477000},"service":"be-base-web-20240901"}'),
       (241024185245005622, '/sign/userName/signIn/password', 247, 1, '',
        '{"SignUserNameSignInPasswordDTO":"{\\"password\\":\\"m6urK9GldP+XfIyVYwLUhib6n4SW80qkBYb3uXL0bvIa5cMegkJvGT9IhNa2CZPkA9cLBpgOv9POVO8zS/72WUo15PvN8N1Z/TyJcDeVrWQIHzUjnB0uo8nWr1MniaOsqp1JlNSdZ3/s7wLePFbYy2QB4vl++nUL3YL8Aes6Kx0=\\",\\"username\\":\\"kar2\\"}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"jwt":"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3Mjk4NTM1NjUsInVzZXJJZCI6MjQwOTAyMTYzNjE4MDA1NTAyLCJ3eEFwcElkIjoiIiwid3hPcGVuSWQiOiIifQ.ZRLrIzVzIdS4uE7zOuEGuKg0wXvjczcgOmF4d7lDQz4","jwtExpireTs":1729852965801,"jwtRefreshToken":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjI0MDkwMjE2MzYxODAwNTUwMiwid3hBcHBJZCI6IiIsInd4T3BlbklkIjoiIn0.EQ5cOG4rw-DBzyGBHwORkDIsOoaUAIYCGs1iIs7z8-4"},"service":"be-base-web-20240901"}'),
       (241024185245005623, '/base/user/manageSignInFlag', 41, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":true,"service":"be-base-web-20240901"}'),
       (241024185246005624, '/base/menu/userSelfMenuList', 118, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"id":1},{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442}],"service":"be-base-web-20240901"}'),
       (241024185248005625, '/base/menu/tree', 120, 1, '',
        '{"BaseMenuPageDTO":"{\\"current\\":1,\\"pageSize\\":10}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"children":[{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2,"enableFlag":true},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532,"enableFlag":true},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641,"enableFlag":true},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971,"enableFlag":true},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831,"enableFlag":true},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832,"enableFlag":true},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833,"enableFlag":true},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441,"enableFlag":true},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442,"enableFlag":true}],"id":1,"enableFlag":true}],"service":"be-base-web-20240901"}'),
       (241024185248005626, '/base/role/dictList', 111, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240830160154004401,"name":"管理员"},{"id":240830160647004402,"name":"默认角色"}],"total":2,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185250005627, '/base/menu/infoById', 158, 1, '', '{"NotNullId":"{\\"id\\":1}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"roleIdSet":[240830160647004402,240830160154004401],"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"linkFlag":false,"router":"","redirect":"","uuid":"Base","orderNo":10000,"id":1,"enableFlag":true,"remark":"","createId":0,"createTime":1723796295000,"updateId":240902154357005281,"updateTime":1725264477000},"service":"be-base-web-20240901"}'),
       (241024185253005628, '/base/menu/dictTreeList', 125, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","pid":0,"children":[{"name":"菜单管理","pid":1,"id":2},{"name":"角色管理","pid":1,"id":240823111516002532},{"name":"权限管理","pid":1,"id":240823140711002641},{"name":"用户管理","pid":1,"id":240823162121002971},{"name":"区域管理","pid":1,"id":240912092930005831},{"name":"部门管理","pid":1,"id":240912093214005832},{"name":"岗位管理","pid":1,"id":240912094324005833},{"name":"参数管理","pid":1,"id":240924112136000441},{"name":"字典管理","pid":1,"id":240924112334000442}],"id":1}],"service":"be-base-web-20240901"}'),
       (241024185253005629, '/base/user/dictList', 210, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240912095958005834,"name":"t1"},{"id":240902163618005502,"name":"nickname_kar2"},{"id":240902154357005281,"name":"nickname_kar1"},{"id":0,"name":"admin"}],"total":4,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185253005630, '/base/auth/dictList', 131, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240902143347004951,"name":"基础:菜单:新增修改"},{"id":240902143348004952,"name":"基础:菜单:列表查询"},{"id":240902143348004953,"name":"基础:菜单:删除"},{"id":240902143348004954,"name":"基础:菜单:查看详情"},{"id":240902143348004955,"name":"基础:菜单:下拉列表"},{"id":240902143451004956,"name":"基础:角色:新增修改"},{"id":240902143451004957,"name":"基础:角色:列表查询"},{"id":240902143451004958,"name":"基础:角色:删除"},{"id":240902143451004959,"name":"基础:角色:查看详情"},{"id":240902143452004960,"name":"基础:角色:下拉列表"},{"id":240902143513004961,"name":"基础:权限:新增修改"},{"id":240902143513004962,"name":"基础:权限:列表查询"},{"id":240902143513004963,"name":"基础:权限:删除"},{"id":240902143513004964,"name":"基础:权限:查看详情"},{"id":240902143514004965,"name":"基础:权限:下拉列表"},{"id":240902143532004966,"name":"基础:用户:新增修改"},{"id":240902143532004967,"name":"基础:用户:列表查询"},{"id":240902143533004968,"name":"基础:用户:删除"},{"id":240902143533004969,"name":"基础:用户:查看详情"},{"id":240902143533004970,"name":"基础:用户:下拉列表"},{"id":241015142748002311,"name":"基础:参数:新增修改"},{"id":241015142749002312,"name":"基础:参数:列表查询"},{"id":241015142749002313,"name":"基础:参数:删除"},{"id":241015142750002314,"name":"基础:参数:查看详情"},{"id":241015142751002315,"name":"基础:参数:下拉列表"},{"id":241015142855002316,"name":"基础:字典:新增修改"},{"id":241015142855002317,"name":"基础:字典:列表查询"},{"id":241015142856002318,"name":"基础:字典:删除"},{"id":241015142857002319,"name":"基础:字典:查看详情"},{"id":241015142857002320,"name":"基础:字典:下拉列表"},{"id":241015143600002321,"name":"基础:区域:新增修改"},{"id":241015143600002322,"name":"基础:区域:列表查询"},{"id":241015143601002323,"name":"基础:区域:删除"},{"id":241015143602002324,"name":"基础:区域:查看详情"},{"id":241015143602002325,"name":"基础:区域:下拉列表"},{"id":241015143619002326,"name":"基础:部门:新增修改"},{"id":241015143619002327,"name":"基础:部门:列表查询"},{"id":241015143620002328,"name":"基础:部门:删除"},{"id":241015143621002329,"name":"基础:部门:查看详情"},{"id":241015143621002330,"name":"基础:部门:下拉列表"},{"id":241015143631002331,"name":"基础:岗位:新增修改"},{"id":241015143632002332,"name":"基础:岗位:列表查询"},{"id":241015143632002333,"name":"基础:岗位:删除"},{"id":241015143633002334,"name":"基础:岗位:查看详情"},{"id":241015143634002335,"name":"基础:岗位:下拉列表"}],"total":45,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185253005631, '/base/role/page', 316, 1, '',
        '{"BaseRolePageDTO":"{\\"current\\":1,\\"pageSize\\":15}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"name":"默认角色","uuid":"default","defaultFlag":true,"id":240830160647004402,"enableFlag":true,"remark":"","createId":0,"createTime":1725005208000,"updateId":0,"updateTime":1728974239000},{"name":"管理员","uuid":"admin","defaultFlag":false,"id":240830160154004401,"enableFlag":true,"remark":"","createId":0,"createTime":1725004915000,"updateId":0,"updateTime":1728974216000}],"total":2,"size":15,"current":1},"service":"be-base-web-20240901"}'),
       (241024185255005632, '/base/role/infoById', 376, 1, '',
        '{"NotNullId":"{\\"id\\":240830160154004401}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"userIdSet":[240902154357005281],"menuIdSet":[1,2,240912092930005831,240823111516002532,240823162121002971,240912093214005832,240924112334000442,240924112136000441,240912094324005833,240823140711002641],"authIdSet":[240902143514004965,241015142751002315,241015142856002318,241015143633002334,241015143620002328,241015142857002320,241015143602002324,241015143602002325,240902143532004966,241015142857002319,240902143532004967,240902143348004953,240902143348004952,240902143348004955,241015143619002327,241015143632002333,240902143348004954,241015143619002326,241015143632002332,240902143452004960,241015142748002311,241015143631002331,241015142749002313,241015142749002312,241015143601002323,240902143533004968,240902143347004951,240902143533004969,240902143533004970,241015143600002322,241015143600002321,240902143513004964,241015143634002335,240902143451004957,240902143513004963,240902143451004956,240902143513004962,240902143451004959,240902143513004961,241015142750002314,241015142855002316,240902143451004958,241015142855002317,241015143621002330,241015143621002329],"name":"管理员","uuid":"admin","defaultFlag":false,"id":240830160154004401,"enableFlag":true,"remark":"","createId":0,"createTime":1725004915000,"updateId":0,"updateTime":1728974216000},"service":"be-base-web-20240901"}'),
       (241024185303005633, '/base/role/infoById', 349, 1, '',
        '{"NotNullId":"{\\"id\\":240830160647004402}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"userIdSet":[],"menuIdSet":[1,2,240912092930005831,240823111516002532,240823162121002971,240912093214005832,240924112334000442,240924112136000441,240912094324005833,240823140711002641],"authIdSet":[240902143514004965,241015142751002315,241015143633002334,241015142857002320,241015143602002324,241015143602002325,241015142857002319,240902143532004967,240902143348004952,240902143348004955,241015143619002327,240902143348004954,241015143632002332,240902143452004960,241015142749002312,240902143533004970,241015143600002322,240902143513004964,241015143634002335,240902143451004957,240902143513004962,240902143451004959,241015142750002314,241015142855002317,241015143621002330,241015143621002329],"name":"默认角色","uuid":"default","defaultFlag":true,"id":240830160647004402,"enableFlag":true,"remark":"","createId":0,"createTime":1725005208000,"updateId":0,"updateTime":1728974239000},"service":"be-base-web-20240901"}'),
       (241024185333005634, '/base/role/dictList', 105, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240830160154004401,"name":"管理员"},{"id":240830160647004402,"name":"默认角色"}],"total":2,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185333005635, '/base/user/page', 896, 1, '',
        '{"BaseUserPageDTO":"{\\"current\\":1,\\"pageSize\\":15,\\"order\\":{\\"name\\":\\"lastActiveTime\\",\\"value\\":\\"descend\\"}}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240902163618005502,"nickname":"nickname_kar2","avatarFileId":-1,"email":"","username":"k***","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1725266178000,"lastActiveTime":1729767183000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true},{"id":240902154357005281,"nickname":"nickname_kar1","avatarFileId":-1,"email":"k***@qq.com","username":"k***","phone":"186****3333","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1725263037000,"lastActiveTime":1729767161000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","roleIdSet":[240830160154004401],"manageSignInFlag":true},{"id":0,"nickname":"admin","avatarFileId":-1,"email":"","username":"a****","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1726106399000,"lastActiveTime":1729767135000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true},{"id":240912095958005834,"nickname":"t1","avatarFileId":-1,"email":"","username":"t*","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1726106399000,"lastActiveTime":1726106399000,"ip":"0:0:0:0:0:0:0:1","region":"ipv6#0:0:0:0:0:0:0:1","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true}],"total":4,"size":15,"current":1},"service":"be-base-web-20240901"}'),
       (241024185335005636, '/base/user/infoById', 0, 0, '不允许访问', '',
        '{"code":100041,"msg":"权限不足","receive":true,"service":"be-base-web-20240901"}'),
       (241024185637005722, '/base/user/manageSignInFlag', 152, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":true,"service":"be-base-web-20240901"}'),
       (241024185638005723, '/base/menu/userSelfMenuList', 291, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":[{"name":"系统管理","path":"/base","icon":"ri:settings-3-line","showFlag":true,"router":"","redirect":"","uuid":"Base","orderNo":10000,"pid":0,"id":1},{"name":"菜单管理","path":"/base/menu","icon":"ep:menu","showFlag":true,"router":"","redirect":"","uuid":"BaseMenu","orderNo":10000,"pid":1,"id":2},{"name":"角色管理","path":"/base/role","icon":"ri:file-user-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseRole","orderNo":9900,"pid":1,"id":240823111516002532},{"name":"权限管理","path":"/base/auth","icon":"ri:key-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseAuth","orderNo":9800,"pid":1,"id":240823140711002641},{"name":"用户管理","path":"/base/user","icon":"ri:user-settings-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseUser","orderNo":9700,"pid":1,"id":240823162121002971},{"name":"区域管理","path":"/base/area","icon":"ri:compass-3-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseArea","orderNo":9600,"pid":1,"id":240912092930005831},{"name":"部门管理","path":"/base/dept","icon":"ri:cup-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDept","orderNo":9500,"pid":1,"id":240912093214005832},{"name":"岗位管理","path":"/base/post","icon":"ep:briefcase","showFlag":true,"router":"","redirect":"","uuid":"BasePost","orderNo":9400,"pid":1,"id":240912094324005833},{"name":"参数管理","path":"/base/param","icon":"ri:album-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseParam","orderNo":9300,"pid":1,"id":240924112136000441},{"name":"字典管理","path":"/base/dict","icon":"ri:book-2-fill","showFlag":true,"router":"","redirect":"","uuid":"BaseDict","orderNo":9200,"pid":1,"id":240924112334000442}],"service":"be-base-web-20240901"}'),
       (241024185639005724, '/base/role/dictList', 169, 1, '', '{}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240830160154004401,"name":"管理员"},{"id":240830160647004402,"name":"默认角色"}],"total":2,"size":10,"current":1},"service":"be-base-web-20240901"}'),
       (241024185639005725, '/base/user/page', 899, 1, '',
        '{"BaseUserPageDTO":"{\\"current\\":1,\\"pageSize\\":15,\\"order\\":{\\"name\\":\\"lastActiveTime\\",\\"value\\":\\"descend\\"}}"}',
        '{"code":200,"msg":"操作成功","receive":true,"data":{"records":[{"id":240902163618005502,"nickname":"nickname_kar2","avatarFileId":-1,"email":"","username":"k***","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1725266178000,"lastActiveTime":1729767216000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true},{"id":240902154357005281,"nickname":"nickname_kar1","avatarFileId":-1,"email":"k***@qq.com","username":"k***","phone":"186****3333","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1725263037000,"lastActiveTime":1729767161000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","roleIdSet":[240830160154004401],"manageSignInFlag":true},{"id":0,"nickname":"admin","avatarFileId":-1,"email":"","username":"a****","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1726106399000,"lastActiveTime":1729767135000,"ip":"127.0.0.1","region":"0|0|0|内网IP|内网IP","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true},{"id":240912095958005834,"nickname":"t1","avatarFileId":-1,"email":"","username":"t*","phone":"","wxAppId":"","wxOpenId":"","enableFlag":true,"passwordFlag":true,"createTime":1726106399000,"lastActiveTime":1726106399000,"ip":"0:0:0:0:0:0:0:1","region":"ipv6#0:0:0:0:0:0:0:1","signUpType":"PC_BROWSER_WINDOWS","manageSignInFlag":true}],"total":4,"size":15,"current":1},"service":"be-base-web-20240901"}'),
       (241024185642005726, '/base/user/infoById', 0, 0,
        'org.springframework.security.access.AccessDeniedException: 不允许访问\r\n	at org.springframework.security.access.vote.AffirmativeBased.decide(AffirmativeBased.java:73)\r\n	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.attemptAuthorization(AbstractSecurityInterceptor.java:239)\r\n	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.beforeInvocation(AbstractSecurityInterceptor.java:208)\r\n	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:58)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:707)\r\n	at com.kar20240901.be.base.web.controller.base.BaseUserController$$EnhancerBySpringCGLIB$$269d9808.infoById(<generated>)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:555)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(Framewor',
        '', '{"code":100041,"msg":"权限不足","receive":true,"service":"be-base-web-20240901"}'),
       (241024185656005727, '/base/user/infoById', 0, 0,
        'org.springframework.security.access.AccessDeniedException: 不允许访问\r\n	at org.springframework.security.access.vote.AffirmativeBased.decide(AffirmativeBased.java:73)\r\n	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.attemptAuthorization(AbstractSecurityInterceptor.java:239)\r\n	at org.springframework.security.access.intercept.AbstractSecurityInterceptor.beforeInvocation(AbstractSecurityInterceptor.java:208)\r\n	at org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor.invoke(MethodSecurityInterceptor.java:58)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)\r\n	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:762)\r\n	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:707)\r\n	at com.kar20240901.be.base.web.controller.base.BaseUserController$$EnhancerBySpringCGLIB$$269d9808.infoById(<generated>)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\r\n	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.base/java.lang.reflect.Method.invoke(Method.java:568)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:150)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:895)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1072)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:965)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)\r\n	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:555)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(Framewor',
        '', '{"code":100041,"msg":"权限不足","receive":true,"service":"be-base-web-20240901"}');

-- 导出  表 be_base_20240901.base_role 结构
CREATE TABLE IF NOT EXISTS `base_role`
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

-- 正在导出表  be_base_20240901.base_role 的数据：~2 rows (大约)
INSERT INTO `base_role` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                         `enable_flag`, `remark`, `name`, `uuid`, `default_flag`)
VALUES (240830160154004401, 0, '2024-08-30 16:01:55', 0, '2024-10-15 14:36:56', 1, '', '管理员',
        'admin', 0),
       (240830160647004402, 0, '2024-08-30 16:06:48', 0, '2024-10-15 14:37:19', 1, '', '默认角色',
        'default', 1);

-- 导出  表 be_base_20240901.base_role_ref_auth 结构
CREATE TABLE IF NOT EXISTS `base_role_ref_auth`
(
    `role_id` bigint NOT NULL COMMENT '角色主键id',
    `auth_id` bigint NOT NULL COMMENT '权限主键id',
    PRIMARY KEY (`auth_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，权限表';

-- 正在导出表  be_base_20240901.base_role_ref_auth 的数据：~71 rows (大约)
INSERT INTO `base_role_ref_auth` (`role_id`, `auth_id`)
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
       (240830160647004402, 241015143634002335);

-- 导出  表 be_base_20240901.base_role_ref_menu 结构
CREATE TABLE IF NOT EXISTS `base_role_ref_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色主键 id',
    `menu_id` bigint NOT NULL COMMENT '菜单主键 id',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，菜单表';

-- 正在导出表  be_base_20240901.base_role_ref_menu 的数据：~20 rows (大约)
INSERT INTO `base_role_ref_menu` (`role_id`, `menu_id`)
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
       (240830160647004402, 1),
       (240830160647004402, 2),
       (240830160647004402, 240823111516002532),
       (240830160647004402, 240823140711002641),
       (240830160647004402, 240823162121002971),
       (240830160647004402, 240912092930005831),
       (240830160647004402, 240912093214005832),
       (240830160647004402, 240912094324005833),
       (240830160647004402, 240924112136000441),
       (240830160647004402, 240924112334000442);

-- 导出  表 be_base_20240901.base_role_ref_user 结构
CREATE TABLE IF NOT EXISTS `base_role_ref_user`
(
    `role_id` bigint NOT NULL COMMENT '角色主键id',
    `user_id` bigint NOT NULL COMMENT '用户主键id',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240901：关联表：角色表，用户表';

-- 正在导出表  be_base_20240901.base_role_ref_user 的数据：~1 rows (大约)
INSERT INTO `base_role_ref_user` (`role_id`, `user_id`)
VALUES (240830160154004401, 240902154357005281);

-- 导出  表 be_base_20240901.base_sms_configuration 结构
CREATE TABLE IF NOT EXISTS `base_sms_configuration`
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

-- 正在导出表  be_base_20240901.base_sms_configuration 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_socket 结构
CREATE TABLE IF NOT EXISTS `base_socket`
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

-- 正在导出表  be_base_20240901.base_socket 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_socket_ref_user 结构
CREATE TABLE IF NOT EXISTS `base_socket_ref_user`
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
    `mac_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '冗余字段：mac地址，用于：和 port一起判断是否是重复启动，如果是，则需要移除之前的 socket信息',
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

-- 正在导出表  be_base_20240901.base_socket_ref_user 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_user 结构
CREATE TABLE IF NOT EXISTS `base_user`
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

-- 正在导出表  be_base_20240901.base_user 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_user_0 结构
CREATE TABLE IF NOT EXISTS `base_user_0`
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

-- 正在导出表  be_base_20240901.base_user_0 的数据：~3 rows (大约)
INSERT INTO `base_user_0` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                           `enable_flag`, `remark`, `password`, `email`, `username`, `phone`,
                           `wx_app_id`, `wx_open_id`, `wx_union_id`)
VALUES (0, 0, '2024-09-12 09:59:59', 0, '2024-09-12 09:59:59', 1, '',
        '51791182cd33494e8c8172469639b6d1/2ec129bd2d5914753d2dcee2fb2fc781bb65ef907e831435d2fec9f0afd7481971be9840167bb839b834c0d8ba536420515d39fcaf4144ba9a86c70c783f7c69',
        '', 'admin', '', '', '', ''),
       (240902163618005502, 240902154357005281, '2024-09-02 16:36:18', 0, '2024-09-24 15:32:32', 1,
        '',
        '16b275b3a5b549838c6399c0e9159995/5693d65214a99b53a947a4dd7af5b17a48d01cdee0207c949ea0e37acc0505dc3ce87673e1320b975cda4bc71ad29df26aed05a6dfb7063adbcbd86deed59c1b',
        '', 'kar2', '', '', '', ''),
       (240912095958005834, 0, '2024-09-12 09:59:59', 0, '2024-09-12 10:17:29', 1, '',
        '7a01af687fbc43e7a45af75ed8e765af/9685a29520240b340ed1ddc830ea879cb529813a065bfeaeefbac8be470bd41c2c33947371380274faf5bdeef1a5c68a8ab41da5502b1674104b740b88ea8e1a',
        '', 't1', '', '', '', '');

-- 导出  表 be_base_20240901.base_user_1 结构
CREATE TABLE IF NOT EXISTS `base_user_1`
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

-- 正在导出表  be_base_20240901.base_user_1 的数据：~1 rows (大约)
INSERT INTO `base_user_1` (`id`, `create_id`, `create_time`, `update_id`, `update_time`,
                           `enable_flag`, `remark`, `password`, `email`, `username`, `phone`,
                           `wx_app_id`, `wx_open_id`, `wx_union_id`)
VALUES (240902154357005281, 0, '2024-09-02 15:43:57', 240902154357005281, '2024-09-12 09:59:12', 1,
        '',
        'fc3727dc1a1c43c0a0a9fec4d4650225/4b4e73f56f4d2a54efe1ecc780261a79162f76ee9fd5ab04d1c6ac4b78e9974ddc3a5c9fe1088eb545779a89f64afd21bcd293871991e41500d184e812e1fdf8',
        'kar1@qq.com', 'kar1', '18623333333', '', '', '');

-- 导出  表 be_base_20240901.base_user_info 结构
CREATE TABLE IF NOT EXISTS `base_user_info`
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

-- 正在导出表  be_base_20240901.base_user_info 的数据：~0 rows (大约)

-- 导出  表 be_base_20240901.base_user_info_0 结构
CREATE TABLE IF NOT EXISTS `base_user_info_0`
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

-- 正在导出表  be_base_20240901.base_user_info_0 的数据：~3 rows (大约)
INSERT INTO `base_user_info_0` (`id`, `uuid`, `nickname`, `bio`, `avatar_file_id`, `sign_up_type`,
                                `last_active_time`, `last_ip`, `last_region`)
VALUES (0, 'admin', 'admin', '', -1, 101, '2024-10-24 18:52:15', '127.0.0.1',
        '0|0|0|内网IP|内网IP'),
       (240902163618005502, '91a518a5234249a8a312bbc8fb6571c2', 'nickname_kar2', '', -1, 101,
        '2024-10-24 18:56:57', '127.0.0.1', '0|0|0|内网IP|内网IP'),
       (240912095958005834, 'b6c7ebff8b044b60a889ed81edc8c8e9', 't1', '', -1, 101,
        '2024-09-12 09:59:59', '0:0:0:0:0:0:0:1', 'ipv6#0:0:0:0:0:0:0:1');

-- 导出  表 be_base_20240901.base_user_info_1 结构
CREATE TABLE IF NOT EXISTS `base_user_info_1`
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

-- 正在导出表  be_base_20240901.base_user_info_1 的数据：~1 rows (大约)
INSERT INTO `base_user_info_1` (`id`, `uuid`, `nickname`, `bio`, `avatar_file_id`, `sign_up_type`,
                                `last_active_time`, `last_ip`, `last_region`)
VALUES (240902154357005281, '36870faf0934422399872a0260c1596a', 'nickname_kar1', 'kar1_bio', -1,
        101, '2024-10-24 18:52:41', '127.0.0.1', '0|0|0|内网IP|内网IP');

/*!40103 SET TIME_ZONE = IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;
