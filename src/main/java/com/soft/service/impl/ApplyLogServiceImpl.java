package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ApplyLogMapper;
import com.soft.pojo.ApplyLog;
import com.soft.service.ApplyLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplyLogServiceImpl extends ServiceImpl<ApplyLogMapper, ApplyLog> implements ApplyLogService {

    @Override
    public List<ApplyLog> queryByApply(String applyType, Integer applyId) {
        QueryWrapper<ApplyLog> wrapper = new QueryWrapper<>();
        wrapper.eq("apply_type", applyType).eq("apply_id", applyId).orderByAsc("create_time");
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void addLog(String applyType, Integer applyId, String billNo, String stepName, String operator, String role, String operation) {
        ApplyLog log = new ApplyLog();
        log.setApplyType(applyType);
        log.setApplyId(applyId);
        log.setBillNo(billNo);
        log.setStepName(stepName);
        log.setOperator(operator);
        log.setRole(role);
        log.setOperation(operation);
        log.setCreateTime(LocalDateTime.now());
        this.baseMapper.insert(log);
    }
}