package com.zzyl.controller;

import com.zzyl.common.Result;
import com.zzyl.entity.Role;
import com.zzyl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Result<List<Role>> list(@RequestParam(required = false) String roleName) {
        return Result.success(roleService.findAll(roleName));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        roleService.updateStatus(id, status);
        return Result.success();
    }

    @PostMapping("/saveMenuPerms")
    public Result<Void> saveMenuPerms(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("roleId").toString());
        List<Long> menuIds = null;
        if (params.get("menuIds") != null) {
            menuIds = ((List<Integer>) params.get("menuIds")).stream().map(Long::valueOf).toList();
        }
        roleService.saveMenuPerms(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/menuIds/{roleId}")
    public Result<List<Long>> getMenuIds(@PathVariable Long roleId) {
        return Result.success(roleService.getMenuIds(roleId));
    }

    @PostMapping("/saveDataScope")
    public Result<Void> saveDataScope(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("roleId").toString());
        String dataScope = (String) params.get("dataScope");
        String deptIds = (String) params.get("deptIds");
        roleService.saveDataScope(roleId, dataScope, deptIds);
        return Result.success();
    }

    @GetMapping("/dataScope/{roleId}")
    public Result<Map<String, Object>> getDataScope(@PathVariable Long roleId) {
        return Result.success(roleService.getDataScope(roleId));
    }
}
