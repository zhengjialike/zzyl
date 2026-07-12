package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 床位表
 * @TableName t_bed
 */
@TableName(value ="t_bed")
@Data
public class Bed {
    /**
     * 床位ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属房间ID（关联t_room.id）
     */
    private Integer roomId;

    /**
     * 床位号，如：1011、1012
     */
    private String bedNumber;

    /**
     * 当前入住的老人ID（关联t_elderly.id，为空表示空闲）
     */
    private Integer elderlyId;

    /**
     * 床位状态（0-空闲，1-已入住）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}