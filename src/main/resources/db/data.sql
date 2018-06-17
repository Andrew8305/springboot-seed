-- ----------------------------
-- Records of `user`
-- admin:admin
-- user:user
-- user1:user
-- user2:user
-- ...
-- ----------------------------
INSERT INTO `user` (id, username, password, role) VALUES ('1', 'admin', '$2a$10$y0fYK8gDeSKly.zsLxbHReWQFbk65IIFQUVdkgvODz0jsyRoUpXJm', 'admin');
INSERT INTO `user` (id, username, password) VALUES ('2', 'user', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('3', 'user1', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('4', 'user2', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('5', 'user3', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('6', 'user4', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('7', 'user5', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('8', 'user6', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('9', 'user7', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('10', 'user8', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES ('11', 'user9', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');
INSERT INTO `user` (id, username, password) VALUES (12, 'user10', '$2a$10$bTipogXC0Wd4IlhQFuPkJ.sm0k0CTr4P80wPHcEBXeJ0wHdKSTBZy');

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
COMMIT;