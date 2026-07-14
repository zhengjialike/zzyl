package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.entity.InoutRecord;
import com.soft.service.InoutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InoutRecordController {

    @Autowired
    private InoutRecordService inoutRecordService;

    @RequestMapping("/queryInList")
    public List<InoutRecord> queryInList() {
        QueryWrapper<InoutRecord> wrapper = new QueryWrapper<>();
        wrapper.select("id", "oldname");
        wrapper.eq("status", "入住");
        return inoutRecordService.list(wrapper);
    }
}
