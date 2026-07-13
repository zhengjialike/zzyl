package com.zzyl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soft.pojo.Role;

public interface RoleMapper extends BaseMapper<Role> {
    @org.apache.ibatis.annotations.Select("SELECT r.* FROM t_role r INNER JOIN t_user_role ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    java.util.List<Role> selectRolesByUserId(@org.apache.ibatis.annotations.Param("userId") Integer userId);
}
