package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_prepaid")
public class Prepaid {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String prepaidNo;
    private String elderName;
    private String elderIdCard;
    private String bedNo;
    private Double amount;
    private String paymentMethod;
    private String paymentVoucher;
    private String remark;
    private String creator;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
