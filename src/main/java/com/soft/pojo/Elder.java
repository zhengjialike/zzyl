package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "t_elder")
@Data
public class Elder {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String idCard;

    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String phone;

    private String address;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}