-- ----------------------------
-- Records of `user`
-- admin:admin
-- user:user
-- user1:user
-- user2:user
-- ...
-- ----------------------------
INSERT INTO `user` (id, username, password, role) VALUES ('1', 'admin', '$2a$10$y0fYK8gDeSKly.zsLxbHReWQFbk65IIFQUVdkgvODz0jsyRoUpXJm', 'admin');
INSERT INTO `user` (id, username, password, role) VALUES ('2', 'user', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('3', 'user1', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('4', 'user2', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('5', 'user3', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('6', 'user4', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('7', 'user5', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('8', 'user6', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('9', 'user7', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('10', 'user8', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('11', 'user9', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, role) VALUES ('12', 'user10', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');

INSERT INTO `permission` VALUES ('1', '1', 'user', 'edit', 'edit');
INSERT INTO `permission` VALUES ('2', '1', '*', 'query', 'edit');
INSERT INTO `permission` VALUES ('3', '2', '*', 'delete', 'delete');
INSERT INTO `permission` VALUES ('4', '2', 'role', 'add', 'add');
INSERT INTO `permission` VALUES ('5', '3', 'role', 'query', 'query');
INSERT INTO `permission` VALUES ('6', '3', 'user', 'add', 'add');

INSERT INTO `department` VALUES ('1', '1', 'hr', 'manager', 'hr manager');
INSERT INTO `department` VALUES ('2', '2', 'research', 'developer', 'research developer');
INSERT INTO `department` VALUES ('3', '3', 'research', 'developer', 'research developer');
INSERT INTO `department` VALUES ('4', '3', 'management', 'CTO', 'CTO');

INSERT INTO `park` (id, `name`, address, province, city, longitude, latitude, user_id, fee_id, `count`, remaining_count, is_member_only)
  VALUES (1, '东源大厦地上停车场', '海淀区成府路35号', '北京', '北京', 116.337292, 39.993615, 1, 1, 200, 100, 0);
INSERT INTO `park` (id, `name`, address, province, city, longitude, latitude, user_id, fee_id, `count`, remaining_count, is_member_only)
  VALUES (2, '停车场(西单大悦城东南)', '西单北大街131号西单大悦城F1层', '北京', '北京', 116.372988, 39.910236, 1, 2, 300, 150, 1);

INSERT INTO `fee` (id, is_free, parameters, free_minutes, per_hour, limit_per_day)
  VALUES (1, 0, 'per_hour|limit_per_day', 30, 2, 20);

INSERT INTO `fee` (id, is_free, parameters, free_minutes, per_hour, limit_per_day)
  VALUES (2, 0, 'per_hour|limit_per_day', 15, 3, 40);


INSERT INTO `car_fee` (id, park_id, user_id, car_number)
  VALUES (1, 1, null, '1111111');

INSERT INTO `car_fee` (id, park_id, user_id, car_number)
  VALUES (2, 2, null, '1111111');

INSERT INTO `car_fee` (id, park_id, user_id, car_number, payment_time, payment_amount)
  VALUES (3, 1, 13, '1111111', '2018-07-27 12:00:00', 4);

INSERT INTO `car_fee` (id, park_id, user_id, car_number, payment_time, payment_amount)
  VALUES (4, 1, 13, '1111111', '2018-07-27 14:30:00', 7);

COMMIT;
