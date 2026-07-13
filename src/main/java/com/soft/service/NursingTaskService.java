package com.soft.service;

import com.soft.pojo.NursingTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_nursing_task(护理任务表)】的数据库操作Service
* @createDate 2026-07-13 09:53:47
*/
public interface NursingTaskService extends IService<NursingTask> {
    /**
     * 分页查询护理任务列表
     */
    Map<String, Object> queryTaskPageList(Map<String, Object> params);

    /**
     * 执行护理任务
     */
    Map<String, Object> executeTask(Integer taskId, String executionRecord, String executionImage, Integer executorId);

    /**
     * 取消护理任务
     */
    Map<String, Object> cancelTask(Integer taskId, String cancelReason, Integer cancelerId);

    /**
     * 查看任务详情
     */
    Map<String, Object> getTaskDetail(Integer taskId);

    /**
     * 生成护理任务（从订单和合同）
     */
    Map<String, Object> generateTasks();
}
