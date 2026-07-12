package com.zzyl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzyl.entity.RoleDataScope;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleDataScopeMapper extends BaseMapper<RoleDataScope> {
    @Delete("DELETE FROM sys_role_data_scope WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT * FROM sys_role_data_scope WHERE role_id = #{roleId}")
    RoleDataScope getByRoleId(@Param("roleId") Long roleId);
}
