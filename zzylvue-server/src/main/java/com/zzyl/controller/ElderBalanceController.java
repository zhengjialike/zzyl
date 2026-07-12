package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.ElderBalance;
import com.zzyl.service.ElderBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/elderBalance")
public class ElderBalanceController {

    @Autowired
    private ElderBalanceService elderBalanceService;

    @PostMapping("/page")
    public Result<PageResult<ElderBalance>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String elderName = (String) params.get("elderName");
        String bedNo = (String) params.get("bedNo");
        return Result.success(elderBalanceService.findPage(pageNum, pageSize, elderName, bedNo));
    }
}
