package com.soft.dto;

import lombok.Data;

@Data
public class UserPwdDto {

    //封装用户密码更新数据
    private Integer id;
    private String oldpwd;
    private String newpwd;

}
