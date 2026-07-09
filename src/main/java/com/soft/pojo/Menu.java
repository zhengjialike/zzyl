package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 
 * @TableName t_menu
 */
@TableName(value ="t_menu")
@Data
public class Menu {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private Integer pid;

    /**
     * 
     */
    private String mname;

    /**
     * 
     */
    private String path;

    /**
     * 
     */
    private Integer sort;

    /**
     * 
     */
    private Integer visible;

    @TableField(exist = false)
    private List<Menu> subItems;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}