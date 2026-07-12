package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName t_order
 */
@TableName(value ="t_order")
@Data
public class Order {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 关联老人ID
     */
    private Long elderlyId;

    /**
     * 下单客户ID
     */
    private Long customerId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 状态：0-待支付 1-待执行 2-已执行 3-已完成 4-已退款 5-已关闭
     */
    private Integer status;

    /**
     * 关联护理项目ID
     */
    private Long nursingItemId;

    /**
     * 期望服务时间
     */
    private LocalDateTime expectedServiceTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 支付时间
     */
    private LocalDateTime paidAt;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;
}