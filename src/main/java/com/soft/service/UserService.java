package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface UserService extends IService<User> {
    
    Map<String, Object> updateUserPwdService(UserPwdDto userDto);

    Map<String, Object> queryUserService(UserDto userDto, HttpSession session);
    
    /**
     * 分页查询用户列表
     */
    Map<String, Object> queryUserPageList(Map<String, Object> params);
}
