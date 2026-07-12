package com.zzyl.controller;

import com.zzyl.common.Result;
import com.soft.pojo.Dept;
import com.zzyl.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> tree(HttpServletRequest request) {
        String deptName = request.getParameter("deptName");
        String status = request.getParameter("status");
        return Result.success(deptService.getDeptTree(deptName, status));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Dept dept) {
        deptService.addDept(dept);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Dept dept) {
        deptService.updateDept(dept);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        deptService.updateStatus(id, status);
        return Result.success();
    }
}
