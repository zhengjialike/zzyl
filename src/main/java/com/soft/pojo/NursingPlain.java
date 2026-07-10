package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 * @TableName t_nursing_plain
 */
@TableName(value ="t_nursing_plain")
@Data
public class NursingPlain implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String plainname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime;

    private String createuser;

    private String islock;

    //扩展属性表示是否禁用列表中的 删除，编辑，禁用
    @TableField(exist = false)
    private Integer flag=0; //0表示启用 1 禁用
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}