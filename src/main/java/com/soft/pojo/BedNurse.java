package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 床位-护理员关联表
 * @TableName t_bed_nurse
 */
@TableName(value ="t_bed_nurse")
@Data
public class BedNurse {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 床位ID，关联t_bed.id
     */
    private Integer bedId;

    /**
     * 护理员ID，关联t_user.id
     */
    private Integer nurseId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}