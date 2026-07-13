package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckOut;
import com.soft.pojo.Elderly;
import com.soft.pojo.Contract;

import java.util.List;
import java.util.Map;

public interface CheckOutService extends IService<CheckOut> {

    /** 查询退住单列表。 */
    Map<String, Object> pageList(CheckOutPageDto dto);

    /** 创建退住单并完成流程第一步。 */
    Map<String, Object> startApply(StepSubmitDto dto, String applicant);

    /** 查询在住且没有办理中退住单的老人。 */
    List<Elderly> queryEligibleElders(String keyword);

    /** 查询本次退住可选择解除的有效合同。 */
    List<Contract> queryActiveContracts(Integer checkOutId);

    /** 按数据库当前节点提交退住第 2～7 步。 */
    Map<String, Object> submitStep(StepSubmitDto dto, String operator);

    /** 查询退住详情并聚合老人、合同展示字段。 */
    CheckOut queryDetail(Integer id);

    /** 查询退住申请操作日志。 */
    List<ApplyLog> queryLogs(Integer id);

    /** 在退住审批通过前撤销申请。 */
    Map<String, Object> revoke(Integer id, String operator);
}
