package com.soft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_elder_balance")
public class ElderBalance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer elderlyId;
    private String bedNo;
    private Double prepaidBalance;
    private Double depositBalance;
    private LocalDateTime changeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
