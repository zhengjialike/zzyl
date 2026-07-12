package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_dept")
public class Dept {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String deptName;
    private Long parentId;
    private Integer sort;
    private String leader;
    private Integer status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
