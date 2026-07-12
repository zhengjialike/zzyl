package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.UserRole;
import com.soft.service.UserRoleService;
import com.soft.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 12
* @description 针对表【t_user_role】的数据库操作Service实现
* @createDate 2026-07-12 20:48:20
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




