package com.soft.dto;

import lombok.Data;

@Data
public class UserDto {
    private String account;   // 账号
    private String upwd;      // 密码（明文或密文，视业务而定）
}
