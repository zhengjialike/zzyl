package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.common.PageResult;
import com.soft.common.Result;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PositionService positionService;

    @PostMapping("/page")
    public Result<PageResult<User>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String name = (String) params.get("name"), email = (String) params.get("email");
        Object s = params.get("status"); String status = s != null ? s.toString() : null;
        Long deptId = params.get("deptId") != null ? Long.valueOf(params.get("deptId").toString()) : null;
        return Result.success(userService.findPage(pageNum, pageSize, name, email, status, deptId));
    }

    @PostMapping("/add") public Result<Void> add(@RequestBody Map<String, Object> params) {
        User u = new User(); u.setAccount((String)params.get("account")); u.setRealname((String)params.get("realName"));
        u.setEmail((String)params.get("email")); u.setPhone((String)params.get("phone")); u.setSex((String)params.get("gender"));
        u.setDeptId(params.get("deptId")!=null?Integer.valueOf(params.get("deptId").toString()):null);
        u.setPositionId(params.get("positionId")!=null?Integer.valueOf(params.get("positionId").toString()):null);
        u.setIslock(params.get("status")!=null?Integer.valueOf(params.get("status").toString()):0);
        Long[] rids = null; if(params.get("roleIds")!=null){ java.util.List<Integer> l=(java.util.List<Integer>)params.get("roleIds"); rids=l.stream().map(Long::valueOf).toArray(Long[]::new); }
        userService.addUser(u, rids); return Result.success();
    }

    @PostMapping("/update") public Result<Void> update(@RequestBody Map<String, Object> params) {
        User u = new User(); u.setId(Integer.valueOf(params.get("id").toString()));
        u.setAccount((String)params.get("account")); u.setRealname((String)params.get("realName"));
        u.setEmail((String)params.get("email")); u.setPhone((String)params.get("phone")); u.setSex((String)params.get("gender"));
        u.setDeptId(params.get("deptId")!=null?Integer.valueOf(params.get("deptId").toString()):null);
        u.setPositionId(params.get("positionId")!=null?Integer.valueOf(params.get("positionId").toString()):null);
        u.setIslock(params.get("status")!=null?Integer.valueOf(params.get("status").toString()):0);
        Long[] rids = null; if(params.get("roleIds")!=null){ java.util.List<Integer> l=(java.util.List<Integer>)params.get("roleIds"); rids=l.stream().map(Long::valueOf).toArray(Long[]::new); }
        userService.updateUser(u, rids); return Result.success();
    }

    @PostMapping("/updateStatus") public Result<Void> updateStatus(@RequestBody Map<String, Object> params) { userService.updateStatus(Long.valueOf(params.get("id").toString()), params.get("status")!=null?params.get("status").toString():null); return Result.success(); }
    @PostMapping("/resetPassword") public Result<Void> resetPassword(@RequestBody Map<String, Object> params) { userService.resetPassword(Long.valueOf(params.get("id").toString())); return Result.success(); }
    @GetMapping("/roleIds/{userId}") public Result<Long[]> getRoleIds(@PathVariable Long userId) { return Result.success(userService.getUserRoleIds(userId)); }


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
    @PostMapping("/pageList")
    public Map<String, Object> userPageList(@RequestBody Map<String, Object> params) {
        return userService.queryUserPageList(params);
    }

    @PostMapping("/nurseList")
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
    @GetMapping("/detail")
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