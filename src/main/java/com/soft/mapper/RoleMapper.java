package com.soft.mapper;

import com.soft.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 12
* @description 针对表【t_role】的数据库操作Mapper
* @createDate 2026-07-12 20:47:57
* @Entity com.soft.pojo.Role
*/
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Integer userId);

}




