package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Leave;
import com.zzyl.mapper.LeaveMapper;
import com.zzyl.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {

    @Override
    public PageResult<Leave> findPage(int pageNum, int pageSize, String leaveNo, String elderName, String elderIdCard, String status) {
        Page<Leave> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Leave> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(leaveNo)) {
            wrapper.eq(Leave::getLeaveNo, leaveNo);
        }
        if (StringUtils.hasText(elderName)) {
            wrapper.like(Leave::getElderName, elderName);
        }
        if (StringUtils.hasText(elderIdCard)) {
            wrapper.eq(Leave::getElderIdCard, elderIdCard);
        }
        if (StringUtils.hasText(status) && !"全部".equals(status)) {
            wrapper.eq(Leave::getStatus, status);
        }
        wrapper.orderByDesc(Leave::getCreateTime);
        IPage<Leave> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    public void addLeave(Leave leave, Long userId) {
        String leaveNo = "QJ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        leave.setLeaveNo(leaveNo);
        leave.setStatus(1);
        leave.setApplicantId(userId);
        // Calculate leave days
        if (leave.getLeaveStartTime() != null && leave.getExpectedReturnTime() != null) {
            double hours = ChronoUnit.MINUTES.between(leave.getLeaveStartTime(), leave.getExpectedReturnTime()) / 60.0;
            double days = hours / 24.0;
            if (days < 0.5) days = 0.5;
            else if (days < 1) days = 1;
            else days = Math.round(days * 10.0) / 10.0;
            leave.setLeaveDays(days);
        }
        baseMapper.insert(leave);
    }

    @Override
    public void returnBack(Long id, String actualReturnTimeStr, String remark, Long userId) {
        Leave leave = baseMapper.selectById(id);
        if (leave == null) return;
        LocalDateTime actualReturnTime = LocalDateTime.parse(actualReturnTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        leave.setActualReturnTime(actualReturnTime);
        leave.setRemark(remark);
        leave.setStatus(2);
        // Calculate actual leave days
        if (leave.getLeaveStartTime() != null) {
            double hours = ChronoUnit.MINUTES.between(leave.getLeaveStartTime(), actualReturnTime) / 60.0;
            double days = hours / 24.0;
            if (days < 0.5) days = 0.5;
            else if (days < 1) days = 1;
            else days = Math.round(days * 10.0) / 10.0;
            leave.setActualLeaveDays(days);
        }
        baseMapper.updateById(leave);
    }
}
