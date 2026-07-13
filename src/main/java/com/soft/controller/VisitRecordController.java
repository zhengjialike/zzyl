package com.soft.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.VisitQueryDto;
import com.soft.dto.VisitRegisterDto;
import com.soft.dto.VisitResponseDto;
import com.soft.service.VisitRecordService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/visit")
public class VisitRecordController {

    private final VisitRecordService visitRecordService;

    public VisitRecordController(VisitRecordService visitRecordService) {
        this.visitRecordService = visitRecordService;
    }

    @PostMapping("/list")
    public Map<String, Object> queryVisits(@RequestBody VisitQueryDto queryDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<VisitResponseDto> page = visitRecordService.queryVisits(queryDto);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", page.getRecords());
            result.put("total", page.getTotal());
            result.put("pageNum", page.getCurrent());
            result.put("pageSize", page.getSize());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> registerDirectVisit(@Valid @RequestBody VisitRegisterDto registerDto,
                                                    HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String creator = getCurrentUser(session);
            boolean success = visitRecordService.registerDirectVisit(registerDto, creator);
            if (success) {
                result.put("code", 200);
                result.put("msg", "来访登记成功");
            } else {
                result.put("code", 400);
                result.put("msg", "来访登记失败");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "来访登记失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/detail/{id}")
    public Map<String, Object> getVisitDetail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            var visit = visitRecordService.getVisitById(id);
            if (visit != null) {
                result.put("code", 200);
                result.put("msg", "查询成功");
                result.put("data", visit);
            } else {
                result.put("code", 400);
                result.put("msg", "来访记录不存在");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private String getCurrentUser(HttpSession session) {
        Object realName = session.getAttribute("realName");
        return realName != null ? realName.toString() : "系统";
    }
}
