-- ----------------------------
-- Records of `user`
-- admin:admin
-- user:user
-- user1:user
-- user2:user
-- ...
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '$2a$10$y0fYK8gDeSKly.zsLxbHReWQFbk65IIFQUVdkgvODz0jsyRoUpXJm', '2017-05-07 0:0:0', '1', 'admin');
INSERT INTO `user` VALUES ('2', 'user', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('3', 'user1', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('4', 'user2', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('5', 'user3', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('6', 'user4', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('7', 'user5', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('8', 'user6', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('9', 'user7', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('10', 'user8', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('11', 'user9', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');
INSERT INTO `user` VALUES ('12', 'user10', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', '2017-05-07 0:0:0', '1', 'user');

INSERT INTO `role` VALUES ('1', 'root', 'root');
INSERT INTO `role` VALUES ('2', 'admin', 'admin');
INSERT INTO `role` VALUES ('3', 'user', 'user');
INSERT INTO `role` VALUES ('4', 'guest', 'guest');

INSERT INTO `user_role` VALUES ('1', '1', '1');
INSERT INTO `user_role` VALUES ('2', '2', '2');
INSERT INTO `user_role` VALUES ('3', '2', '3');
INSERT INTO `user_role` VALUES ('4', '3', '3');
INSERT INTO `user_role` VALUES ('5', '4', '3');

COMMIT;