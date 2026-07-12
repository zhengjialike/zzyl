package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Position;
import com.zzyl.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @PostMapping("/page")
    public Result<PageResult<Position>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String positionName = (String) params.get("positionName");
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        Long deptId = params.get("deptId") != null ? Long.valueOf(params.get("deptId").toString()) : null;
        return Result.success(positionService.findPage(pageNum, pageSize, positionName, status, deptId));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Position position) {
        positionService.addPosition(position);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Position position) {
        positionService.updatePosition(position);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        positionService.updateStatus(id, status);
        return Result.success();
    }
}
