/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : example

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 14/06/2019 15:31:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for TEST_USER
-- ----------------------------
DROP TABLE IF EXISTS `TEST_USER`;
CREATE TABLE `TEST_USER` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id自增主键',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  `age` int(3) DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `lid` varchar(255) DEFAULT NULL COMMENT '父类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2938179 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

