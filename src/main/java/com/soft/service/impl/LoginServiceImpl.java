package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft.pojo.User;
import com.soft.mapper.UserMapper;
import com.soft.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired private UserMapper userMapper;
    @Autowired private HttpSession session;

    @Override
    public Map<String, Object> login(String account, String upwd) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, account).or().eq(User::getEmail, account);
        User user = userMapper.selectOne(wrapper);
        if (user == null) { result.put("code", 500); result.put("msg", "账号不存在"); return result; }
        if (Integer.valueOf(1).equals(user.getIslock())) { result.put("code", 500); result.put("msg", "账号已禁用，请联系管理员"); return result; }
        if (!user.getUpwd().equals(upwd)) { result.put("code", 500); result.put("msg", "密码错误"); return result; }
        session.setAttribute("userId", user.getId().longValue());
        session.setAttribute("realName", user.getRealname());
        result.put("code", 200); result.put("msg", "登录成功"); return result;
    }

    @Override
    public Map<String, Object> loadInfo(Long userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.selectById(userId.intValue());
        if (user != null) { result.put("uname", user.getRealname()); }
        return result;
    }

    @Override
    public void logout() { session.invalidate(); }
}
