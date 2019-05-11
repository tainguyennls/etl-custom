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

 Date: 11/05/2019 19:29:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_file_configuration
-- ----------------------------
DROP TABLE IF EXISTS `data_file_configuration`;
CREATE TABLE `data_file_configuration`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FEED_NAME` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REMOTE_DIR` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `LOCAL_DIR` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `HOST_NAME` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `PORT` int(11) NULL DEFAULT NULL,
  `USER_HOST` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `USER_PASS` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `COLUMNS_HOST_FEED` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `PROPS_STAGING_FOR_WAREHOUSE` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `PROPS_WAREHOUSE` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `FEED_DELIM` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `SRC_FEED` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `TABLE_STAGING` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `DATE_FORMAT` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `MAIL_GROUP` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `IS_ACTIVE` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
