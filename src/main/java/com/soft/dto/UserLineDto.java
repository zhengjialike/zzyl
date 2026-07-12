package com.soft.dto;

import lombok.Data;

/*封装用户登录身份验证通过后信息*/
@Data
public class UserLineDto {

    private Integer id;
    private String uname;        // 对应前端的"姓名"
    private String sex;          // 性别
    private String phone;        // 手机号
    private String image;        // 头像
    
    // 新增字段 - 部门和职位
    private Integer deptId;
    private String deptName;     // 部门名称
    
    private Integer positionId;
    private String positionName; // 职位名称
    
    private String roleName;     // 角色名称
    
    // 新增字段 - 邮箱和账号
    private String account;      // 账号
    private String email;        // 邮箱
}
