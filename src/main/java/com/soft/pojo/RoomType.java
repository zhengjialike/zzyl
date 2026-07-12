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
 * 房型表
 * @TableName t_room_type
 */
@TableName(value ="t_room_type")
@Data
public class RoomType {
    /**
     * 房型ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 房型名称，如：四人间、豪华单人间
     */
    private String typeName;

    /**
     * 床位费用（元/月）
     */
    private BigDecimal price;

    /**
     * 房型图片URL
     */
    private String image;

    /**
     * 房型介绍
     */
    private String description;

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