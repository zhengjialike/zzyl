package com.soft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_ai_assistant")
@Data
public class AiAssistant implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer oldid;

    private Date createtime;

    private String inputmsg;

    private String airesult;

    private String aitype;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
