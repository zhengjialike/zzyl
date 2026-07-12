package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role_data_scope")
public class RoleDataScope {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long roleId;
    private String dataScope;
    private String deptIds;
}
