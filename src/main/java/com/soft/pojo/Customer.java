package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 客户/家属表（小程序使用者）
 * @TableName t_customer
 */
@TableName(value ="t_customer")
@Data
public class Customer {
    /**
     * 客户ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 微信小程序唯一标识
     */
    private String openid;

    /**
     * 微信开放平台UnionID
     */
    private String unionid;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像地址
     */
    private String avatarUrl;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 客户真实姓名（家属本人）
     */
    private String realName;

    /**
     * 账户状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}