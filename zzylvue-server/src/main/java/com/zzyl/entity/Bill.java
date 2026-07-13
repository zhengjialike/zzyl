package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_bill")
public class Bill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String billNo;
    private String elderName;
    private String elderIdCard;
    private Double billAmount;
    private Double payableAmount;
    private Double paidAmount;
    private Double depositAmount;
    private Double prepaidAmount;
    private String billMonth;
    private LocalDateTime billStart;
    private LocalDateTime billEnd;
    private Integer totalDays;
    private Integer status;
    private String billType;
    private String paymentMethod;
    private String paymentVoucher;
    private String paymentRemark;
    private String cancelReason;
    private String creator;
    private LocalDateTime payDeadline;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
