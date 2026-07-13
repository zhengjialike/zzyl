package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_elder_balance")
public class ElderBalance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String elderName;
    private String elderIdCard;
    private String bedNo;
    private Double prepaidBalance;
    private Double depositBalance;
    private LocalDateTime changeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
