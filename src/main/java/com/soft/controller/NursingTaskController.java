package com.soft.controller;

import com.soft.service.NursingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/nursingTask")
public class NursingTaskController {

    @Autowired
    private NursingTaskService nursingTaskService;

    /**
     * 分页查询护理任务列表
     */
    @PostMapping("/pageList")
    public Map<String, Object> pageList(@RequestBody Map<String, Object> params) {
        return nursingTaskService.queryTaskPageList(params);
    }

    /**
     * 执行护理任务
     */
    @PostMapping("/execute")
    public Map<String, Object> execute(@RequestBody Map<String, Object> params) {
        Integer taskId = (Integer) params.get("taskId");
        String executionRecord = (String) params.get("executionRecord");
        String executionImage = (String) params.get("executionImage");
        Integer executorId = (Integer) params.get("executorId");
        return nursingTaskService.executeTask(taskId, executionRecord, executionImage, executorId);
    }

    /**
     * 取消护理任务
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestBody Map<String, Object> params) {
        Integer taskId = (Integer) params.get("taskId");
        String cancelReason = (String) params.get("cancelReason");
        Integer cancelerId = (Integer) params.get("cancelerId");
        return nursingTaskService.cancelTask(taskId, cancelReason, cancelerId);
    }

    /**
     * 查看任务详情
     */
    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam Integer taskId) {
        return nursingTaskService.getTaskDetail(taskId);
    }

    /**
     * 生成护理任务（从订单和合同）
     */
    @PostMapping("/generate")
    public Map<String, Object> generate() {
        return nursingTaskService.generateTasks();
    }
}
