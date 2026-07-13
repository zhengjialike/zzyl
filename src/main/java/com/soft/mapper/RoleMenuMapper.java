package com.soft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soft.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Delete("DELETE FROM t_role_menu WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Select("SELECT menu_id FROM t_role_menu WHERE role_id = #{roleId}")
    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);
}
