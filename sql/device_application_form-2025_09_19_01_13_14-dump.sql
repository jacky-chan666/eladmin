-- MySQL dump 10.13  Distrib 9.4.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: eladmin
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
-- Table structure for table `device_application_form`
--

DROP TABLE IF EXISTS `device_application_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_application_form` (
  `id` int NOT NULL AUTO_INCREMENT,
  `applicant_id` varchar(50) NOT NULL COMMENT '申请单UUID',
  `applicant_name` varchar(30) NOT NULL COMMENT '申请人姓名',
  `department` varchar(30) NOT NULL COMMENT '所属部门',
  `application_date` date NOT NULL COMMENT '申请日期',
  `application_data_id` int NOT NULL COMMENT '申请数据id',
  `application_type` int NOT NULL COMMENT '申请单类型：新增，修改，上线，下线',
  `application_data_type` int NOT NULL COMMENT '申请单数据类型：omada，vigi，adblocking',
  `application_title` varchar(30) NOT NULL COMMENT '申请单标题',
  `application_reason` varchar(30) NOT NULL COMMENT '申请理由',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '申请状态',
  `test_contact` varchar(30) NOT NULL COMMENT '测试接口人',
  `test_leader` varchar(30) NOT NULL COMMENT '测试组长',
  `dev_contact` varchar(30) NOT NULL COMMENT '研发接口人',
  `dev_leader` varchar(30) NOT NULL COMMENT '研发组长',
  `test_contact_approval` tinyint NOT NULL DEFAULT '0' COMMENT '测试接口人审批状态',
  `test_leader_approval` tinyint NOT NULL DEFAULT '0' COMMENT '测试组长审批状态',
  `dev_contact_approval` tinyint NOT NULL DEFAULT '0' COMMENT '研发接口人审批状态',
  `dev_leader_approval` tinyint NOT NULL DEFAULT '0' COMMENT '研发组长审批状态',
  `test_contact_comment` varchar(30) NOT NULL DEFAULT '-' COMMENT '测试接口人审批意见',
  `test_leader_comment` varchar(30) NOT NULL DEFAULT '-' COMMENT '测试组长审批意见',
  `dev_contact_comment` varchar(30) NOT NULL DEFAULT '-' COMMENT '研发接口人审批意见',
  `dev_leader_comment` varchar(30) NOT NULL DEFAULT '-' COMMENT '研发组长意见审批',
  `current_approvers` text COMMENT '当前审核人列表（JSON格式存储）',
  `approval_history` text COMMENT '审核历史表（JSON格式存储，记录每次提交的审批人，审批状态和审批意见）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `device_info_details` text COMMENT '设备信息详情（JSON格式存储）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `applicant_id` (`applicant_id`),
  UNIQUE KEY `application_data_id` (`application_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='设备信息申请单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_application_form`
--

LOCK TABLES `device_application_form` WRITE;
/*!40000 ALTER TABLE `device_application_form` DISABLE KEYS */;
INSERT INTO `device_application_form` VALUES (1,'app-001','张伟','研发部','2025-04-01',1001,1,1,'Omada 新增配置申请','新项目上线需求',0,'李雷','王芳','周杰','陈晨',0,0,0,0,'-','-','-','-','[\"王芳\",\"陈晨\"]','[]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(2,'app-002','刘洋','测试部','2025-04-02',1002,2,2,'Vigi 规则修改','修复误报问题',0,'赵敏','王芳','吴磊','陈晨',0,0,0,0,'-','-','-','-','[\"王芳\"]','[{\"approver\":\"王芳\",\"role\":\"test_lead\",\"status\":1,\"comment\":\"同意\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(3,'app-003','陈静','安全组','2025-04-03',1003,1,3,'ADBlocking 规则新增','拦截广告域名',0,'孙浩','刘洋','郑凯','陈晨',0,0,0,0,'-','-','-','-','[\"刘洋\"]','[{\"approver\":\"张伟\",\"role\":\"dev_lead\",\"status\":1,\"comment\":\"初步通过\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(4,'app-004','吴磊','研发部','2025-04-04',1004,3,1,'Omada 固件上线','版本v2.1发布',0,'钱程','赵敏','冯萧','周杰',0,0,0,0,'-','-','-','-','[\"周杰\"]','[{\"approver\":\"王芳\",\"role\":\"test_lead\",\"status\":1,\"comment\":\"通过\"},{\"approver\":\"周杰\",\"role\":\"dev_lead\",\"status\":1,\"comment\":\"确认发布\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(5,'app-005','郑凯','运维组','2025-04-05',1005,3,2,'Vigi 固件升级','提升稳定性',0,'李雷','王芳','张伟','陈晨',0,0,0,0,'-','-','-','-','[\"陈晨\"]','[{\"approver\":\"王芳\",\"status\":1,\"comment\":\"通过\"},{\"approver\":\"陈晨\",\"status\":1,\"comment\":\"进入校验\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(6,'app-006','周杰','研发部','2025-04-06',1006,4,1,'Omada 下线旧版本','停用v1.5',0,'孙浩','刘洋','吴磊','郑凯',0,0,0,0,'-','-','-','-','[\"郑凯\"]','[{\"approver\":\"刘洋\",\"status\":1,\"comment\":\"同意下线\"},{\"approver\":\"郑凯\",\"status\":1,\"comment\":\"执行中\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(7,'app-007','王芳','测试部','2025-03-20',1007,3,3,'ADBlocking 策略上线','应对新型广告',0,'赵敏','刘洋','冯萧','周杰',0,0,0,0,'-','-','-','-','[]','[{\"approver\":\"赵敏\",\"status\":1,\"comment\":\"通过\"},{\"approver\":\"周杰\",\"status\":1,\"comment\":\"已部署\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(8,'app-008','冯萧','研发部','2025-03-18',1008,4,1,'Omada 旧策略下线','资源回收',0,'钱程','王芳','郑凯','陈晨',0,0,0,0,'-','-','-','-','[]','[{\"approver\":\"王芳\",\"status\":1,\"comment\":\"同意\"},{\"approver\":\"陈晨\",\"status\":1,\"comment\":\"已完成下线\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(9,'app-009','孙浩','安全组','2025-04-07',1009,1,2,'Vigi 新规则申请','尝试拦截恶意IP',0,'李雷','赵敏','张伟','周杰',0,0,0,0,'-','-','-','-','[]','[{\"approver\":\"赵敏\",\"status\":0,\"comment\":\"信息不全，请补充日志依据\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(10,'app-010','钱程','测试部','2025-04-08',1010,2,1,'Omada 配置微调','优化性能参数',0,'孙浩','刘洋','吴磊','郑凯',0,0,0,0,'-','-','-','-','[\"刘洋\",\"郑凯\"]','[{\"approver\":\"张伟\",\"status\":1,\"comment\":\"建议通过\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(11,'app-011','赵敏','研发部','2025-04-09',1011,2,3,'ADBlocking 规则更新','调整匹配逻辑',0,'陈静','王芳','周杰','陈晨',0,0,0,0,'-','-','-','-','[\"王芳\"]','[]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(12,'app-012','李雷','运维组','2025-04-10',1012,3,1,'Omada 全量发布','正式环境推送',0,'钱程','刘洋','冯萧','周杰',0,0,0,0,'-','-','-','-','[\"周杰\"]','[{\"approver\":\"刘洋\",\"status\":1,\"comment\":\"测试通过\"}]','2025-09-17 21:45:44','2025-09-17 21:45:44',NULL),(13,'bcd07b3f-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-08-25',10001,0,0,'新增设备接入申请','功能扩展需要',9,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(14,'bcd0a67e-946f-11f0-aa1a-e0d362104aef','admin','运维部','2025-09-18',10002,1,1,'配置修改申请','修复bug',3,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"tom\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(15,'bcd0a823-946f-11f0-aa1a-e0d362104aef','admin','安全中心','2025-08-26',10003,2,2,'新版本上线申请','迭代发布',9,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"alice\", \"bob\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(16,'bcd0ab4e-946f-11f0-aa1a-e0d362104aef','admin','测试部','2025-08-22',10004,0,0,'数据采集权限申请','项目启动',0,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"admin\", \"kate\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(17,'bcd0ad49-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-09-07',10005,3,1,'旧设备下线申请','退役计划',9,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"devops\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(18,'bcd0ae54-946f-11f0-aa1a-e0d362104aef','admin','产品部','2025-09-15',10006,0,2,'Omada设备接入','客户接入',10,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"pm\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(19,'bcd0af4d-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-09-03',10007,1,0,'Vigi规则调整','误报优化',8,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(20,'bcd0b01c-946f-11f0-aa1a-e0d362104aef','admin','安全部','2025-09-17',10008,2,1,'Adblocking上线','反广告策略',1,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"security\", \"admin\", \"legal\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(21,'bcd0b0f9-946f-11f0-aa1a-e0d362104aef','admin','测试部','2025-09-03',10009,0,2,'监控系统升级','提升性能',1,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"lead-test\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(22,'bcd0b1f6-946f-11f0-aa1a-e0d362104aef','admin','运维部','2025-09-17',10010,3,0,'废弃接口关闭','清理资源',10,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"admin\", \"network-admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(23,'bcd0b2ec-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-08-30',10011,1,1,'API参数变更','兼容性调整',3,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"tech-lead\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(24,'bcd0b3b8-946f-11f0-aa1a-e0d362104aef','admin','测试部','2025-09-03',10012,0,0,'自动化脚本部署','CI/CD集成',7,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"jenkins\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(25,'bcd0b4b3-946f-11f0-aa1a-e0d362104aef','admin','安全中心','2025-08-26',10013,2,2,'证书更新申请','过期预警',10,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"security-officer\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(26,'bcd0b580-946f-11f0-aa1a-e0d362104aef','admin','产品部','2025-09-01',10014,0,1,'用户行为分析接入','数据埋点',9,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"product-manager\", \"data-team\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(27,'bcd0b73c-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-08-27',10015,1,0,'数据库索引优化','查询提速',1,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"dba\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(28,'bcd0b80a-946f-11f0-aa1a-e0d362104aef','admin','测试部','2025-09-11',10016,2,2,'压力测试执行申请','性能验证',10,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"perf-engineer\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(29,'bcd0b995-946f-11f0-aa1a-e0d362104aef','admin','运维部','2025-08-22',10017,3,1,'日志归档任务终止','已完成归档',8,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(30,'bcd0bac9-946f-11f0-aa1a-e0d362104aef','admin','安全部','2025-09-18',10018,0,0,'防火墙策略开放','调试需要',9,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"firewall-admin\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(31,'bcd0bb90-946f-11f0-aa1a-e0d362104aef','admin','研发部','2025-09-09',10019,1,2,'缓存机制调整','减少延迟',10,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"architect\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL),(32,'bcd0bc57-946f-11f0-aa1a-e0d362104aef','admin','测试部','2025-08-22',10020,0,1,'灰度发布通道开启','分阶段上线',6,'lisi','wangwu','john','zhaoliu',0,0,0,0,'-','-','-','-','[\"release-manager\", \"qa-lead\", \"admin\"]','[]','2025-09-18 01:13:27','2025-09-18 01:13:27',NULL);
/*!40000 ALTER TABLE `device_application_form` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-19  1:13:14
