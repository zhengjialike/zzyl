package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName t_nursing_item
 */
@TableName(value ="t_nursing_item")
@Data
public class NursingItem {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String itemname;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private String sort;

    /**
     * 
     */
    private String islock;

    /**
     * 
     */
    private String image;

    /**
     * 
     */
    private String description;
}