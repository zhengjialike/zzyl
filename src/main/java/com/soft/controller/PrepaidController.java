package com.soft.controller;

import com.soft.common.PageResult;
import com.soft.common.Result;
import com.soft.entity.Prepaid;
import com.soft.service.PrepaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/prepaid")
public class PrepaidController {
    @Autowired private PrepaidService prepaidService;
    @Autowired private HttpSession session;

    @PostMapping("/page")
    public Result<PageResult<Prepaid>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        Integer elderlyId = params.get("elderlyId") != null ? Integer.valueOf(params.get("elderlyId").toString()) : null;
        String bedNo = (String) params.get("bedNo");
        return Result.success(prepaidService.findPage(pageNum, pageSize, elderlyId, bedNo));
    }
    @PostMapping("/recharge") public Result<Void> recharge(@RequestBody Prepaid prepaid) { prepaidService.recharge(prepaid, (Long)session.getAttribute("userId")); return Result.success(); }
}
