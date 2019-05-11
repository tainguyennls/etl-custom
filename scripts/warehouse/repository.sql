/*
 Navicat Premium Data Transfer

 Source Server         : WareHouse
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 13.92.197.43:3306
 Source Schema         : warehouse

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 11/05/2019 19:35:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for repository
-- ----------------------------
DROP TABLE IF EXISTS `repository`;
CREATE TABLE `repository`  (
  `ID_SK` bigint(20) NOT NULL,
  `STUDENT_ID` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `FULL_NAME` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `DATE_OF_BIRTH` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `ADDRESS` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `GENDER` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `SOURCE_ID` int(11) NULL DEFAULT NULL,
  `SOURCE_NAME` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `TIME_IMPORT` datetime(0) NULL DEFAULT NULL,
  `IS_ACTIVE` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_SK`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
