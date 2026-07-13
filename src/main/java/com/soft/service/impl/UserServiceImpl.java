package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.mapper.UserMapper;
import com.soft.pojo.User;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> queryUserService(UserDto userDto, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "身份验证失败......");
        
        String account = userDto.getAccount();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        
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
        
        //身份验证通过，记录登录信息（包含完整字段）
        UserLineDto userLineDto = new UserLineDto();
        userLineDto.setId(user.getId());
        userLineDto.setUname(user.getRealname());
        userLineDto.setSex(user.getSex());
        userLineDto.setPhone(user.getPhone());
        userLineDto.setImage(user.getImage());
        userLineDto.setDeptId(user.getDeptId());
        userLineDto.setPositionId(user.getPositionId());
        userLineDto.setAccount(user.getAccount());
        userLineDto.setEmail(user.getEmail());
        
        session.setAttribute("online", userLineDto);

        result.put("code", 200);
        return result;
    }

    @Override
    public Map<String, Object> updateUserPwdService(UserPwdDto userDto) {
        Map<String, Object> result = new HashMap<>();
        
        // 根据 id 查询用户，获取原密码
        User user = this.getById(userDto.getId());
        if (user == null) {
            result.put("code", 400);
            result.put("msg", "用户不存在");
            return result;
        }
        
        // 2. 验证原始密码是否正确
        if (!user.getUpwd().equals(userDto.getOldpwd())) {
            result.put("msg", "原始密码不正确......");
            return result;
        }
        
        // 3. 更新新密码
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




