package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import com.soft.service.UserService;
import com.soft.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 12
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2026-07-06 09:23:02
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> queryUserService(UserDto userDto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        String account = userDto.getAccount();

        // 创建 QueryWrapper 对象封装查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account); // where account = ?

        // 根据账号查询数据
        List<User> users = userMapper.selectList(wrapper);
        if (users == null || users.isEmpty()) {
            result.put("msg", account + "账号不存在......");
            return result;
        }

        // 账号存在，验证密码
        User user = users.get(0);
        String dbPwd = user.getUpwd();
        // 实际生产应使用加密比对（如 MD5），此处为明文比对（示例）
        if (!dbPwd.equals(userDto.getUpwd())) {
            result.put("msg", "输入密码错误......");
            return result;
        }
        //身份验证通过，记录登录信息
        UserLineDto userLineDto = new UserLineDto();
        userLineDto.setId(user.getId());
        userLineDto.setUname(user.getRealname());
        session.setAttribute("online", userLineDto);

        result.put("code", 200);
        return result;
    }

    @Override
    public Map<String, Object> updateUserPwdService(UserPwdDto pwdDto) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新用户密码失败......");

        // 1. 根据 id 查询原始用户
        Integer id = pwdDto.getId();
        User user = userMapper.selectById(id);
        if (user == null) {
            result.put("msg", "用户不存在");
            return result;
        }

        // 2. 验证原始密码是否正确
        if (!user.getUpwd().equals(pwdDto.getOldpwd())) {
            result.put("msg", "原始密码不正确......");
            return result;
        }

        // 3. 更新新密码
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setUpwd(pwdDto.getNewpwd());
        userMapper.updateById(updateUser);

        result.put("code", 200);
        result.put("msg", "更新用户密码成功，请重新登录......");
        return result;
    }
}




