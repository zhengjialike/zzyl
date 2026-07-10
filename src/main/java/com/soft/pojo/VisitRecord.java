package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 来访记录表
 * @TableName t_visit_record
 */
@TableName(value ="t_visit_record")
@Data
public class VisitRecord {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 来访类型：参观来访/探访来访
     */
    private String visitType;

    /**
     * 来访人姓名
     */
    private String visitorName;

    /**
     * 来访人手机号
     */
    private String visitorPhone;

    /**
     * 老人姓名（探访来访时填写）
     */
    private String elderName;

    /**
     * 来访时间
     */
    private LocalDateTime visitTime;

    /**
     * 来源：0-预约到院，1-直接登记
     */
    private Integer source;

    /**
     * 关联的预约ID（如果是预约到院则填写，直接登记则为NULL）
     */
    private Integer appointmentId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}