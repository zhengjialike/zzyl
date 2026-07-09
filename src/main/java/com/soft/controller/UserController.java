package com.soft.controller;

import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.pojo.User;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 加载用户登录信息接口
     */
    @RequestMapping("/loadInfo")
    public UserLineDto loadLoginInfo(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online != null) {
            return (UserLineDto) online;
        }
        return null;
    }

    /**
     * 加载当前登录用户个人信息
     * @param session HttpSession
     * @return User 对象
     */
    @RequestMapping("/showInfo")
    public User showUserInfo(HttpSession session) {
        Object object = session.getAttribute("online");
        if (object != null) {
            UserLineDto dto = (UserLineDto) object;
            Integer id = dto.getId();
            // 查询用户信息
            return userService.getById(id);
        }
        return null;
    }

    /**
     * 更新用户信息
     * @param userDto 前端传递的用户数据（JSON格式）
     * @return 包含操作结果（code、msg）的Map
     */
    @RequestMapping("/updateUser")
    public Map<String, Object> updateUser(@RequestBody UserLineDto userDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 将 DTO 属性复制到 User 实体
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            // 如果 DTO 中的 uname 对应 User 的 realname，则单独设置
            user.setRealname(userDto.getUname());

            // 调用 Service 更新数据库
            userService.updateById(user);

            result.put("code", 200);
            result.put("msg", "更新用户信息成功......");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新用户信息失败......");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新用户密码
     * @param userDto 前端传递的密码信息（oldpwd, newpwd）
     * @param session 当前会话
     * @return 操作结果
     */
    @RequestMapping("/updatePwd")
    public Map<String, Object> updateUserPwd(@RequestBody UserPwdDto userDto, HttpSession session) {
        // 从 session 中获取当前登录用户 id
        Object object = session.getAttribute("online");
        if (object != null) {
            UserLineDto dto = (UserLineDto) object;
            Integer id = dto.getId();
            userDto.setId(id); // 设置用户 id
        }
        // 调用 service 更新密码
        return userService.updateUserPwdService(userDto);
    }
}