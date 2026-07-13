package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Leave;
import com.zzyl.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired private LeaveService leaveService;
    @Autowired private HttpSession session;

    @PostMapping("/page")
    public Result<PageResult<Leave>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String leaveNo = (String) params.get("leaveNo");
        Integer elderlyId = params.get("elderlyId") != null ? Integer.valueOf(params.get("elderlyId").toString()) : null;
        Integer status = params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null;
        return Result.success(leaveService.findPage(pageNum, pageSize, leaveNo, elderlyId, status));
    }

    @PostMapping("/add") public Result<Void> add(@RequestBody Leave leave) { leaveService.addLeave(leave, (Long)session.getAttribute("userId")); return Result.success(); }
    @GetMapping("/{id}") public Result<Leave> getById(@PathVariable Long id) { return Result.success(leaveService.getById(id)); }
    @PostMapping("/returnBack") public Result<Void> returnBack(@RequestBody Map<String, Object> params) { leaveService.returnBack(Long.valueOf(params.get("id").toString()), (String)params.get("actualReturnTime"), (String)params.get("remark"), (Long)session.getAttribute("userId")); return Result.success(); }
}
