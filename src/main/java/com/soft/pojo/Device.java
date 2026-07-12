package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 设备表
 * @TableName t_device
 */
@TableName(value ="t_device")
@Data
public class Device {
    /**
     * 设备ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备唯一标识
     */
    private String deviceName;

    /**
     * 备注名称
     */
    private String remarkName;

    /**
     * 所属产品ID
     */
    private Integer productId;

    /**
     * 接入位置类型（1-房间，2-床位，3-老人本人）
     */
    private Integer locationType;

    /**
     * 接入位置ID（对应t_room.id / t_bed.id / t_elderly.id）
     */
    private Integer locationId;

    /**
     * 设备在线状态（0-离线，1-在线）
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