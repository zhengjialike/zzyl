package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.AiDto;
import com.soft.entity.AiAssistant;
import com.soft.mapper.AiAssistantMapper;
import com.soft.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AiController {

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private AiAssistantMapper aiAssistantMapper;

    // AI 心理咨询助手
    @RequestMapping("/getPsychology")
    public String getPsychologyResult(@RequestBody AiDto dto) {
        return deepSeekService.chat(dto.getInputmsg());
    }

    @RequestMapping("/savePsy")
    public Map<String, Object> savePsyResult(@RequestBody AiDto dto) {
        return deepSeekService.savePsychology(dto);
    }

    // 咨询记录分页查询
    @RequestMapping("/psyRecord/page")
    public Map<String, Object> psyRecordPage(@RequestBody Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

        Page<AiAssistant> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AiAssistant> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createtime");

        Page<AiAssistant> result = aiAssistantMapper.selectPage(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pageNum", result.getCurrent());
        data.put("pageSize", result.getSize());
        return data;
    }
}