# zzyl-server - 智慧养老服务平台后端

## 技术栈
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.5
- MySQL 8.0+
- Maven 3.6+

## 快速启动

### 1. 创建数据库
```sql
-- 执行初始化脚本
source src/main/resources/init.sql
```

### 2. 修改配置
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zzyl?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 启动项目
```bash
mvn spring-boot:run
```

默认端口：`8080`

## 项目结构
```
src/main/java/com/zzyl/
├── ZzylApplication.java        # 启动类
├── common/                     # 公共类
│   ├── Result.java             # 统一响应
│   ├── PageResult.java         # 分页结果
│   └── BaseEntity.java         # 实体基类
├── config/                     # 配置
│   ├── MyBatisPlusConfig.java  # MyBatis-Plus 配置
│   ├── MyMetaObjectHandler.java # 自动填充
│   └── WebConfig.java          # CORS + 拦截器
├── entity/                     # 实体类
├── mapper/                     # Mapper 接口
├── service/                    # 服务接口
│   └── impl/                   # 服务实现
├── controller/                 # 控制器
└── interceptor/                # 拦截器
    └── LoginInterceptor.java   # 登录校验
```

## API 概览

### 登录
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /login | 登录 |
| GET | /loadInfo | 获取当前用户信息 |
| GET | /logout | 退出 |
| GET | /sysMenus | 获取菜单树 |

### 权限管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /user/page | 用户分页 |
| POST | /user/add | 新增用户 |
| POST | /user/update | 修改用户 |
| POST | /user/updateStatus | 启用/禁用 |
| POST | /user/resetPassword | 重置密码 |
| GET | /user/roleIds/{id} | 用户角色ID列表 |
| GET | /role/list | 角色列表 |
| POST | /role/add | 新增角色 |
| POST | /role/update | 修改角色 |
| POST | /role/updateStatus | 启用/禁用 |
| POST | /role/saveMenuPerms | 保存菜单权限 |
| GET | /role/menuIds/{id} | 角色菜单ID列表 |
| POST | /role/saveDataScope | 保存数据权限 |
| GET | /role/dataScope/{id} | 获取数据权限 |
| GET | /menu/tree | 菜单树 |
| POST | /menu/add | 新增菜单 |
| POST | /menu/update | 修改菜单 |
| POST | /menu/updateStatus | 启用/禁用 |
| GET | /menu/buttons/{menuId} | 按钮列表 |
| POST | /menu/addButton | 新增按钮 |
| POST | /menu/updateButton | 修改按钮 |
| POST | /menu/updateButtonStatus | 按钮启用/禁用 |
| GET | /dept/tree | 部门树 |
| POST | /dept/add | 新增部门 |
| POST | /dept/update | 修改部门 |
| POST | /dept/updateStatus | 启用/禁用 |
| POST | /position/page | 职位分页 |
| POST | /position/add | 新增职位 |
| POST | /position/update | 修改职位 |
| POST | /position/updateStatus | 启用/禁用 |

### 消息中心
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /message/page | 消息分页 |
| POST | /message/markRead | 标记已读 |
| POST | /message/markAllRead | 全部已读 |
| POST | /message/delete | 删除消息 |
| POST | /message/deleteAll | 删除全部 |
| GET | /message/unreadCount | 未读数 |

### 请假管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /leave/page | 请假分页 |
| POST | /leave/add | 新增请假 |
| GET | /leave/{id} | 请假详情 |
| POST | /leave/returnBack | 填写返回时间 |

### 财务管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /bill/page | 账单分页 |
| GET | /bill/{id} | 账单详情 |
| POST | /bill/generateMonthly | 生成月度账单 |
| POST | /bill/pay | 支付 |
| POST | /bill/cancel | 取消 |
| POST | /prepaid/page | 充值记录分页 |
| POST | /prepaid/recharge | 充值 |
| POST | /elderBalance/page | 余额分页 |
