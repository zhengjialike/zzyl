package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.ContractMapper;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckIn;
import com.soft.pojo.Contract;
import com.soft.pojo.Elder;
import com.soft.pojo.FamilyMember;
import com.soft.service.ApplyLogService;
import com.soft.service.CheckInService;
import com.soft.service.ElderService;
import com.soft.service.FamilyMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    private static final String[] STEP_ROLES = {"发起人", "评估人", "审批人", "配置人", "发起人"};
    private static final String[] STEP_OPS = {"已发起", "已处理", "已审批", "已配置", "已签约"};

    @Autowired private CheckInMapper checkInMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private ApplyLogService applyLogService;
    @Autowired private FamilyMemberService familyMemberService;
    @Autowired private ElderService elderService;

    private static final ObjectMapper JSON = new ObjectMapper();

    /** 把 Object(可能是数组/List)序列化为JSON字符串,null返回null */
    private static String toJson(Object v) {
        if (v == null) return null;
        if (v instanceof String s) return s;
        try { return JSON.writeValueAsString(v); } catch (Exception e) { return v.toString(); }
    }

    /** 把 DTO 中字段映射到 CheckIn POJO(字段名不一致的手动赋值) */
    private void copyWithSerialize(StepSubmitDto dto, CheckIn c) {
        // 基本信息
        c.setElderName(dto.getElderName());
        c.setIdCard(dto.getIdCard());
        c.setSex(dto.getGender());
        c.setBirthday(dto.getBirthday());
        c.setAge(dto.getAge());
        c.setAddress(dto.getAddress());
        c.setPhone(dto.getPhone());
        c.setNation(dto.getNation());
        c.setPoliticsStatus(dto.getPoliticalStatus());
        c.setReligion(dto.getReligion());
        c.setMarriage(dto.getMaritalStatus());
        c.setEducation(dto.getEducation());
        c.setIncomeSource(dto.getIncomeSource());
        c.setMedicalInsurance(dto.getMedicalSecurity());
        c.setMedicareCard(dto.getMedicareCard());
        c.setHobbies(dto.getHobbies());
        c.setPhoto(dto.getPhoto());
        c.setIdCardFront(dto.getIdCardFront());
        c.setIdCardBack(dto.getIdCardBack());
        // 健康评估
        c.setDiseases(toJson(dto.getDiseases()));
        c.setMedications(toJson(dto.getMedications()));
        c.setRiskFalls(dto.getRiskFalls());
        c.setRiskLost(dto.getRiskLost());
        c.setRiskChoking(dto.getRiskChoking());
        c.setRiskComa(dto.getRiskComa());
        c.setRiskSuicide(dto.getRiskSuicide());
        c.setBodyHealth(toJson(dto.getBodyHealth()));
        c.setMedicalCare(toJson(dto.getMedicalCare()));
        c.setAbilitySelf(dto.getAbilitySelf());
        c.setBehaviorIssues(toJson(dto.getBehaviorIssues()));
        c.setMedicalReport(dto.getMedicalReport());
        // 能力评估
        c.setEvalScores(toJson(dto.getEvalScores()));
        c.setEvalTotalScore(dto.getEvalTotalScore());
        c.setEvalSelfScore(dto.getEvalSelfScore());
        c.setEvalMentalScore(dto.getEvalMentalScore());
        c.setEvalPerceptionScore(dto.getEvalPerceptionScore());
        c.setEvalLevel(dto.getEvalLevel());
        c.setLevelChangeReason(toJson(dto.getLevelChangeReason()));
        // 入住配置
        c.setBedNo(dto.getBedNo());
        c.setNursingLevel(dto.getNursingLevel());
        c.setDeposit(dto.getDeposit());
        c.setNursingFee(dto.getNursingFee());
        c.setBedFee(dto.getBedFee());
        c.setOtherFee(dto.getOtherFee());
        c.setInsurancePay(dto.getInsurancePay());
        c.setGovSubsidy(dto.getGovSubsidy());
        if (dto.getStartDate() != null) c.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) c.setEndDate(dto.getEndDate());
        if (dto.getFeeStartDate() != null) c.setFeeStartDate(dto.getFeeStartDate());
        if (dto.getFeeEndDate() != null) c.setFeeEndDate(dto.getFeeEndDate());
        // 签约
        c.setContractName(dto.getContractName());
        c.setSignDate(dto.getSignDate());
        c.setPartyCName(dto.getPartyCName());
        c.setPartyCPhone(dto.getPartyCPhone());
        c.setContractFile(dto.getContractFile());
        // 审批
        if (dto.getApprover() != null) c.setApprover(dto.getApprover());
        if (dto.getApproveOpinion() != null) c.setApproveOpinion(dto.getApproveOpinion());
        if (dto.getApproveResult() != null) c.setApproveResult(dto.getApproveResult());
        if (dto.getRemark() != null) c.setRemark(dto.getRemark());
    }

    @Override
    public Map<String, Object> pageList(CheckInPageDto dto) {
        Page<CheckIn> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<CheckIn> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getBillNo())) wrapper.eq("bill_no", dto.getBillNo());
        if (StringUtils.hasText(dto.getElderName())) wrapper.like("elder_name", dto.getElderName());
        if (StringUtils.hasText(dto.getIdCard())) wrapper.eq("id_card", dto.getIdCard());
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            wrapper.between("check_in_date", dto.getStartDate(), dto.getEndDate());
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

        // 1. 按身份证号查 t_elder,不存在才新增
        Elder elder = elderService.queryByIdCard(dto.getIdCard());
        if (elder == null) {
            elder = new Elder();
            elder.setName(dto.getElderName());
            elder.setIdCard(dto.getIdCard());
            elder.setSex(dto.getGender());
            elder.setBirthday(dto.getBirthday());
            elder.setPhone(dto.getPhone());
            elder.setAddress(dto.getAddress());
            elder.setStatus(0);
            elderService.save(elder);
        }

        // 2. 创建入住申请,回填 elder_id
        CheckIn checkIn = new CheckIn();
        copyWithSerialize(dto, checkIn);
        checkIn.setElderId(elder.getId());
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
                    Elder elderUpd = elderService.getById(checkIn.getElderId());
                    if (elderUpd != null) {
                        elderUpd.setStatus(1);
                        elderService.updateById(elderUpd);
                    }
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
}
