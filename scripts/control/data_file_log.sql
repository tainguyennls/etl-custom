/*
 Navicat Premium Data Transfer

 Source Server         : WareHouse
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 13.92.197.43:3306
 Source Schema         : control

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 11/05/2019 19:29:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_file_log
-- ----------------------------
DROP TABLE IF EXISTS `data_file_log`;
CREATE TABLE `data_file_log`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ID_CONF` int(11) NOT NULL,
  `SOURCE_FEED` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ACTION_TYPE` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LOG_STATUS` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TIME_INSERT_LOG` timestamp(0) NOT NULL,
  `ROW_COUNT` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
