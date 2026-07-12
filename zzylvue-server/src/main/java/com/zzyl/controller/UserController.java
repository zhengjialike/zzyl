package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.User;
import com.zzyl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/page")
    public Result<PageResult<User>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String name = (String) params.get("name");
        String email = (String) params.get("email");
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        Long deptId = params.get("deptId") != null ? Long.valueOf(params.get("deptId").toString()) : null;
        return Result.success(userService.findPage(pageNum, pageSize, name, email, status, deptId));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Map<String, Object> params) {
        User user = new User();
        user.setUsername((String) params.get("username"));
        user.setRealName((String) params.get("realName"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setGender((String) params.get("gender"));
        user.setDeptId(params.get("deptId") != null ? Long.valueOf(params.get("deptId").toString()) : null);
        user.setPositionId(params.get("positionId") != null ? Long.valueOf(params.get("positionId").toString()) : null);
        user.setStatus(params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null);
        Long[] roleIds = null;
        if (params.get("roleIds") != null) {
            java.util.List<Integer> list = (java.util.List<Integer>) params.get("roleIds");
            roleIds = list.stream().map(Long::valueOf).toArray(Long[]::new);
        }
        userService.addUser(user, roleIds);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Map<String, Object> params) {
        User user = new User();
        user.setId(Long.valueOf(params.get("id").toString()));
        user.setUsername((String) params.get("username"));
        user.setRealName((String) params.get("realName"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setGender((String) params.get("gender"));
        user.setDeptId(params.get("deptId") != null ? Long.valueOf(params.get("deptId").toString()) : null);
        user.setPositionId(params.get("positionId") != null ? Long.valueOf(params.get("positionId").toString()) : null);
        user.setStatus(params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null);
        Long[] roleIds = null;
        if (params.get("roleIds") != null) {
            java.util.List<Integer> list = (java.util.List<Integer>) params.get("roleIds");
            roleIds = list.stream().map(Long::valueOf).toArray(Long[]::new);
        }
        userService.updateUser(user, roleIds);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        userService.updateStatus(id, status);
        return Result.success();
    }

    @PostMapping("/resetPassword")
    public Result<Void> resetPassword(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        userService.resetPassword(id);
        return Result.success();
    }

    @GetMapping("/roleIds/{userId}")
    public Result<Long[]> getRoleIds(@PathVariable Long userId) {
        return Result.success(userService.getUserRoleIds(userId));
    }
}
