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
    private Integer elderId;
    private String elderName;
    private String billType;
    private String billMonth;
    @TableField("item_name")
    private String nursingItemName;
    private BigDecimal payableAmount;
    private BigDecimal paidAmount;
    private BigDecimal refundableAmount;
    private BigDecimal actualRefund;
    private String status;
    private String payCategory;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer checkOutId;
    private String adjustRemark;
    @TableField("refundable_deposit")
    private BigDecimal refundableDeposit;
    private BigDecimal prepaidAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}