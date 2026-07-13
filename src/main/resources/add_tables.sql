USE zzyl;
CREATE TABLE IF NOT EXISTS sys_role (id BIGINT AUTO_INCREMENT PRIMARY KEY, role_name VARCHAR(50), role_code VARCHAR(50), status VARCHAR(10), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, del_flag INT DEFAULT 0) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_user_role (id BIGINT AUTO_INCREMENT PRIMARY KEY, user_id BIGINT, role_id BIGINT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_role_menu (id BIGINT AUTO_INCREMENT PRIMARY KEY, role_id BIGINT, menu_id BIGINT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_role_data_scope (id BIGINT AUTO_INCREMENT PRIMARY KEY, role_id BIGINT, data_scope VARCHAR(20), dept_ids VARCHAR(500)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_menu (id BIGINT AUTO_INCREMENT PRIMARY KEY, menu_name VARCHAR(50), parent_id BIGINT DEFAULT 0, path VARCHAR(200), icon VARCHAR(100), sort INT DEFAULT 0, type VARCHAR(20) DEFAULT 'menu', perms VARCHAR(100), status VARCHAR(10), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, del_flag INT DEFAULT 0) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_dept (id BIGINT AUTO_INCREMENT PRIMARY KEY, dept_name VARCHAR(50), parent_id BIGINT DEFAULT 0, sort INT DEFAULT 0, leader VARCHAR(50), status VARCHAR(10), description VARCHAR(300), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, del_flag INT DEFAULT 0) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE IF NOT EXISTS sys_position (id BIGINT AUTO_INCREMENT PRIMARY KEY, position_name VARCHAR(50), dept_id BIGINT, status VARCHAR(10), description VARCHAR(300), create_time DATETIME DEFAULT CURRENT_TIMESTAMP, update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, del_flag INT DEFAULT 0) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT IGNORE INTO sys_role (id, role_name, status) VALUES (1, '管理员', '启用');
INSERT IGNORE INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(1,'工作台',0,'/UserInfo',1,'menu','启用'),(2,'来访管理',0,'',2,'menu','启用'),(3,'入退管理',0,'',3,'menu','启用'),
(4,'在住管理',0,'',4,'menu','启用'),(5,'服务管理',0,'',5,'menu','启用'),(6,'订单管理',0,'',6,'menu','启用'),
(7,'财务管理',0,'',7,'menu','启用'),(8,'客户管理',0,'',8,'menu','启用'),(9,'权限管理',0,'',9,'menu','启用'),
(10,'协同工作',0,'',10,'menu','启用'),(11,'智能监测',0,'',11,'menu','启用'),(12,'个人中心',0,'',12,'menu','启用'),
(13,'消息中心',0,'',13,'menu','启用'),
(14,'用户管理',9,'/userManagement',1,'menu','启用'),(15,'角色管理',9,'/roleManagement',2,'menu','启用'),
(16,'菜单管理',9,'/menuManagement',3,'menu','启用'),(17,'部门管理',9,'/deptManagement',4,'menu','启用'),
(18,'职位管理',9,'/positionManagement',5,'menu','启用'),
(19,'请假管理',4,'/leaveManagement',3,'menu','启用'),(20,'消息通知',13,'/messageCenter',1,'menu','启用'),
(21,'入账列表',7,'/billList',1,'menu','启用'),(22,'欠费老人',7,'/overdueElders',2,'menu','启用'),
(23,'预缴款充值',7,'/prepaidRecharge',3,'menu','启用'),(24,'余额查询',7,'/balanceQuery',4,'menu','启用');
INSERT IGNORE INTO sys_dept (id, dept_name, parent_id, sort, status) VALUES (1,'中州养老院',0,1,'启用'),(2,'财务部',1,2,'启用'),(3,'行政部',1,3,'启用');
INSERT IGNORE INTO sys_position (id, position_name, dept_id, status) VALUES (1,'员工',1,'启用');
