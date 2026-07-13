package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 护理任务表
 * @TableName t_nursing_task
 */
@TableName(value ="t_nursing_task")
@Data
public class NursingTask {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务编号，如ZD204810101500110012
     */
    private String taskNo;

    /**
     * 老人ID，关联t_elderly.id
     */
    private Integer elderlyId;

    /**
     * 床位ID，关联t_bed.id
     */
    private Integer bedId;

    /**
     * 护理项目ID，关联t_nursing_item.id
     */
    private Integer nursingItemId;

    /**
     * 关联订单ID（计划外任务来自订单）
     */
    private Integer orderId;

    /**
     * 项目类型：护理计划内/护理计划外
     */
    private String itemType;

    /**
     * 护理员ID，关联t_user.id（负责该床位的护理员）
     */
    private Integer nurseId;

    /**
     * 期望服务时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedServiceTime;

    /**
     * 任务状态：0-待执行 1-已执行 2-已取消
     */
    private Integer status;

    /**
     * 执行人ID，关联t_user.id（实际执行任务的人）
     */
    private Integer executorId;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime executionTime;

    /**
     * 执行记录文本
     */
    private String executionRecord;

    /**
     * 执行照片URL
     */
    private String executionImage;

    /**
     * 取消人ID，关联t_user.id
     */
    private Integer cancelerId;

    /**
     * 取消时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String elderlyName;

    @TableField(exist = false)
    private String bedNumber;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String nurseName;

    @TableField(exist = false)
    private String executorName;

    @TableField(exist = false)
    private String cancelerName;

    @TableField(exist = false)
    private String gender;

    @TableField(exist = false)
    private Integer age;

    @TableField(exist = false)
    private String nursingLevel;

    @TableField(exist = false)
    private String nurseNames;

    @TableField(exist = false)
    private String elderlyPhoto;
}