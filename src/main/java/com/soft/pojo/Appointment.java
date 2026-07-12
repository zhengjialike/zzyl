package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 预约记录表
 * @TableName t_appointment
 */
@TableName(value ="t_appointment")
@Data
public class Appointment {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 预约类型：参观预约/探访预约
     */
    private String appointmentType;

    /**
     * 预约人姓名
     */
    private String visitorName;

    /**
     * 预约人手机号
     */
    private String visitorPhone;

    /**
     * 老人姓名（探访预约时填写）
     */
    private String elderName;

    /**
     * 预约时间
     */
    private LocalDateTime appointmentTime;

    /**
     * 预约状态：0-待上门，1-已完成，2-已取消，3-已过期
     */
    private Integer status;

    /**
     * 实际到院时间
     */
    private LocalDateTime arrivalTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
