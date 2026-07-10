package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName t_plain_item
 */
@TableName(value ="t_plain_item")
@Data
public class PlainItem {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer itemId;

    /**
     * 
     */
    private Integer plainId;

    /**
     * 
     */
    private String itemname;

    /**
     * 
     */
    private String hlsj;

    /**
     * 
     */
    private String hlzq;

    /**
     * 
     */
    private Integer hlpc;
}