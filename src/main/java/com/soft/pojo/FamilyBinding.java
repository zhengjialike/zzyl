package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 客户（家属）与老人的绑定关系表
 * @TableName t_family_binding
 */
@TableName(value ="t_family_binding")
@Data
public class FamilyBinding {
    /**
     * 关系ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID（关联t_customer.id，即家属）
     */
    private Integer customerId;

    /**
     * 老人ID（关联t_elderly.id，即被绑定的老人）
     */
    private Integer elderlyId;

    /**
     * 称呼/关系（如：父亲、母亲、爷爷）
     */
    private String relationship;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}