-- 创建数据库
CREATE DATABASE IF NOT EXISTS `mall_sys` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_sys`;

-- ============ 系统权限表 ============

-- 1. 系统用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `username` varchar(50) NOT NULL COMMENT '用户名',
                            `password` varchar(100) NOT NULL COMMENT 'BCrypt 加密密码',
                            `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
                            `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                            `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
                            `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 2. 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `role_name` varchar(50) NOT NULL COMMENT '角色名称',
                            `role_key` varchar(50) NOT NULL COMMENT '角色标识',
                            `sort` int DEFAULT '0' COMMENT '排序',
                            `status` tinyint DEFAULT '1' COMMENT '状态',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. 菜单权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `parent_id` bigint DEFAULT '0' COMMENT '父 ID',
                            `name` varchar(50) NOT NULL COMMENT '名称',
                            `path` varchar(200) DEFAULT NULL COMMENT '路径',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件',
                            `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
                            `type` tinyint NOT NULL DEFAULT '0' COMMENT '类型：0-目录，1-菜单，2-按钮',
                            `icon` varchar(50) DEFAULT NULL COMMENT '图标',
                            `sort` int DEFAULT '0' COMMENT '排序',
                            `visible` tinyint DEFAULT '1' COMMENT '是否可见',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 4. 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint NOT NULL,
                                 `role_id` bigint NOT NULL,
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 5. 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint NOT NULL,
                                 `menu_id` bigint NOT NULL,
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ============ 初始化数据 ============

-- 默认管理员账号：admin / 123456
-- 密码使用 BCrypt 加密（salt rounds = 10）
INSERT INTO `sys_user` VALUES
    (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcva72D8HQYYsTFbJbGv1bGv1bG',
     '超级管理员', NULL, 'admin@mall.com', '13800138000', 1, NOW(), NOW(), 0);

-- 超级管理员角色
INSERT INTO `sys_role` VALUES
    (1, '超级管理员', 'super_admin', 1, 1, NOW(), 0);

-- 用户角色关联
INSERT INTO `sys_user_role` VALUES (1, 1);

-- 菜单数据（系统管理模块）
INSERT INTO `sys_menu` VALUES
                           (1, 0, '系统管理', '/system', 'Layout', NULL, 0, 'Setting', 100, 1, NOW(), 0),
                           (2, 1, '用户管理', 'user', 'system/user/index', 'sys:user:list', 1, 'User', 1, 1, NOW(), 0),
                           (3, 2, '用户查询', '', NULL, 'sys:user:query', 2, NULL, 1, 1, NOW(), 0),
                           (4, 2, '用户新增', '', NULL, 'sys:user:add', 2, NULL, 2, 1, NOW(), 0),
                           (5, 2, '用户修改', '', NULL, 'sys:user:edit', 2, NULL, 3, 1, NOW(), 0),
                           (6, 2, '用户删除', '', NULL, 'sys:user:delete', 2, NULL, 4, 1, NOW(), 0),
                           (7, 1, '角色管理', 'role', 'system/role/index', 'sys:role:list', 1, 'Roles', 2, 1, NOW(), 0),
                           (8, 1, '菜单管理', 'menu', 'system/menu/index', 'sys:menu:list', 1, 'Menu', 3, 1, NOW(), 0);

-- 超级管理员关联所有菜单
INSERT INTO `sys_role_menu` SELECT 1, id FROM `sys_menu`;