package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzyl.entity.User;
import com.zzyl.entity.UserRole;
import com.zzyl.entity.Role;
import com.zzyl.entity.Menu;
import com.zzyl.mapper.*;
import com.zzyl.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private HttpSession session;

    @Override
    public Map<String, Object> login(String account, String upwd) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, account).or().eq(User::getEmail, account);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            result.put("code", 500);
            result.put("msg", "账号不存在");
            return result;
        }
        if (Integer.valueOf(0).equals(user.getStatus())) {
            result.put("code", 500);
            result.put("msg", "账号已禁用，请联系管理员");
            return result;
        }
        if (!user.getPassword().equals(upwd)) {
            result.put("code", 500);
            result.put("msg", "密码错误");
            return result;
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("realName", user.getRealName());
        result.put("code", 200);
        result.put("msg", "登录成功");
        return result;
    }

    @Override
    public Map<String, Object> loadInfo(Long userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.selectById(userId);
        if (user != null) {
            result.put("uname", user.getRealName());
        }
        return result;
    }

    @Override
    public void logout() {
        session.invalidate();
    }
}
