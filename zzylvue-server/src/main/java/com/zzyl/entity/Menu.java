package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_menu")
public class Menu {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String menuName;
    private Long parentId;
    private String path;
    private String icon;
    private Integer sort;
    private String type;
    private String perms;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
