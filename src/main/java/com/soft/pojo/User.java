package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String account;

    private String upwd;

    private String realname;

    private Integer deptId;

    private Integer positionId;

    private String email;

    private String phone;

    private String sex;

    private Integer islock;

    private String image;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}