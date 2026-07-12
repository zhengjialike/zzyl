package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 楼层表
 * @TableName t_floor
 */
@TableName(value ="t_floor")
@Data
public class Floor {
    /**
     * 楼层ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 楼层名称，如：1楼、2楼
     */
    private String floorName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}