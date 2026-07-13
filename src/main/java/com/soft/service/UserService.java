package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.common.PageResult;
import com.soft.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.UserDto;
import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_user】的数据库操作Service
* @createDate 2026-07-06 09:23:02
*/
public interface UserService extends IService<User> {

    PageResult<User> findPage(int pageNum, int pageSize, String name, String email, String status, Long deptId);
    void addUser(User user, Long[] roleIds);
    void updateUser(User user, Long[] roleIds);
    void updateStatus(Long id, String status);
    void resetPassword(Long id);
    Long[] getUserRoleIds(Long userId);
    Map<String, Object> updateUserPwdService(UserPwdDto userDto);
    Map<String, Object> queryUserPageList(Map<String, Object> params);
}
