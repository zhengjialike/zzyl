-- ============================================
-- 智慧养老服务平台 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS zzyl DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE zzyl;
SET NAMES utf8mb4;
SET SESSION sql_mode = 'NO_ENGINE_SUBSTITUTION';

-- ===== 权限管理 =====

-- 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '上级部门ID',
    sort INT DEFAULT 0 COMMENT '排序',
    leader VARCHAR(50) COMMENT '部门负责人',
    status VARCHAR(10) DEFAULT '启用' COMMENT '启用/禁用',
    description VARCHAR(300) COMMENT '部门说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '部门表';

-- 职位表
CREATE TABLE IF NOT EXISTS sys_position (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    position_name VARCHAR(50) NOT NULL COMMENT '职位名称',
    dept_id BIGINT COMMENT '所属部门ID',
    status VARCHAR(10) DEFAULT '启用' COMMENT '启用/禁用',
    description VARCHAR(300) COMMENT '职位说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '职位表';

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '账号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    password VARCHAR(100) DEFAULT '888itcast.CN764%...' COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    gender VARCHAR(10) COMMENT '性别',
    dept_id BIGINT COMMENT '所属部门ID',
    position_id BIGINT COMMENT '所属职位ID',
    status VARCHAR(10) DEFAULT '启用' COMMENT '启用/禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) COMMENT '角色编码',
    status VARCHAR(10) DEFAULT '启用' COMMENT '启用/禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
) COMMENT '用户角色关联表';

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单/按钮名称',
    parent_id BIGINT DEFAULT 0 COMMENT '上级菜单ID',
    path VARCHAR(200) COMMENT '路由',
    icon VARCHAR(100) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    type VARCHAR(20) DEFAULT 'menu' COMMENT 'menu/button',
    perms VARCHAR(100) COMMENT '权限标识',
    status VARCHAR(10) DEFAULT '启用' COMMENT '启用/禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '菜单表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL
) COMMENT '角色菜单关联表';

-- 角色数据权限表
CREATE TABLE IF NOT EXISTS sys_role_data_scope (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    data_scope VARCHAR(20) DEFAULT 'PERSONAL' COMMENT 'ALL/CUSTOM/SUB/THIS/PERSONAL',
    dept_ids VARCHAR(500) COMMENT '自定义部门ID集合'
) COMMENT '角色数据权限表';

-- ===== 消息中心 =====

-- 消息表
CREATE TABLE IF NOT EXISTS sys_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) COMMENT '消息标题',
    content TEXT COMMENT '消息内容',
    msg_type VARCHAR(50) COMMENT '消息类型(协同工作/报警通知)',
    receiver_id BIGINT COMMENT '接收人ID',
    is_read VARCHAR(10) DEFAULT '未读' COMMENT '未读/已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '消息表';

-- ===== 请假管理 =====

-- 请假表
CREATE TABLE IF NOT EXISTS sys_leave (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    leave_no VARCHAR(50) COMMENT '请假单编号',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    elder_id_card VARCHAR(20) COMMENT '老人身份证号',
    elder_phone VARCHAR(20) COMMENT '联系方式',
    caregiver_level VARCHAR(50) COMMENT '护理等级',
    bed_no VARCHAR(50) COMMENT '入住床位',
    caregiver VARCHAR(100) COMMENT '护理员',
    leave_reason TEXT COMMENT '请假原因',
    companion_type VARCHAR(20) COMMENT '陪同人类型',
    companion_name VARCHAR(20) COMMENT '陪同人姓名',
    companion_phone VARCHAR(20) COMMENT '陪同人联系方式',
    leave_start_time DATETIME COMMENT '请假开始时间',
    expected_return_time DATETIME COMMENT '预计返回时间',
    actual_return_time DATETIME COMMENT '实际返回时间',
    leave_days DECIMAL(5,1) COMMENT '请假天数',
    actual_leave_days DECIMAL(5,1) COMMENT '实际请假天数',
    status VARCHAR(20) DEFAULT '请假中' COMMENT '请假中/已返回/超时未归',
    applicant VARCHAR(50) COMMENT '申请人',
    applicant_id BIGINT COMMENT '申请人ID',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '请假表';

-- ===== 财务管理 =====

-- 账单表
CREATE TABLE IF NOT EXISTS sys_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_no VARCHAR(50) COMMENT '账单编号',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    elder_id_card VARCHAR(20) COMMENT '老人身份证号',
    bill_amount DECIMAL(10,2) DEFAULT 0 COMMENT '账单金额',
    payable_amount DECIMAL(10,2) DEFAULT 0 COMMENT '应付金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
    deposit_amount DECIMAL(10,2) DEFAULT 0 COMMENT '押金',
    prepaid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '预缴款支付',
    bill_month VARCHAR(7) COMMENT '账单月份',
    bill_start DATETIME COMMENT '账单周期开始',
    bill_end DATETIME COMMENT '账单周期结束',
    total_days INT COMMENT '共计天数',
    status VARCHAR(20) DEFAULT '待支付' COMMENT '待支付/已支付/已关闭',
    bill_type VARCHAR(20) DEFAULT '月度账单' COMMENT '月度账单/费用账单',
    payment_method VARCHAR(20) COMMENT '支付方式',
    payment_voucher VARCHAR(200) COMMENT '支付凭证',
    payment_remark VARCHAR(200) COMMENT '支付备注',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    creator VARCHAR(50) COMMENT '创建人',
    pay_deadline DATETIME COMMENT '支付截止时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '账单表';

-- 预缴款充值表
CREATE TABLE IF NOT EXISTS sys_prepaid (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prepaid_no VARCHAR(50) COMMENT '预缴款编号',
    elder_name VARCHAR(50) COMMENT '老人姓名',
    elder_id_card VARCHAR(20) COMMENT '老人身份证号',
    bed_no VARCHAR(50) COMMENT '床位号',
    amount DECIMAL(10,2) DEFAULT 0 COMMENT '充值金额',
    payment_method VARCHAR(20) COMMENT '充值方式',
    payment_voucher VARCHAR(200) COMMENT '充值凭证',
    remark VARCHAR(200) COMMENT '备注',
    creator VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '预缴款充值表';

-- 老人余额表
CREATE TABLE IF NOT EXISTS sys_elder_balance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    elder_name VARCHAR(50) COMMENT '老人姓名',
    elder_id_card VARCHAR(20) COMMENT '老人身份证号',
    bed_no VARCHAR(50) COMMENT '床位号',
    prepaid_balance DECIMAL(10,2) DEFAULT 0 COMMENT '预缴款余额',
    deposit_balance DECIMAL(10,2) DEFAULT 0 COMMENT '押金余额',
    change_time DATETIME COMMENT '变动时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag INT DEFAULT 0
) COMMENT '老人余额表';

-- ===== 初始数据 =====

-- 默认管理员
INSERT INTO sys_user (id, username, real_name, password, email, status) VALUES
(1, 'admin', '管理员', '888itcast', 'admin@zzyl.com', '启用');

-- 默认部门
INSERT INTO sys_dept (id, dept_name, parent_id, sort, status) VALUES
(1, '中州养老院', 0, 1, '启用'),
(2, '财务部', 1, 2, '启用'),
(3, '行政部', 1, 3, '启用'),
(4, '后勤部', 1, 4, '启用'),
(5, '护理部', 1, 5, '启用'),
(6, '销售部', 1, 6, '启用'),
(7, '法务部', 1, 7, '启用'),
(8, '售后保障部', 1, 8, '启用');

-- 默认角色
INSERT INTO sys_role (id, role_name, status) VALUES
(1, '部长', '启用'),
(2, '部门主管', '启用'),
(3, '员工', '启用');

-- 默认菜单 (一级)
INSERT INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(1, '工作台', 0, '/UserInfo', 1, 'menu', '启用'),
(2, '来访管理', 0, '', 2, 'menu', '启用'),
(3, '入退管理', 0, '', 3, 'menu', '启用'),
(4, '在住管理', 0, '', 4, 'menu', '启用'),
(5, '服务管理', 0, '', 5, 'menu', '启用'),
(6, '订单管理', 0, '', 6, 'menu', '启用'),
(7, '财务管理', 0, '', 7, 'menu', '启用'),
(8, '客户管理', 0, '', 8, 'menu', '启用'),
(9, '权限管理', 0, '', 9, 'menu', '启用'),
(10, '协同工作', 0, '', 10, 'menu', '启用'),
(11, '智能监测', 0, '', 11, 'menu', '启用'),
(12, '个人中心', 0, '', 12, 'menu', '启用'),
(13, '消息中心', 0, '', 13, 'menu', '启用');

-- 权限管理子菜单
INSERT INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(14, '用户管理', 9, '/userManagement', 1, 'menu', '启用'),
(15, '角色管理', 9, '/roleManagement', 2, 'menu', '启用'),
(16, '菜单管理', 9, '/menuManagement', 3, 'menu', '启用'),
(17, '部门管理', 9, '/deptManagement', 4, 'menu', '启用'),
(18, '职位管理', 9, '/positionManagement', 5, 'menu', '启用');

-- 请假管理子菜单
INSERT INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(19, '请假管理', 4, '/leaveManagement', 3, 'menu', '启用');

-- 消息中心子菜单
INSERT INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(20, '消息通知', 13, '/messageCenter', 1, 'menu', '启用');

-- 财务子菜单
INSERT INTO sys_menu (id, menu_name, parent_id, path, sort, type, status) VALUES
(21, '入账列表', 7, '/billList', 1, 'menu', '启用'),
(22, '欠费老人', 7, '/overdueElders', 2, 'menu', '启用'),
(23, '预缴款充值', 7, '/prepaidRecharge', 3, 'menu', '启用'),
(24, '余额查询', 7, '/balanceQuery', 4, 'menu', '启用');
