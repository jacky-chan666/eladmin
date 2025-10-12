-- MySQL dump 10.13  Distrib 9.4.0, for Win64 (x86_64)
--
-- Host: localhost    Database: eladmin
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user`
(
    `user_id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dept_id`        bigint                                  DEFAULT NULL COMMENT '部门名称',
    `username`       varchar(180) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
    `nick_name`      varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
    `gender`         varchar(2) COLLATE utf8mb4_general_ci   DEFAULT NULL COMMENT '性别',
    `phone`          varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
    `email`          varchar(180) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
    `avatar_name`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像地址',
    `avatar_path`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像真实路径',
    `password`       varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
    `is_admin`       bit(1)                                  DEFAULT b'0' COMMENT '是否为admin账号',
    `enabled`        bit(1)                                  DEFAULT NULL COMMENT '状态：1启用、0禁用',
    `create_by`      varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建者',
    `update_by`      varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者',
    `pwd_reset_time` datetime                                DEFAULT NULL COMMENT '修改密码的时间',
    `create_time`    datetime                                DEFAULT NULL COMMENT '创建日期',
    `update_time`    datetime                                DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE KEY `uniq_email` (`email`) USING BTREE,
    UNIQUE KEY `uniq_username` (`username`) USING BTREE,
    KEY              `idx_dept_id` (`dept_id`) USING BTREE,
    KEY              `idx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK
TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user`
VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '201507802@qq.com', 'avatar-20250122102642222.png',
        '/Users/jie/Documents/work/private/eladmin/~/avatar/avatar-20250122102642222.png',
        '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', _binary '', _binary '', NULL, 'admin',
        '2020-05-03 16:38:31', '2018-08-23 09:11:56', '2025-01-22 10:26:42'),
       (2, 7, 'test', '测试', '男', '19999999999', '231@qq.com', NULL, NULL,
        '$2a$10$4XcyudOYTSz6fue6KFNMHeUQnCX5jbBQypLEnGk1PmekXt5c95JcK', _binary '\0', _binary '', 'admin', 'admin',
        NULL, '2020-05-05 11:15:49', '2025-01-21 14:53:04'),
       (3, 7, '张三', '张三', '男', '13544437309', '123@qq.com', NULL, NULL,
        '$2a$10$O0ISKNQpF/79uwFRaQzhl.t66VdZ6M1JiorzAhtek041hkAEGDPGC', _binary '\0', _binary '', 'admin', 'admin',
        NULL, '2025-09-17 21:17:53', '2025-09-17 21:17:59'),
       (4, 2, 'lisi', 'lisi', '男', '13888888888', 'admin@eladmin.net', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (7, 2, 'lisi0', 'lisi', '男', '13888888888', 'admin@eladmin.net0', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (8, 2, 'lisi1', 'lisi', '男', '13888888888', 'admin@eladmin.net1', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (9, 2, 'lisi2', 'lisi', '男', '13888888888', 'admin@eladmin.net2', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (10, 2, 'lisi3', 'lisi', '男', '13888888888', 'admin@eladmin.net3', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (11, 2, 'lisi4', 'lisi', '男', '13888888888', 'admin@eladmin.net4', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (12, 2, 'lisi5', 'lisi', '男', '13888888888', 'admin@eladmin.net5', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (13, 2, 'lisi6', 'lisi', '男', '13888888888', 'admin@eladmin.net6', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL),
       (14, 2, 'lisi7', 'lisi', '男', '13888888888', 'admin@eladmin.net7', NULL, NULL, NULL, _binary '\0', _binary '',
        NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK
TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-17 22:26:02
