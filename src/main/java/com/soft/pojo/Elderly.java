package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 老人档案表（被服务对象）
 * @TableName t_elderly
 */
@TableName(value ="t_elderly")
@Data
public class Elderly {
    /**
     * 老人ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 老人真实姓名
     */
    private String realName;

    /**
     * 身份证号（正式环境需加密）
     */
    private String idCard;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 老人本人手机号
     */
    private String phone;

    /**
     * 状态（0-未入住，1-已入住，2-已退住）
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