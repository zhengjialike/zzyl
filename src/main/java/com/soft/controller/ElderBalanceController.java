package com.soft.controller;

import com.soft.common.PageResult;
import com.soft.common.Result;
import com.soft.entity.ElderBalance;
import com.soft.service.ElderBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/elderBalance")
public class ElderBalanceController {
    @Autowired private ElderBalanceService elderBalanceService;
    @PostMapping("/page")
    public Result<PageResult<ElderBalance>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        Integer elderlyId = params.get("elderlyId") != null ? Integer.valueOf(params.get("elderlyId").toString()) : null;
        String bedNo = (String) params.get("bedNo");
        return Result.success(elderBalanceService.findPage(pageNum, pageSize, elderlyId, bedNo));
    }
}
