package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "t_bill")
@Data
public class Bill {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String billNo;
    private Integer elderlyId;
    private BigDecimal billAmount;
    private BigDecimal payableAmount;
    private String billMonth;
    private Integer status;
    private String billType;
    private String paymentMethod;
    private String paymentVoucher;
    private String paymentRemark;
    private String cancelReason;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payDeadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private Integer delFlag;
    private BigDecimal paidAmount;
    private BigDecimal depositAmount;
    private BigDecimal prepaidAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billEnd;
    private Integer totalDays;
}