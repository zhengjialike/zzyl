package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_ai_assistant
 */
@TableName(value ="t_ai_assistant")
@Data
public class AiAssistant {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer oldid;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String airesult;

    /**
     * 
     */
    private String aitype;

    /**
     * 
     */
    private String inputmsg;
}