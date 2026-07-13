package com.soft.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soft.pojo.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Delete("DELETE FROM t_user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

    @Select("SELECT role_id FROM t_user_role WHERE user_id = #{userId}")
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);
}
