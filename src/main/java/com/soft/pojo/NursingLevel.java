package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @TableName t_nursing_level
 */
@TableName(value ="t_nursing_level")
@Data
public class NursingLevel implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String levelname;
    private String islock;
    private String createuser;
    private String description;
    private Integer plainid;
    private BigDecimal money;
    @TableField(exist = false)
    private String plainname;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}