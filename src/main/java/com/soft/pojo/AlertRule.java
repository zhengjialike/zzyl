package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 报警规则表
 * @TableName t_alert_rule
 */
@TableName(value ="t_alert_rule")
@Data
public class AlertRule {
    /**
     * 报警规则ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 报警规则名称
     */
    private String ruleName;

    /**
     * 所属产品ID（关联t_product.id）
     */
    private Integer productId;

    /**
     * 功能名称（从产品表的functions字段解析）
     */
    private String functionName;

    /**
     * 关联设备ID（0表示所有设备，非0表示指定设备）
     */
    private String deviceId;

    /**
     * 运算符（如：>、<、=、!=）
     */
    private String operator;

    /**
     * 阈值
     */
    private BigDecimal threshold;

    /**
     * 持续触发周期数
     */
    private Integer durationPeriod;

    /**
     * 数据聚合周期（分钟）
     */
    private Integer dataAggregationPeriod;

    /**
     * 报警生效开始时间
     */
    private Time effectiveStart;

    /**
     * 报警生效结束时间
     */
    private Time effectiveEnd;

    /**
     * 报警沉默周期（分钟）
     */
    private Integer silentMinutes;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}