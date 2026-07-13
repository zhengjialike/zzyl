package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_visit_record
 */
@TableName(value ="t_visit_record")
@Data
public class VisitRecord {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String visittype;

    /**
     * 
     */
    private String visitname;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String oldname;

    /**
     * 
     */
    private Date visittime;

    /**
     * 
     */
    private String createuser;

    /**
     * 
     */
    private Date createtime;
}