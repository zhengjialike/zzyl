package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@TableName(value = "t_apply_log")
@Data
public class ApplyLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String applyType;
    private Integer applyId;
    private String billNo;
    private String stepName;
    private String operator;
    private String role;
    private String operation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}