package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 退款记录表
 * @TableName t_refund
 */
@TableName(value ="t_refund")
@Data
public class Refund {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 退款编号
     */
    private String refundNo;

    /**
     * 关联订单ID
     */
    private Integer orderId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 状态：0-处理中 1-成功 2-失败
     */
    private Integer status;

    /**
     * 申请人
     */
    private Integer customerId;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款渠道
     */
    private String refundChannel;

    /**
     * 退款方式
     */
    private String refundMethod;

    /**
     * 申请时间
     */
    private LocalDateTime appliedAt;

    /**
     * 退款时间
     */
    private LocalDateTime refundedAt;

    /**
     * 
     */
    private LocalDateTime createTime;
}