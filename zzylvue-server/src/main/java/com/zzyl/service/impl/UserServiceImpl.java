package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.User;
import com.zzyl.entity.UserRole;
import com.zzyl.mapper.UserMapper;
import com.zzyl.mapper.UserRoleMapper;
import com.zzyl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public PageResult<User> findPage(int pageNum, int pageSize, String name, String email, String status, Long deptId) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(User::getRealName, name);
        }
        if (StringUtils.hasText(email)) {
            wrapper.like(User::getEmail, email);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(User::getStatus, status);
        }
        if (deptId != null) {
            wrapper.eq(User::getDeptId, deptId);
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    @Transactional
    public void addUser(User user, Long[] roleIds) {
        user.setPassword("888itcast.CN764%...");
        baseMapper.insert(user);
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            }
        }
    }

    @Override
    @Transactional
    public void updateUser(User user, Long[] roleIds) {
        baseMapper.updateById(user);
        userRoleMapper.deleteByUserId(user.getId());
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            }
        }
    }

    @Override
    public void updateStatus(Long id, String status) {
        User user = new User();
        user.setId(id);
        user.setStatus(Integer.valueOf(status));
        baseMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long id) {
        User user = new User();
        user.setId(id);
        user.setPassword("888itcast.CN764%...");
        baseMapper.updateById(user);
    }

    @Override
    public Long[] getUserRoleIds(Long userId) {
        return userRoleMapper.getRoleIdsByUserId(userId).toArray(new Long[0]);
    }
}
