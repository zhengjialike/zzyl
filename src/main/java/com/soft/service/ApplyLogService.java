package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.ApplyLog;

import java.util.List;

public interface ApplyLogService extends IService<ApplyLog> {
    List<ApplyLog> queryByApply(String applyType, Integer applyId);
    void addLog(String applyType, Integer applyId, String billNo, String stepName, String operator, String role, String operation);
}