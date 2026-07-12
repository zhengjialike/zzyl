package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Role;
import com.soft.service.RoleService;
import com.soft.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 12
* @description 针对表【t_role】的数据库操作Service实现
* @createDate 2026-07-12 20:47:57
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




