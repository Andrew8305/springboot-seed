SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `target` varchar(50) DEFAULT NULL,
  `permission_name` varchar(50) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
--  Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `department_name` varchar(50) DEFAULT NULL,
  `duty` varchar(50) DEFAULT NULL,
  `comment` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `auth_type` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `register_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `role` varchar(50) DEFAULT NULL,
  `open_id` varchar(50) DEFAULT NULL,
  `union_id` varchar(50) DEFAULT NULL,
  `session_key` varchar(50) DEFAULT '',
  `nickName` varchar(100) DEFAULT '',
  `gender` smallint NOT NULL DEFAULT '0',
  `language` varchar(50) DEFAULT '',
  `city` varchar(100) DEFAULT '',
  `province` varchar(100) DEFAULT '',
  `country` varchar(100) DEFAULT '',
  `avatar_url` varchar(200) DEFAULT '',
  `sms_code` varchar(200) DEFAULT '',
  `sms_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);
ALTER TABLE `user` ADD UNIQUE (`username`);
ALTER TABLE `user` ADD UNIQUE (`email`);
ALTER TABLE `user` ADD UNIQUE (`phone`);
ALTER TABLE `user` ADD UNIQUE (`open_id`);
ALTER TABLE `user` ADD UNIQUE (`union_id`);

-- ----------------------------
--  Table structure for `car`
-- ----------------------------
DROP TABLE IF EXISTS `car`;
CREATE TABLE `car` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `car_number` varchar(100) DEFAULT '',
  `car_type` varchar(100) DEFAULT '',
  `owner` varchar(100) DEFAULT '',
  `license_picture` varchar(100) DEFAULT '',
  `vin_code` varchar(100) DEFAULT '',
  `engine_code` varchar(100) DEFAULT '',
  `register_date` DATETIME DEFAULT NULL,
  `issue_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
);

SET FOREIGN_KEY_CHECKS = 1;
