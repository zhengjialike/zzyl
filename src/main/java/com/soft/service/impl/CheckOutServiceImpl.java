package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.mapper.CheckOutMapper;
import com.soft.mapper.ContractMapper;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.Bill;
import com.soft.pojo.CheckOut;
import com.soft.pojo.Contract;
import com.soft.pojo.Elder;
import com.soft.service.ApplyLogService;
import com.soft.service.BillService;
import com.soft.service.CheckOutService;
import com.soft.service.ElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CheckOutServiceImpl extends ServiceImpl<CheckOutMapper, CheckOut> implements CheckOutService {

    private static final String[] STEP_NAMES = {"申请退住", "申请审批", "解除合同", "调整账单", "账单审批", "费用清算"};
    private static final String[] STEP_ROLES = {"发起人", "审批人", "操作人", "操作人", "审批人", "操作人"};
    private static final String[] STEP_OPS = {"已发起", "已审批", "已解除", "已调整", "已审批", "已结清"};

    @Autowired private CheckOutMapper checkOutMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private ApplyLogService applyLogService;
    @Autowired private BillService billService;
    @Autowired private ElderService elderService;

    @Override
    public Map<String, Object> pageList(CheckOutPageDto dto) {
        Page<CheckOut> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<CheckOut> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getBillNo())) wrapper.eq("bill_no", dto.getBillNo());
        if (StringUtils.hasText(dto.getElderName())) wrapper.like("elder_name", dto.getElderName());
        if (StringUtils.hasText(dto.getIdCard())) wrapper.eq("id_card", dto.getIdCard());
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            wrapper.between("check_out_date", dto.getStartDate(), dto.getEndDate());
        }
        wrapper.orderByDesc("create_time");
        Page<CheckOut> resultPage = checkOutMapper.selectPage(page, wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("list", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        return result;
    }

    @Override
    public List<Elder> queryEligibleElders(String keyword) {
        QueryWrapper<CheckOut> activeWrapper = new QueryWrapper<>();
        activeWrapper.select("elder_id").eq("flow_status", "申请中").isNotNull("elder_id");
        List<Integer> applyingIds = checkOutMapper.selectObjs(activeWrapper).stream()
                .map(value -> Integer.valueOf(value.toString()))
                .toList();
        QueryWrapper<Elder> elderWrapper = new QueryWrapper<>();
        elderWrapper.eq("status", 1);
        if (!applyingIds.isEmpty()) elderWrapper.notIn("id", applyingIds);
        if (StringUtils.hasText(keyword)) {
            elderWrapper.and(wrapper -> wrapper.like("name", keyword).or().eq("id_card", keyword));
        }
        elderWrapper.orderByAsc("name");
        return elderService.list(elderWrapper);
    }

    @Override
    public List<Contract> queryActiveContracts(Integer checkOutId) {
        CheckOut checkOut = checkOutMapper.selectById(checkOutId);
        if (checkOut == null || checkOut.getElderId() == null) return List.of();
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("elder_id", checkOut.getElderId())
                .in("status", "未生效", "生效中")
                .orderByDesc("create_time");
        return contractMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public Map<String, Object> startApply(StepSubmitDto dto, String applicant) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        Elder elder = dto.getElderId() == null ? elderService.queryByIdCard(dto.getIdCard()) : elderService.getById(dto.getElderId());
        if (elder == null) { result.put("msg", "请选择有效的在住老人"); return result; }
        if (!Integer.valueOf(1).equals(elder.getStatus())) { result.put("msg", "该老人当前不是在住状态，不能申请退住"); return result; }
        QueryWrapper<CheckOut> duplicateWrapper = new QueryWrapper<>();
        duplicateWrapper.eq("elder_id", elder.getId()).eq("flow_status", "申请中");
        if (checkOutMapper.selectCount(duplicateWrapper) > 0) { result.put("msg", "该老人已有正在办理的退住申请"); return result; }
        if (dto.getCheckOutDate() == null || !StringUtils.hasText(dto.getCheckoutReason())) {
            result.put("msg", "请填写退住日期和退住原因"); return result;
        }
        CheckOut co = new CheckOut();
        co.setBillNo(generateBillNo("TZ"));
        co.setElderId(elder.getId());
        co.setElderName(elder.getName());
        co.setIdCard(elder.getIdCard());
        co.setCheckOutDate(dto.getCheckOutDate());
        co.setReason(dto.getCheckoutReason());
        co.setRemark(dto.getRemark());
        co.setCurrentStep(2);
        co.setFlowStatus("申请中");
        co.setApplicant(applicant);
        checkOutMapper.insert(co);
        applyLogService.addLog("退住", co.getId(), co.getBillNo(), STEP_NAMES[0], applicant, STEP_ROLES[0], STEP_OPS[0]);
        result.put("code", 200);
        result.put("msg", "申请退住提交成功");
        result.put("id", co.getId());
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> submitStep(StepSubmitDto dto, String operator) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        CheckOut co = checkOutMapper.selectById(dto.getId());
        if (co == null) { result.put("msg", "单据不存在"); return result; }
        int step = dto.getStep();
        if (step < 2 || step > 6) { result.put("msg", "无效步骤"); return result; }
        if (!"申请中".equals(co.getFlowStatus())) { result.put("msg", "当前退住申请已结束，不能继续提交"); return result; }
        if (co.getCurrentStep() == null || step != co.getCurrentStep()) {
            result.put("msg", "流程节点已变化，请刷新页面后重试"); return result;
        }
        String operation = STEP_OPS[step - 1];
        switch (step) {
            case 2: // 申请审批
                if (!"通过".equals(dto.getApproveResult()) && !"驳回".equals(dto.getApproveResult())) {
                    result.put("msg", "请选择审批结果"); return result;
                }
                co.setApprover(operator);
                co.setApproveRemark(dto.getApproveRemark());
                co.setApproveResult(dto.getApproveResult());
                operation = "驳回".equals(dto.getApproveResult()) ? "已驳回" : "已通过";
                if ("驳回".equals(dto.getApproveResult())) co.setFlowStatus("已关闭");
                else co.setCurrentStep(3);
                break;
            case 3: // 解除合同
                if (dto.getTerminateDate() == null || !StringUtils.hasText(dto.getTerminateAgreement())) {
                    result.put("msg", "请填写解除日期并上传解除协议"); return result;
                }
                co.setTerminateDate(dto.getTerminateDate());
                co.setTerminateAgreement(dto.getTerminateAgreement());
                invalidateContract(co, dto.getContractId());
                co.setCurrentStep(4);
                break;
            case 4: // 调整账单
                if (dto.getBills() != null) {
                    dto.getBills().forEach(b -> {
                        b.setCheckOutId(co.getId());
                        if (b.getId() != null) billService.updateById(b);
                        else billService.save(b);
                    });
                }
                co.setCurrentStep(5);
                break;
            case 5: // 账单审批/退住审批
                if (!"通过".equals(dto.getApproveResult()) && !"驳回".equals(dto.getApproveResult())) {
                    result.put("msg", "请选择审批结果"); return result;
                }
                co.setApprover(operator);
                if (dto.getApproveRemark() != null) co.setApproveRemark(dto.getApproveRemark());
                co.setApproveResult(dto.getApproveResult());
                operation = "驳回".equals(dto.getApproveResult()) ? "已驳回" : "已通过";
                if ("驳回".equals(dto.getApproveResult())) co.setFlowStatus("已关闭");
                else co.setCurrentStep(6);
                break;
            case 6: // 费用清算
                co.setRefundWay(dto.getRefundMethod());
                co.setRefundRemark(dto.getRefundRemark());
                co.setRefundVoucher(dto.getRefundVoucher());
                co.setRefundAmount(dto.getRefundAmount());
                co.setSettleStatus("已结清");
                co.setSettlementAmount(dto.getRefundAmount());
                co.setFinishTime(LocalDateTime.now());
                co.setFlowStatus("已完成");
                co.setCurrentStep(6);
                if (co.getElderId() != null) {
                    Elder elder = elderService.getById(co.getElderId());
                    if (elder != null) {
                        elder.setStatus(2);
                        elderService.updateById(elder);
                    }
                }
                break;
            default:
                result.put("msg", "无效步骤"); return result;
        }
        checkOutMapper.updateById(co);
        applyLogService.addLog("退住", co.getId(), co.getBillNo(), STEP_NAMES[step-1], operator, STEP_ROLES[step-1], operation);
        result.put("code", 200);
        result.put("msg", STEP_NAMES[step-1] + "提交成功");
        result.put("currentStep", co.getCurrentStep());
        return result;
    }

    private void invalidateContract(CheckOut co, Integer contractId) {
        QueryWrapper<Contract> cw = new QueryWrapper<>();
        cw.eq("elder_id", co.getElderId()).in("status", "未生效", "生效中");
        if (contractId != null) cw.eq("id", contractId);
        cw.orderByDesc("create_time").last("LIMIT 1");
        Contract contract = contractMapper.selectOne(cw);
        if (contract != null) {
            contract.setStatus("已失效");
            contract.setCheckOutId(co.getId());
            contract.setInvalidTime(LocalDateTime.now());
            contractMapper.updateById(contract);
        } else {
            throw new IllegalStateException("未找到可解除的有效合同");
        }
    }

    @Override
    public CheckOut queryDetail(Integer id) { return checkOutMapper.selectById(id); }

    @Override
    public List<ApplyLog> queryLogs(Integer id) {
        CheckOut co = checkOutMapper.selectById(id);
        if (co == null) return List.of();
        return applyLogService.queryByApply("退住", id);
    }

    @Override
    @Transactional
    public Map<String, Object> revoke(Integer id, String operator) {
        Map<String, Object> result = new HashMap<>();
        CheckOut co = checkOutMapper.selectById(id);
        if (co == null) { result.put("code", 400); result.put("msg", "单据不存在"); return result; }
        if (!"申请中".equals(co.getFlowStatus()) || !Integer.valueOf(2).equals(co.getCurrentStep())) {
            result.put("code", 400); result.put("msg", "仅申请审批前可以撤销"); return result;
        }
        co.setFlowStatus("已关闭");
        checkOutMapper.updateById(co);
        applyLogService.addLog("退住", id, co.getBillNo(), "撤销申请", operator, "发起人", "已撤销");
        result.put("code", 200); result.put("msg", "撤销成功");
        return result;
    }

    private String generateBillNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
