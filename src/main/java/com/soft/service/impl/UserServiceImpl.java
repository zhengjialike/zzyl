package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.common.PageResult;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.mapper.UserMapper;
import com.soft.pojo.User;
import com.soft.pojo.UserRole;
import com.soft.mapper.UserMapper;
import com.soft.mapper.UserRoleMapper;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired private UserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<User> findPage(int pageNum, int pageSize, String name, String email, String status, Long deptId) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) { wrapper.like(User::getRealname, name); }
        if (StringUtils.hasText(email)) { wrapper.like(User::getEmail, email); }
        if (StringUtils.hasText(status)) { wrapper.eq(User::getIslock, Integer.valueOf(status)); }
        if (deptId != null) { wrapper.eq(User::getDeptId, deptId); }
        wrapper.orderByDesc(User::getId);
        IPage<User> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override @Transactional
    public void addUser(User user, Long[] roleIds) {
        user.setUpwd("888itcast.CN764%..."); baseMapper.insert(user);
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) { UserRole ur = new UserRole(); ur.setUserId(user.getId()); ur.setRoleId(roleId); userRoleMapper.insert(ur); }
        }
    }

    @Override @Transactional
    public void updateUser(User user, Long[] roleIds) {
        baseMapper.updateById(user); userRoleMapper.deleteByUserId(user.getId().longValue());
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) { UserRole ur = new UserRole(); ur.setUserId(user.getId()); ur.setRoleId(roleId); userRoleMapper.insert(ur); }
        }
    }

    @Override
    public void updateStatus(Long id, String status) { User user = new User(); user.setId(id.intValue()); user.setIslock(Integer.valueOf(status)); baseMapper.updateById(user); }

    @Override
    public void resetPassword(Long id) { User user = new User(); user.setId(id.intValue()); user.setUpwd("888itcast.CN764%..."); baseMapper.updateById(user); }

    @Override
    public Long[] getUserRoleIds(Long userId) { return userRoleMapper.getRoleIdsByUserId(userId).toArray(new Long[0]); }

    @Override
    public Map<String, Object> updateUserPwdService(UserPwdDto userDto) {
        Map<String, Object> result = new HashMap<>();
        User user = this.getById(userDto.getId());
        if (user == null) {
            result.put("code", 400);
            result.put("msg", "用户不存在");
            return result;
        }
        if (!user.getUpwd().equals(userDto.getOldpwd())) {
            result.put("msg", "原始密码不正确......");
            return result;
        }
        User updateUser = new User();
        updateUser.setId(userDto.getId());
        updateUser.setUpwd(userDto.getNewpwd());
        userMapper.updateById(updateUser);
        result.put("code", 200);
        result.put("msg", "修改密码成功");
        return result;
    }

    @Override
    public Map<String, Object> queryUserPageList(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            String realname = (String) params.get("realname");
            String position = (String) params.get("position");
            Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);

            QueryWrapper<User> wrapper = new QueryWrapper<>();

            if (StringUtils.hasText(realname)) {
                wrapper.like("realname", realname);
            }

            if (StringUtils.hasText(position)) {
                wrapper.like("realname", position);
            }

            Page<User> page = new Page<>(pageNum, pageSize);
            IPage<User> iPage = this.page(page, wrapper);

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("users", iPage.getRecords());
            result.put("total", iPage.getTotal());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
