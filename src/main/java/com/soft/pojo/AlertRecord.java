package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 报警记录表
 * @TableName t_alert_record
 */
@TableName(value ="t_alert_record")
@Data
public class AlertRecord {
    /**
     * 报警记录ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备ID（关联t_device.id）
     */
    private Integer deviceId;

    /**
     * 触发的报警规则ID（关联t_alert_rule.id）
     */
    private Integer ruleId;

    /**
     * 触发报警时的数据值
     */
    private String dataValue;



    /**
     * 报警发生时间
     */
    private Date alertTime;

    /**
     * 处理状态（0-待处理，1-已处理）
     */
    private Integer handleStatus;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}