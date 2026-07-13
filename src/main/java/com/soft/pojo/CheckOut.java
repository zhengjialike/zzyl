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
/**
 * 退住业务单实体，对应申请、两类审批、合同解除、账单调整和费用清算七步流程。
 * 老人最终状态和床位释放只在费用清算完成时更新。
 */
public class CheckOut {
    // ---------- 单据、老人和流程状态 ----------
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

    /** 数据库列使用 checkout_reason，页面/实体使用更自然的 reason。 */
    @TableField("checkout_reason")
    private String reason;

    // ---------- 审批和费用清算 ----------
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

    // ---------- 从最近一次已完成入住单复制的只读业务快照 ----------
    private String nursingLevel;
    private String bedNo;
    private String advisor;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billEndDate;

    /** 以下字段由老人表、合同表聚合，仅用于退住详情展示，不映射到 t_check_out。 */
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private Integer contractId;
    @TableField(exist = false)
    private String contractNo;
}
