package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Prepaid;
import com.zzyl.service.PrepaidService;
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
