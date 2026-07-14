package com.soft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_inout_record")
@Data
public class InoutRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String ordercode;
    private String oldname;
    private String cardid;

    private Date inouttime;

    private String createuser;

    private Date createtime;

    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
