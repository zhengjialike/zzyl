package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Leave;
import com.zzyl.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private HttpSession session;

    @PostMapping("/page")
    public Result<PageResult<Leave>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String leaveNo = (String) params.get("leaveNo");
        String elderName = (String) params.get("elderName");
        String elderIdCard = (String) params.get("elderIdCard");
        String status = (String) params.get("status");
        return Result.success(leaveService.findPage(pageNum, pageSize, leaveNo, elderName, elderIdCard, status));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Leave leave) {
        Long userId = (Long) session.getAttribute("userId");
        leaveService.addLeave(leave, userId);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Leave> getById(@PathVariable Long id) {
        return Result.success(leaveService.getById(id));
    }

    @PostMapping("/returnBack")
    public Result<Void> returnBack(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String actualReturnTime = (String) params.get("actualReturnTime");
        String remark = (String) params.get("remark");
        Long userId = (Long) session.getAttribute("userId");
        leaveService.returnBack(id, actualReturnTime, remark, userId);
        return Result.success();
    }
}
