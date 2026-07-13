package com.soft.controller;

import com.soft.service.impl.NursingTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class NursingTaskController {

    @Autowired
    private NursingTaskServiceImpl nursingTaskService;

    @RequestMapping("/nursingTaskPage")
    public Map<String, Object> pageList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String taskNo,
            @RequestParam(required = false) String elderlyName,
            @RequestParam(required = false) Integer status) {
        return nursingTaskService.pageList(pageNum, pageSize, taskNo, elderlyName, status);
    }
}
