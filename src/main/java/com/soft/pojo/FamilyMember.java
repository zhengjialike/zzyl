package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@TableName(value = "t_family_member")
@Data
public class FamilyMember {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer checkInId;
    @TableField("family_name")
    private String name;
    @TableField("family_phone")
    private String phone;
    @TableField("relationship")
    private String relation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}