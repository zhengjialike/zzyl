package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.ContractMapper;
import com.soft.pojo.*;
import com.soft.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {

    private static final String[] STEP_NAMES = {"申请入住", "入住评估", "入住审批", "入住配置", "签约办理"};
    private static final String[] STEP_ROLES = {"发起人", "操作人", "审批人", "操作人", "操作人"};
    private static final String[] STEP_OPS = {"已发起", "已评估", "已审批", "已配置", "已签约"};

    @Autowired private CheckInMapper checkInMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private ApplyLogService applyLogService;
    @Autowired private FamilyMemberService familyMemberService;
    @Autowired private ElderlyService elderlyService;
    @Autowired private BedService bedService;

    @Override
    public Map<String, Object> pageList(CheckInPageDto dto) {
        Page<CheckIn> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<CheckIn> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getBillNo())) wrapper.eq("bill_no", dto.getBillNo());
        if (StringUtils.hasText(dto.getElderName())) wrapper.like("elder_name", dto.getElderName());
        if (StringUtils.hasText(dto.getIdCard())) wrapper.eq("id_card", dto.getIdCard());
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            wrapper.between("start_date", dto.getStartDate(), dto.getEndDate());
        }
        wrapper.orderByDesc("create_time");
        Page<CheckIn> resultPage = checkInMapper.selectPage(page, wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("list", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> startApply(StepSubmitDto dto, String applicant) {
        Map<String, Object> result = new HashMap<>();

        // 1. 按身份证号查 t_elderly,不存在才新增
        Elderly elderly = elderlyService.queryByIdCard(dto.getIdCard());
        if (elderly == null) {
            elderly = new Elderly();
            elderly.setRealName(dto.getElderName());
            elderly.setIdCard(dto.getIdCard());
            elderly.setGender("男".equals(dto.getGender()) ? 1 : ("女".equals(dto.getGender()) ? 2 : 0));
            elderly.setBirthday(dto.getBirthday());
            elderly.setPhone(dto.getPhone());
            elderly.setAddress(dto.getAddress());
            elderly.setStatus(0);
            elderlyService.save(elderly);
        }

        // 2. 创建入住申请,回填 elder_id
        CheckIn checkIn = new CheckIn();
        copyWithSerialize(dto, checkIn);
        checkIn.setElderId(elderly.getId());
        checkIn.setBillNo(generateBillNo("RZ"));
        checkIn.setCurrentStep(2);
        checkIn.setFlowStatus("申请中");
        checkIn.setApplicant(applicant);
        checkInMapper.insert(checkIn);

        if (dto.getFamilyMembers() != null) {
            familyMemberService.saveByCheckInId(checkIn.getId(), dto.getFamilyMembers());
        }
        applyLogService.addLog("入住", checkIn.getId(), checkIn.getBillNo(), STEP_NAMES[0], applicant, STEP_ROLES[0], STEP_OPS[0]);

        result.put("code", 200);
        result.put("msg", "申请入住提交成功");
        result.put("id", checkIn.getId());
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> submitStep(StepSubmitDto dto, String operator) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        CheckIn checkIn = checkInMapper.selectById(dto.getId());
        if (checkIn == null) { result.put("msg", "单据不存在"); return result; }
        int step = dto.getStep();
        switch (step) {
            case 2: // 入住评估 (健康+能力+报告)
                copyWithSerialize(dto, checkIn);
                break;
            case 3: // 入住审批
                checkIn.setApprover(dto.getApprover());
                checkIn.setApproveOpinion(dto.getApproveOpinion());
                checkIn.setApproveResult(dto.getApproveResult());
                if ("驳回".equals(dto.getApproveResult())) {
                    checkIn.setFlowStatus("已关闭");
                }
                break;
            case 4: // 入住配置
                copyWithSerialize(dto, checkIn);
                break;
            case 5: // 签约办理
                copyWithSerialize(dto, checkIn);
                checkIn.setFinishTime(LocalDateTime.now());
                checkIn.setFlowStatus("已完成");
                createContract(checkIn, operator);
                
                // 老人状态改为在住
                if (checkIn.getElderId() != null) {
                    elderlyService.updateStatusToCheckedIn(checkIn.getElderId());
                }
                
                // 更新床位信息：将老人ID绑定到床位
                if (StringUtils.hasText(checkIn.getBedNo()) && checkIn.getElderId() != null) {
                    updateBedWithElderly(checkIn.getBedNo(), checkIn.getElderId());
                }
                break;
            default:
                result.put("msg", "无效步骤"); return result;
        }
        if (!"已关闭".equals(checkIn.getFlowStatus())) {
            checkIn.setCurrentStep(step + 1 > 5 ? 5 : step + 1);
        }
        checkInMapper.updateById(checkIn);
        applyLogService.addLog("入住", checkIn.getId(), checkIn.getBillNo(), STEP_NAMES[step-1], operator, STEP_ROLES[step-1], STEP_OPS[step-1]);
        result.put("code", 200);
        result.put("msg", STEP_NAMES[step-1] + "提交成功");
        result.put("currentStep", checkIn.getCurrentStep());
        return result;
    }

    /**
     * 根据床位号更新床位的老人ID和状态
     */
    private void updateBedWithElderly(String bedNo, Integer elderlyId) {
        try {
            // 根据床位号查询床位
            QueryWrapper<Bed> wrapper = new QueryWrapper<>();
            wrapper.eq("bed_number", bedNo);
            Bed bed = bedService.getOne(wrapper);
            
            if (bed != null) {
                // 更新床位的老人ID和状态
                bed.setElderlyId(elderlyId);
                bed.setStatus(1); // 1-已入住
                bedService.updateById(bed);
                System.out.println("床位更新成功: bedNo=" + bedNo + ", elderlyId=" + elderlyId);
            } else {
                System.err.println("未找到床位: bedNo=" + bedNo);
            }
        } catch (Exception e) {
            System.err.println("更新床位信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createContract(CheckIn checkIn, String creator) {
        Contract contract = new Contract();
        contract.setContractNo(generateBillNo("HT"));
        contract.setContractName(checkIn.getContractName());
        contract.setElderId(checkIn.getElderId());
        contract.setElderName(checkIn.getElderName());
        contract.setIdCard(checkIn.getIdCard());
        contract.setStartDate(checkIn.getStartDate());
        contract.setEndDate(checkIn.getEndDate());
        contract.setStatus(judgeStatus(checkIn.getStartDate(), checkIn.getEndDate()));
        contract.setCreator(creator);
        contract.setCheckInId(checkIn.getId());
        contractMapper.insert(contract);
    }

    @Override
    public CheckIn queryDetail(Integer id) { return checkInMapper.selectById(id); }

    @Override
    public List<ApplyLog> queryLogs(Integer id) {
        CheckIn ci = checkInMapper.selectById(id);
        if (ci == null) return List.of();
        return applyLogService.queryByApply("入住", id);
    }

    @Override
    @Transactional
    public Map<String, Object> revoke(Integer id, String operator) {
        Map<String, Object> result = new HashMap<>();
        CheckIn ci = checkInMapper.selectById(id);
        if (ci == null) { result.put("code", 400); result.put("msg", "单据不存在"); return result; }
        ci.setFlowStatus("已关闭");
        checkInMapper.updateById(ci);
        applyLogService.addLog("入住", id, ci.getBillNo(), "撤销申请", operator, "发起人", "已撤销");
        result.put("code", 200); result.put("msg", "撤销成功");
        return result;
    }

    private String generateBillNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private String judgeStatus(LocalDate start, LocalDate end) {
        if (start == null || end == null) return "未生效";
        LocalDate now = LocalDate.now();
        if (now.isBefore(start)) return "未生效";
        if (now.isAfter(end)) return "已过期";
        return "生效中";
    }

    private void copyWithSerialize(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
