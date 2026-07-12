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

@TableName(value = "t_check_out")
@Data
public class CheckOut {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String billNo;
    private Integer elderId;
    private String elderName;
    private String idCard;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    private Integer currentStep;
    private String flowStatus;
    private String applicant;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String remark;

    @TableField("checkout_reason")
    private String reason;

    private String approver;
    @TableField("approve_opinion")
    private String approveRemark;
    @TableField("settlement_status")
    private String settleStatus;
    @TableField("settlement_amount")
    private BigDecimal settlementAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate terminateDate;
    private String terminateAgreement;
    @TableField("refund_method")
    private String refundWay;
    private String refundRemark;
    private String refundVoucher;
    private BigDecimal refundAmount;
    private String approveResult;
    private String nursingLevel;
    private String bedNo;
    private String advisor;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billEndDate;

    /** 以下字段用于退住详情聚合展示，不直接映射到 t_check_out。 */
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private Integer contractId;
    @TableField(exist = false)
    private String contractNo;
}
