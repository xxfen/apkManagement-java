/*
 Navicat Premium Data Transfer

 Source Server         : mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : apkmanagement

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 09/01/2023 10:00:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_apk
-- ----------------------------
DROP TABLE IF EXISTS `tb_apk`;
CREATE TABLE `tb_apk`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `appPackage` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `vName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `vCode` int(0) NULL DEFAULT NULL,
  `apkPath` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `fileSize` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `forcedUpdating` tinyint(1) NULL DEFAULT NULL COMMENT '是否强制更新',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `appPackage_UNIQUE`(`appPackage`) USING BTREE,
  UNIQUE INDEX `vName`(`vName`, `vCode`) USING BTREE,
  CONSTRAINT `tb_apk_ibfk_1` FOREIGN KEY (`appPackage`) REFERENCES `tb_application` (`appPackage`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_app_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_app_user`;
CREATE TABLE `tb_app_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `appPackage` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `appUserName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `appPackage`(`appPackage`, `appUserName`) USING BTREE,
  CONSTRAINT `tb_app_user_ibfk_1` FOREIGN KEY (`appPackage`) REFERENCES `tb_application` (`appPackage`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_application
-- ----------------------------
DROP TABLE IF EXISTS `tb_application`;
CREATE TABLE `tb_application`  (
  `appPackage` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `appName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `companyId` int(0) NULL DEFAULT NULL,
  `userId` int(0) NULL DEFAULT NULL,
  `id` int(0) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `appPackage_UNIQUE`(`appPackage`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `companyId`(`companyId`) USING BTREE,
  CONSTRAINT `tb_application_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tb_application_ibfk_2` FOREIGN KEY (`companyId`) REFERENCES `tb_company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_company
-- ----------------------------
DROP TABLE IF EXISTS `tb_company`;
CREATE TABLE `tb_company`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `companyName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `companyName_UNIQUE`(`companyName`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_download_statistics
-- ----------------------------
DROP TABLE IF EXISTS `tb_download_statistics`;
CREATE TABLE `tb_download_statistics`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `appUserId` int(0) NOT NULL,
  `apkId` int(0) NOT NULL,
  `count` int(0) NULL DEFAULT 1,
  `updateTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_link`;
CREATE TABLE `tb_link`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `apkId` int(0) NOT NULL,
  `link` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `deadTime` timestamp(0) NULL DEFAULT NULL COMMENT '失效时间',
  `count` int(0) NULL DEFAULT 0,
  `disabledCount` int(0) NULL DEFAULT NULL COMMENT '限制下载次数',
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `userId` int(0) NOT NULL,
  `disabled` tinyint(1) NULL DEFAULT 0 COMMENT '是否失效、禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `link_UNIQUE`(`link`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `tb_link_ibfk_1`(`apkId`) USING BTREE,
  CONSTRAINT `tb_link_ibfk_1` FOREIGN KEY (`apkId`) REFERENCES `tb_apk` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `tb_link_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `userPassword` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '密码',
  `companyId` int(0) NOT NULL COMMENT '公司id',
  `root` tinyint(1) NULL DEFAULT NULL COMMENT '是否是管理员',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `creationTime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone_UNIQUE`(`phone`) USING BTREE,
  INDEX `companyId`(`companyId`) USING BTREE,
  CONSTRAINT `tb_user_ibfk_1` FOREIGN KEY (`companyId`) REFERENCES `tb_company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
