package com.soft.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.pojo.Elderly;
import com.soft.service.ElderlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ElderlyController {

    @Autowired
    private ElderlyService elderlyService;

    /**
     * 老人分页查询
     */
    @PostMapping("/elderlyPage")
    public Map<String, Object> elderlyPageList(@RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            int pageNum = params.getOrDefault("pageNum", 1);
            int pageSize = params.getOrDefault("pageSize", 10);
            
            Page<Elderly> page = new Page<>(pageNum, pageSize);
            IPage<Elderly> iPage = elderlyService.page(page);
            
            result.put("code", 200);
            result.put("total", iPage.getTotal());
            result.put("elderlies", iPage.getRecords());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据ID列表查询老人信息
     */
    @PostMapping("/elderlyListByIds")
    public Map<String, Object> getElderlyListByIds(@RequestBody List<Integer> ids) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Elderly> elderlyList = elderlyService.listByIds(ids);
            result.put("code", 200);
            result.put("data", elderlyList);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }
}