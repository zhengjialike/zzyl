package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckOut;
import com.soft.pojo.Elder;
import com.soft.pojo.Contract;

import java.util.List;
import java.util.Map;

public interface CheckOutService extends IService<CheckOut> {

    Map<String, Object> pageList(CheckOutPageDto dto);

    Map<String, Object> startApply(StepSubmitDto dto, String applicant);

    List<Elder> queryEligibleElders(String keyword);

    List<Contract> queryActiveContracts(Integer checkOutId);

    Map<String, Object> submitStep(StepSubmitDto dto, String operator);

    CheckOut queryDetail(Integer id);

    List<ApplyLog> queryLogs(Integer id);

    Map<String, Object> revoke(Integer id, String operator);
}
