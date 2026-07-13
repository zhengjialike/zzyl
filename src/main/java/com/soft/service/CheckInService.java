package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.Bed;
import com.soft.pojo.CheckIn;

import java.util.List;
import java.util.Map;

public interface CheckInService extends IService<CheckIn> {

    /** 查询入住单列表。 */
    Map<String, Object> pageList(CheckInPageDto dto);

    /** 创建/更新老人档案、创建入住单并完成流程第一步。 */
    Map<String, Object> startApply(StepSubmitDto dto, String applicant);

    /** 按数据库当前节点提交入住第 2～5 步。 */
    Map<String, Object> submitStep(StepSubmitDto dto, String operator);

    /** 查询入住主表并回填预生成的合同编号。 */
    CheckIn queryDetail(Integer id);

    /** 查询入住申请操作日志。 */
    List<ApplyLog> queryLogs(Integer id);

    /** 查询未被老人占用的床位。 */
    List<Bed> queryAvailableBeds();

    /** 撤销办理中的入住申请并清理床位和临时合同。 */
    Map<String, Object> revoke(Integer id, String operator);
}
