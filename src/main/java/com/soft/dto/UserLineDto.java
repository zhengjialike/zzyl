package com.soft.dto;

import lombok.Data;

/*封装用户登录身份验证通过后信息*/
@Data
public class UserLineDto {

    private Integer id;
    private String uname;
    private String sex;
    private String phone;
    private String image;
}
