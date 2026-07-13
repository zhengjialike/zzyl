package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.soft.pojo.User;
import com.zzyl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired private UserService userService;

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
}
