package com.soft.service;

import com.soft.dto.UserDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_user】的数据库操作Service
* @createDate 2026-07-06 09:23:02
*/
public interface UserService extends IService<User> {
    /**
     * 身份认证
     */
    public Map<String,Object> queryUserService(UserDto userDto, HttpSession session);

    /**
     * 更新用户密码
     * @param pwdDto 包含 id、oldpwd、newpwd 的 DTO
     * @return 操作结果（code, msg）
     */
    Map<String, Object> updateUserPwdService(UserPwdDto pwdDto);
}
