-- ----------------------------
-- Records of `user`
-- admin:admin
-- user:user
-- user1:user
-- user2:user
-- ...
-- ----------------------------
INSERT INTO `user` (id, username, password, authority) VALUES ('1', 'admin', '$2a$10$y0fYK8gDeSKly.zsLxbHReWQFbk65IIFQUVdkgvODz0jsyRoUpXJm', 'admin');
INSERT INTO `user` (id, username, password, authority) VALUES ('2', 'user', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('3', 'user1', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('4', 'user2', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('5', 'user3', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('6', 'user4', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('7', 'user5', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('8', 'user6', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('9', 'user7', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('10', 'user8', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES ('11', 'user9', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');
INSERT INTO `user` (id, username, password, authority) VALUES (12, 'user10', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy', 'user');

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