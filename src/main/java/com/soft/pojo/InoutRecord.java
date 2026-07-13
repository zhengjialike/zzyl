package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_inout_record
 */
@TableName(value ="t_inout_record")
@Data
public class InoutRecord {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String ordercode;

    /**
     * 
     */
    private String oldname;

    /**
     * 
     */
    private String cardid;

    /**
     * 
     */
    private Date inouttime;

    /**
     * 
     */
    private String createuser;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String status;
}