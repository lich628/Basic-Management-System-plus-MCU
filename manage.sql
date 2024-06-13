/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:3306
 Source Schema         : manage

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 13/06/2024 08:49:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for batch_goods
-- ----------------------------
DROP TABLE IF EXISTS `batch_goods`;
CREATE TABLE `batch_goods`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `goods_id` int NOT NULL,
  `quantity` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of batch_goods
-- ----------------------------
INSERT INTO `batch_goods` VALUES (86, '20240524123135824', 1, 30);
INSERT INTO `batch_goods` VALUES (87, '20240524123135824', 2, 30);
INSERT INTO `batch_goods` VALUES (88, '20240524123333470', 1, 30);

-- ----------------------------
-- Table structure for batches
-- ----------------------------
DROP TABLE IF EXISTS `batches`;
CREATE TABLE `batches`  (
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `batch_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` int NULL DEFAULT NULL,
  `reviewer_id` int NULL DEFAULT NULL,
  `op_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `review_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `batch_status` int NULL DEFAULT NULL,
  `op_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `viewer_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `card_uid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_closed` int NULL DEFAULT 0,
  PRIMARY KEY (`batch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of batches
-- ----------------------------
INSERT INTO `batches` VALUES ('20240524123135824', 'in', 42, 42, '2024/5/24 12:31:43', '2024/6/5 15:58:50', 0, '入库！', '', '', 0);
INSERT INTO `batches` VALUES ('20240524123333470', 'out', 42, 42, '2024/5/24 12:33:39', '2024/5/24 12:33:41', 1, '出库', '允许', 'A3285F16', 0);

-- ----------------------------
-- Table structure for env_records
-- ----------------------------
DROP TABLE IF EXISTS `env_records`;
CREATE TABLE `env_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `temperature` double NULL DEFAULT NULL,
  `humidity` double NULL DEFAULT NULL,
  `smoke_sensor` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13567 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of env_records
-- ----------------------------
INSERT INTO `env_records` VALUES (13555, 28, 52, '689.0', '2024/6/6 10:00:22');
INSERT INTO `env_records` VALUES (13556, 28, 52, '630.0', '2024/6/6 10:00:24');
INSERT INTO `env_records` VALUES (13565, 28.5, 43, '246.0', '2024/6/11 14:21:34');
INSERT INTO `env_records` VALUES (13566, 28.5, 43, '239.0', '2024/6/11 14:21:36');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `goods_id` int NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `goods_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `goods_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `current_quantity` int NULL DEFAULT NULL,
  `quantity_mark` int NULL DEFAULT NULL,
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '医用外科口罩', '防护用品', 'Medical  Masks', 10, 20, 'http://localhost:8088/img/医用外科口罩.jpg');
INSERT INTO `goods` VALUES (2, '防毒面具', '防护用品', 'respirator', 30, 20, 'http://localhost:8088/img/防毒面具.jpg');
INSERT INTO `goods` VALUES (3, '防疫防护服', '防护用品', 'protective clothing', 50, 20, 'http://localhost:8088/img/防疫防护服.jpg');
INSERT INTO `goods` VALUES (4, '卷纸纸巾', '日常用品', 'toilet rolls', 50, 20, 'http://localhost:8088/img/卷纸纸巾.jpg');
INSERT INTO `goods` VALUES (5, 'N95口罩', '防护用品', 'N95 mask', 50, 20, 'http://localhost:8088/img/N95口罩.jpg');
INSERT INTO `goods` VALUES (6, '84消毒液', '日常用品', 'NaCIO  disinfectant', 50, 20, 'http://localhost:8088/img/84消毒液.jpg');
INSERT INTO `goods` VALUES (7, '救生圈', '生命救助', 'life-ring', 50, 20, 'http://localhost:8088/img/救生圈.jpg');
INSERT INTO `goods` VALUES (8, '黄色施工安全头盔', '安全防护', 'Construction safety helmets', 50, 20, 'http://localhost:8088/img/施工安全头盔.jpg');
INSERT INTO `goods` VALUES (9, '自动体外心脏除颤器（AED）', '生命救助', 'automated external defibrillator', 50, 20, 'http://localhost:8088/img/AED急救工具.jpg');
INSERT INTO `goods` VALUES (10, '红外测温枪', '器材工具', 'infrared thermometer', 50, 20, 'http://localhost:8088/img/红外测温枪.jpg');
INSERT INTO `goods` VALUES (11, '防洪沙袋', '防灾抢险', 'Sand Bag', 50, 20, 'http://localhost:8088/img/防洪沙袋.jpg');
INSERT INTO `goods` VALUES (12, '柴油发电机', '应急能源', 'Diesel Generator', 50, 20, 'http://localhost:8088/img/柴油发电机.jpg');
INSERT INTO `goods` VALUES (13, '医用酒精', '医疗物资', 'Medicinal Alcohol', 50, 20, 'http://localhost:8088/img/医用酒精.jpg');
INSERT INTO `goods` VALUES (14, '手电筒', '照明设备', 'Flashlight', 50, 20, 'http://localhost:8088/img/手电筒.jpg');
INSERT INTO `goods` VALUES (15, '急救绷带', '医疗物资', 'Bandaid', 30, 20, 'http://localhost:8088/img/急救绷带.jpg');
INSERT INTO `goods` VALUES (16, '应急出口指示灯', '器材工具', 'Emergency Exit Indicator', 50, 20, 'http://localhost:8088/img/安全出口指示灯.jpg');
INSERT INTO `goods` VALUES (17, '应急干粮', '临时食宿', 'Emergency Rations', 50, 20, 'http://localhost:8088/img/应急干粮.jpg');
INSERT INTO `goods` VALUES (18, '消毒紫光灯', '器材工具', 'Ultraviolet disinfection lamp', 50, 20, 'http://localhost:8088/img/消毒紫光灯.jpg');
INSERT INTO `goods` VALUES (60, '洗手液', '日常用品', 'hand sanitizer', 0, 50, 'http://localhost:8088/img/洗手液.png.png');

-- ----------------------------
-- Table structure for io_records
-- ----------------------------
DROP TABLE IF EXISTS `io_records`;
CREATE TABLE `io_records`  (
  `io_id` int NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operator_id` int NULL DEFAULT NULL,
  `operator_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`io_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of io_records
-- ----------------------------
INSERT INTO `io_records` VALUES (15, '20240524123135824', '2024/5/24 12:32:44', 42, '李飞', 'in');
INSERT INTO `io_records` VALUES (16, '20240524123333470', '2024/5/24 12:34:08', 42, '李飞', 'out');

-- ----------------------------
-- Table structure for logistics
-- ----------------------------
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `destination` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logistics
-- ----------------------------
INSERT INTO `logistics` VALUES (2, '20240524095682671', '运输中', '到达三里屯转运中心', '北京');
INSERT INTO `logistics` VALUES (3, '20240524095713794', '派件中', '正在派件', '广州');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '待入库',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (14, '20240524123135824', '待入库', '重庆市11区11街道111号', '供应商');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `gender` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `state` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `time` datetime NULL DEFAULT NULL,
  `des` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 908 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (895, '张三', '男', '15143355464', '正常', '2024-05-02 14:03:52', NULL);
INSERT INTO `record` VALUES (906, '李飞', '男', '13143528009', '正常', '2024-05-15 21:51:14', NULL);

-- ----------------------------
-- Table structure for suppliers
-- ----------------------------
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of suppliers
-- ----------------------------
INSERT INTO `suppliers` VALUES (2, '供应商Beta', '北京市东城区长安街', '13140000002', '华北仓库');
INSERT INTO `suppliers` VALUES (3, '供应商Charlie', '上海市陆家嘴环路', '13140000003', '华东仓库');
INSERT INTO `suppliers` VALUES (4, '供应商Delta', '重庆市11区11街道111号', '13140000004', '西南仓库');
INSERT INTO `suppliers` VALUES (16, '供应商Alpha', '广东省广州市花都区新华镇4号', '13140000001', '华南仓库');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_account` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `user_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名字',
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `sex` int NULL DEFAULT 1 COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `role` int UNSIGNED NULL DEFAULT 2 COMMENT '角色 0超级管理，1审核员，2操作员',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 561 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (42, 'admin', '李飞', '123456', 22, 1, '131435280091', 0, 'http://localhost:8088/avatar/default_boy.png');
INSERT INTO `users` VALUES (43, 'hellomc', '小明', '123456', 24, 0, '17360785643', 1, 'http://localhost:8088/avatar/default_boy.png');
INSERT INTO `users` VALUES (48, 'admin6', '小李', 'test', 22, 0, '13458479625', 1, 'http://localhost:8088/avatar/default_boy.png');
INSERT INTO `users` VALUES (51, 'lch123', '张三', '123123', 0, 0, '15423685972', 2, 'http://localhost:8088/avatar/张三-2024-06-06_14-14-27_white.jpg');
INSERT INTO `users` VALUES (54, '4797790', '王五', '790970', 21, 0, '17360785643', 2, NULL);
INSERT INTO `users` VALUES (55, 'ergegr123', '马六', 'ergreg', 23, 1, '16645872359', 2, NULL);

SET FOREIGN_KEY_CHECKS = 1;
