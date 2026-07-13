package com.soft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soft.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectRolesByUserId(@Param("userId") Integer userId);
}
