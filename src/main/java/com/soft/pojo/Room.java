package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 房间表
 * @TableName t_room
 */
@TableName(value ="t_room")
@Data
public class Room {
    /**
     * 房间ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属楼层ID（关联t_floor.id）
     */
    private Integer floorId;

    /**
     * 房型ID（关联t_room_type.id）
     */
    private Integer roomTypeId;

    /**
     * 房间号，如：101、102
     */
    private String roomNumber;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 房间状态（0-空闲，1-部分入住，2-已满）
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