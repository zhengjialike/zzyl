package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckIn;

import java.util.List;
import java.util.Map;

public interface CheckInService extends IService<CheckIn> {

    Map<String, Object> pageList(CheckInPageDto dto);

    Map<String, Object> startApply(StepSubmitDto dto, String applicant);

    Map<String, Object> submitStep(StepSubmitDto dto, String operator);

    CheckIn queryDetail(Integer id);

    List<ApplyLog> queryLogs(Integer id);

    Map<String, Object> revoke(Integer id, String operator);
}