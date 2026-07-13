package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.mapper.CheckOutMapper;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.ContractMapper;
import com.soft.mapper.BedMapper;
import com.soft.pojo.*;
import com.soft.service.*;
import com.soft.service.BillService;
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
/**
 * 退住七步流程业务实现。
 *
 * <p>退住会跨越老人档案、入住快照、合同、账单和床位。合同在第 3 步只建立临时关联，
 * 第 6 步退住审批通过后才失效；老人状态和床位则到第 7 步费用清算完成后才更新。</p>
 */
public class CheckOutServiceImpl extends ServiceImpl<CheckOutMapper, CheckOut> implements CheckOutService {

    /** 原型定义的退住流程是七步，账单审批与退住审批是两个独立节点。 */
    private static final String[] STEP_NAMES = {"申请退住", "申请审批", "解除合同", "调整账单", "账单审批", "退住审批", "费用清算"};
    private static final String[] STEP_ROLES = {"发起人", "审批人", "操作人", "操作人", "审批人", "审批人", "操作人"};
    private static final String[] STEP_OPS = {"已发起", "已审批", "已提交协议", "已调整", "已审批", "已审批", "已结清"};

    @Autowired private CheckOutMapper checkOutMapper;
    @Autowired private CheckInMapper checkInMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private BedMapper bedMapper;
    @Autowired private ApplyLogService applyLogService;
    @Autowired private BillService billService;
    @Autowired private ElderlyService elderlyService;

    @Override
    /** 按查询条件分页返回退住单，默认按创建时间倒序。 */
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
    /**
     * 只返回在住老人，并排除已经存在“申请中”退住单的老人，防止同一老人并行办理两张退住单。
     */
    public List<Elderly> queryEligibleElders(String keyword) {
        QueryWrapper<CheckOut> activeWrapper = new QueryWrapper<>();
        activeWrapper.select("elder_id").eq("flow_status", "申请中").isNotNull("elder_id");
        List<Integer> applyingIds = checkOutMapper.selectObjs(activeWrapper).stream()
                .map(value -> Integer.valueOf(value.toString()))
                .toList();
        QueryWrapper<Elderly> elderWrapper = new QueryWrapper<>();
        elderWrapper.eq("status", 1);
        if (!applyingIds.isEmpty()) elderWrapper.notIn("id", applyingIds);
        if (StringUtils.hasText(keyword)) {
            elderWrapper.and(wrapper -> wrapper.like("real_name", keyword).or().eq("id_card", keyword));
        }
        elderWrapper.orderByAsc("real_name");
        return elderlyService.list(elderWrapper);
    }

    @Override
    /** 从退住单反查老人，只暴露该老人未生效或生效中的合同。 */
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
    /**
     * 发起退住申请。床位、护理等级和账单期限均从最近完成的入住单复制，
     * 不采用请求体中的同名字段，避免客户端伪造关键业务快照。
     */
    public Map<String, Object> startApply(StepSubmitDto dto, String applicant) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        Elderly elderly = dto.getElderId() == null ? elderlyService.queryByIdCard(dto.getIdCard()) : elderlyService.getById(dto.getElderId());
        if (elderly == null) { result.put("msg", "请选择有效的在住老人"); return result; }
        if (!Integer.valueOf(1).equals(elderly.getStatus())) { result.put("msg", "该老人当前不是在住状态，不能申请退住"); return result; }
        QueryWrapper<CheckOut> duplicateWrapper = new QueryWrapper<>();
        duplicateWrapper.eq("elder_id", elderly.getId()).eq("flow_status", "申请中");
        if (checkOutMapper.selectCount(duplicateWrapper) > 0) { result.put("msg", "该老人已有正在办理的退住申请"); return result; }
        if (dto.getCheckOutDate() == null || !StringUtils.hasText(dto.getCheckoutReason())) {
            result.put("msg", "请填写退住日期和退住原因"); return result;
        }

        // 退住基本信息以后端最近一张已完成入住单为准，避免前端伪造床位、护理等级和费用期限。
        QueryWrapper<CheckIn> checkInWrapper = new QueryWrapper<>();
        checkInWrapper.eq("elder_id", elderly.getId()).eq("flow_status", "已完成")
                .orderByDesc("finish_time").last("LIMIT 1");
        CheckIn latestCheckIn = checkInMapper.selectOne(checkInWrapper);
        if (latestCheckIn == null) { result.put("msg", "未找到该老人的有效入住记录"); return result; }
        if (latestCheckIn.getFeeStartDate() != null && latestCheckIn.getFeeEndDate() != null
                && (dto.getCheckOutDate().isBefore(latestCheckIn.getFeeStartDate())
                || dto.getCheckOutDate().isAfter(latestCheckIn.getFeeEndDate()))) {
            result.put("msg", "请在费用期限内发起退住申请"); return result;
        }
        CheckOut co = new CheckOut();
        co.setBillNo(generateBillNo("TZ"));
        co.setElderId(elderly.getId());
        co.setElderName(elderly.getRealName());
        co.setIdCard(elderly.getIdCard());
        co.setCheckOutDate(dto.getCheckOutDate());
        co.setReason(dto.getCheckoutReason());
        co.setRemark(dto.getRemark());
        co.setNursingLevel(latestCheckIn.getNursingLevel());
        co.setBedNo(latestCheckIn.getBedNo());
        co.setAdvisor(latestCheckIn.getAdvisor());
        co.setBillStartDate(latestCheckIn.getFeeStartDate());
        co.setBillEndDate(latestCheckIn.getFeeEndDate());
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
    /**
     * 提交退住第 2～7 步。每个 case 只处理当前节点允许修改的数据，
     * 成功后更新 currentStep 并写入统一流程日志。
     */
    public Map<String, Object> submitStep(StepSubmitDto dto, String operator) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        CheckOut co = checkOutMapper.selectById(dto.getId());
        if (co == null) { result.put("msg", "单据不存在"); return result; }
        int step = dto.getStep();
        if (step < 2 || step > 7) { result.put("msg", "无效步骤"); return result; }
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
                // 此节点只保存解除协议并关联合同。原型要求合同在退住审批通过前保持原状态。
                linkContractForCheckout(co, dto.getContractId());
                co.setCurrentStep(4);
                break;
            case 4: // 调整账单
                if (dto.getBills() != null) {
                    dto.getBills().forEach(b -> {
                        // 账单只能属于当前老人，禁止通过请求体修改其他老人的账单。
                        b.setElderlyId(co.getElderId());
                        if (b.getId() != null) {
                            Bill original = billService.getById(b.getId());
                            if (original != null && co.getElderId().equals(original.getElderlyId())) {
                                billService.updateById(b);
                            }
                        }
                    });
                }
                co.setCurrentStep(5);
                break;
            case 5: // 账单审批
                // 驳回后流程关闭，同时清除第 3 步建立的合同临时关联。
                if (!"通过".equals(dto.getApproveResult()) && !"驳回".equals(dto.getApproveResult())) {
                    result.put("msg", "请选择审批结果"); return result;
                }
                operation = "驳回".equals(dto.getApproveResult()) ? "已驳回" : "已通过";
                if ("驳回".equals(dto.getApproveResult())) {
                    co.setFlowStatus("已关闭");
                    unlinkPendingContract(co.getId());
                }
                else co.setCurrentStep(6);
                break;
            case 6: // 退住审批
                if (!"通过".equals(dto.getApproveResult()) && !"驳回".equals(dto.getApproveResult())) {
                    result.put("msg", "请选择审批结果"); return result;
                }
                co.setApprover(operator);
                co.setApproveRemark(dto.getApproveRemark());
                co.setApproveResult(dto.getApproveResult());
                operation = "驳回".equals(dto.getApproveResult()) ? "已驳回" : "已通过";
                if ("驳回".equals(dto.getApproveResult())) {
                    co.setFlowStatus("已关闭");
                    unlinkPendingContract(co.getId());
                } else {
                    // 只有退住审批通过后合同才变为已失效，解除记录才出现在合同详情中。
                    invalidateLinkedContract(co);
                    co.setCurrentStep(7);
                }
                break;
            case 7: // 费用清算
                // 清算前重新查未缴账单，不能只依赖页面进入该步骤时加载的旧数据。
                List<Bill> unpaidBills = billService.queryByElderId(co.getElderId()).stream()
                        .filter(bill -> Integer.valueOf(0).equals(bill.getStatus())).toList();
                if (!unpaidBills.isEmpty()) { result.put("msg", "存在欠费账单，请完成缴费后再清算"); return result; }
                if (dto.getRefundAmount() != null && dto.getRefundAmount().signum() > 0
                        && (!StringUtils.hasText(dto.getRefundMethod()) || !StringUtils.hasText(dto.getRefundVoucher()))) {
                    result.put("msg", "退款时必须选择退款方式并上传退款凭证"); return result;
                }
                co.setRefundWay(dto.getRefundMethod());
                co.setRefundRemark(dto.getRefundRemark());
                co.setRefundVoucher(dto.getRefundVoucher());
                co.setRefundAmount(dto.getRefundAmount());
                co.setSettleStatus("已结清");
                co.setSettlementAmount(dto.getRefundAmount());
                co.setFinishTime(LocalDateTime.now());
                co.setFlowStatus("已完成");
                co.setCurrentStep(7);
                if (co.getElderId() != null) {
                    Elderly elderly = elderlyService.getById(co.getElderId());
                    if (elderly != null) {
                        elderly.setStatus(2);
                        elderlyService.updateById(elderly);
                    }
                    // 退住完成后释放老人占用的床位，供新的入住申请选择。
                    UpdateWrapper<Bed> releaseBed = new UpdateWrapper<>();
                    releaseBed.eq("elderly_id", co.getElderId())
                            .set("elderly_id", null)
                            .set("status", 0);
                    bedMapper.update(null, releaseBed);
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

    /**
     * 在“解除合同”节点记录本次退住选择的合同，但不提前改变合同状态。
     * t_contract.check_out_id 充当退住单与合同之间的关联字段。
     */
    private void linkContractForCheckout(CheckOut co, Integer contractId) {
        QueryWrapper<Contract> cw = new QueryWrapper<>();
        cw.eq("elder_id", co.getElderId()).in("status", "未生效", "生效中");
        if (contractId != null) cw.eq("id", contractId);
        cw.orderByDesc("create_time").last("LIMIT 1");
        Contract contract = contractMapper.selectOne(cw);
        if (contract != null) {
            contract.setCheckOutId(co.getId());
            contractMapper.updateById(contract);
        } else {
            throw new IllegalStateException("未找到可解除的有效合同");
        }
    }

    /** 退住审批通过后，才正式将此前关联的合同置为已失效。 */
    private void invalidateLinkedContract(CheckOut co) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("check_out_id", co.getId()).last("LIMIT 1");
        Contract contract = contractMapper.selectOne(wrapper);
        if (contract == null) throw new IllegalStateException("未找到本次退住关联的合同");
        contract.setStatus("已失效");
        contract.setInvalidTime(LocalDateTime.now());
        contractMapper.updateById(contract);
    }

    /** 审批驳回或用户撤销时，清除尚未生效的退住关联，合同保持原状态。 */
    private void unlinkPendingContract(Integer checkOutId) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("check_out_id", checkOutId).ne("status", "已失效");
        for (Contract contract : contractMapper.selectList(wrapper)) {
            contract.setCheckOutId(null);
            contract.setInvalidTime(null);
            contractMapper.updateById(contract);
        }
    }

    @Override
    /** 聚合退住主表、老人联系电话以及本次关联的合同编号。 */
    public CheckOut queryDetail(Integer id) {
        CheckOut detail = checkOutMapper.selectById(id);
        if (detail == null) return null;
        if (detail.getElderId() != null) {
            Elderly elderly = elderlyService.getById(detail.getElderId());
            if (elderly != null) detail.setPhone(elderly.getPhone());
        }
        QueryWrapper<Contract> contractWrapper = new QueryWrapper<>();
        contractWrapper.eq("check_out_id", id).last("LIMIT 1");
        Contract contract = contractMapper.selectOne(contractWrapper);
        if (contract != null) {
            detail.setContractId(contract.getId());
            detail.setContractNo(contract.getContractNo());
        }
        return detail;
    }

    @Override
    /** 单据不存在时返回空日志列表。 */
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
        // 原型规则：退住审批通过前均可撤销；退住审批通过后撤销按钮必须置灰。
        if (!"申请中".equals(co.getFlowStatus()) || co.getCurrentStep() == null || co.getCurrentStep() > 6) {
            result.put("code", 400); result.put("msg", "退住审批通过后不能撤销"); return result;
        }
        // 撤销时解除临时合同关联，合同状态维持原状，不展示解除记录。
        unlinkPendingContract(id);
        co.setFlowStatus("已关闭");
        checkOutMapper.updateById(co);
        applyLogService.addLog("退住", id, co.getBillNo(), "撤销申请", operator, "发起人", "已撤销");
        result.put("code", 200); result.put("msg", "撤销成功");
        return result;
    }

    /** 生成 TZ 前缀退住单号。 */
    private String generateBillNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ThreadLocalRandom.current().nextInt(1000, 9999);
    }
}
