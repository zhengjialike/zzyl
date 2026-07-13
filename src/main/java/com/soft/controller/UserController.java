package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 分页查询用户列表
     */
    @PostMapping("/user/pageList")
    public Map<String, Object> userPageList(@RequestBody Map<String, Object> params) {
        return userService.queryUserPageList(params);
    }

    /**
     * 查询护理员列表（根据职位名称过滤）
     */
    @PostMapping("/user/nurseList")
    public Map<String, Object> nurseList(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) params.getOrDefault("pageSize", 100);
            
            // 查询职位名称包含"护理"的position ID列表
            List<Position> positions = positionService.list().stream()
                .filter(p -> p.getPositionName() != null && p.getPositionName().contains("护理"))
                .collect(Collectors.toList());
            
            List<Integer> nursePositionIds = positions.stream()
                .map(Position::getId)
                .collect(Collectors.toList());
            
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            if (!nursePositionIds.isEmpty()) {
                wrapper.in("position_id", nursePositionIds);
            } else {
                // 如果职位表没有数据，返回空列表
                result.put("code", 200);
                result.put("users", List.of());
                result.put("total", 0);
                return result;
            }
            
            Page<User> page = new Page<>(pageNum, pageSize);
            IPage<User> iPage = userService.page(page, wrapper);
            
            result.put("code", 200);
            result.put("users", iPage.getRecords());
            result.put("total", iPage.getTotal());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/user/detail")
    public Map<String, Object> userDetail(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = userService.getById(id);
            if (user == null) {
                result.put("code", 400);
                result.put("msg", "用户不存在");
                return result;
            }
            
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("user", user);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
}