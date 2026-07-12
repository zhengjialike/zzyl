package com.soft.controller;

import com.soft.dto.UserLineDto;
import com.soft.dto.UserPwdDto;
import com.soft.mapper.RoleMapper;
import com.soft.pojo.Dept;
import com.soft.pojo.Position;
import com.soft.pojo.Role;
import com.soft.pojo.User;
import com.soft.service.DeptService;
import com.soft.service.PositionService;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DeptService deptService;
    
    @Autowired
    private PositionService positionService;
    
    @Autowired
    private RoleMapper roleMapper;
    
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
     * 加载当前登录用户个人信息（包含部门、职位、角色名称）
     * @param session HttpSession
     * @return UserLineDto 对象
     */
    @RequestMapping("/showInfo")
    public UserLineDto showUserInfo(HttpSession session) {
        Object object = session.getAttribute("online");
        if (object != null) {
            UserLineDto dto = (UserLineDto) object;
            Integer id = dto.getId();
            
            // 查询用户基本信息
            User user = userService.getById(id);
            if (user == null) {
                return null;
            }
            
            // 填充基本信息（从数据库重新获取最新数据）
            dto.setUname(user.getRealname());
            dto.setSex(user.getSex());
            dto.setPhone(user.getPhone());
            dto.setImage(user.getImage());
            dto.setDeptId(user.getDeptId());
            dto.setPositionId(user.getPositionId());
            dto.setAccount(user.getAccount());
            dto.setEmail(user.getEmail());
            
            // 查询部门名称
            if (user.getDeptId() != null) {
                Dept dept = deptService.getById(user.getDeptId());
                if (dept != null) {
                    dto.setDeptName(dept.getDeptName());
                } else {
                    dto.setDeptName("-");
                }
            } else {
                dto.setDeptName("-");
            }
            
            // 查询职位名称
            if (user.getPositionId() != null) {
                Position position = positionService.getById(user.getPositionId());
                if (position != null) {
                    dto.setPositionName(position.getPositionName());
                } else {
                    dto.setPositionName("-");
                }
            } else {
                dto.setPositionName("-");
            }
            
            // 查询角色名称（可能有多个角色，取第一个）
            List<Role> roles = roleMapper.selectRolesByUserId(id);
            if (roles != null && !roles.isEmpty()) {
                // 如果有多个角色，用逗号分隔
                StringBuilder roleNames = new StringBuilder();
                for (int i = 0; i < roles.size(); i++) {
                    if (i > 0) {
                        roleNames.append(", ");
                    }
                    roleNames.append(roles.get(i).getRoleName());
                }
                dto.setRoleName(roleNames.toString());
            } else {
                dto.setRoleName(""); // 没有角色时返回空字符串
            }
            
            return dto;
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