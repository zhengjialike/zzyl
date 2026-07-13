package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.ContractMapper;
import com.soft.mapper.BedMapper;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.Bed;
import com.soft.pojo.CheckIn;
import com.soft.pojo.Contract;
import com.soft.pojo.Elderly;
import com.soft.pojo.FamilyMember;
import com.soft.service.ApplyLogService;
import com.soft.service.CheckInService;
import com.soft.service.ElderlyService;
import com.soft.service.FamilyMemberService;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
/**
 * 入住五步流程业务实现。
 *
 * <p>流程顺序固定为：申请入住 → 入住评估 → 入住审批 → 入住配置 → 签约办理。
 * currentStep 表示下一次允许提交的步骤，flowStatus 表示整张单据是否仍可办理。
 * 涉及老人、家属、床位、合同和日志的写操作使用事务保证一致性。</p>
 */
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements CheckInService {

    // 三组数组下标与步骤号 - 1 对应，用于统一生成可读的流程日志。
    private static final String[] STEP_NAMES = {"申请入住", "入住评估", "入住审批", "入住配置", "签约办理"};
    private static final String[] STEP_ROLES = {"发起人", "评估人", "审批人", "配置人", "发起人"};
    private static final String[] STEP_OPS = {"已发起", "已处理", "已审批", "已配置", "已签约"};
    private static final ObjectMapper JSON = new ObjectMapper();

    @Autowired private CheckInMapper checkInMapper;
    @Autowired private ContractMapper contractMapper;
    @Autowired private ApplyLogService applyLogService;
    @Autowired private FamilyMemberService familyMemberService;
    @Autowired private ElderlyService elderlyService;
    @Autowired private BedMapper bedMapper;

    /**
     * 把前端多选项/明细对象序列化为 JSON 保存。
     * 已经是字符串时直接返回，兼容历史调用方已经完成序列化的情况。
     */
    private static String toJson(Object value) {
        if (value == null) return null;
        if (value instanceof String text) return text;
        try {
            return JSON.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("表单数据序列化失败", ex);
        }
    }

    /**
     * 将步骤 DTO 中非 null 字段复制到入住单。
     *
     * <p>这里不使用 BeanUtils 全量覆盖：分步表单每次只提交一部分字段，若复制 null 会把前面步骤的数据清空；
     * 同时页面字段名与实体字段名并不完全相同，多选字段还需要 JSON 序列化。</p>
     */
    private void copyWithSerialize(StepSubmitDto dto, CheckIn target) {
        if (dto.getElderName() != null) target.setElderName(dto.getElderName());
        if (dto.getIdCard() != null) target.setIdCard(dto.getIdCard());
        if (dto.getGender() != null) target.setSex(dto.getGender());
        if (dto.getBirthday() != null) target.setBirthday(dto.getBirthday());
        if (dto.getAge() != null) target.setAge(dto.getAge());
        if (dto.getAddress() != null) target.setAddress(dto.getAddress());
        if (dto.getPhone() != null) target.setPhone(dto.getPhone());
        if (dto.getNation() != null) target.setNation(dto.getNation());
        if (dto.getPoliticalStatus() != null) target.setPoliticsStatus(dto.getPoliticalStatus());
        if (dto.getReligion() != null) target.setReligion(dto.getReligion());
        if (dto.getMaritalStatus() != null) target.setMarriage(dto.getMaritalStatus());
        if (dto.getEducation() != null) target.setEducation(dto.getEducation());
        if (dto.getIncomeSource() != null) target.setIncomeSource(dto.getIncomeSource());
        if (dto.getMedicalSecurity() != null) target.setMedicalInsurance(dto.getMedicalSecurity());
        if (dto.getMedicareCard() != null) target.setMedicareCard(dto.getMedicareCard());
        if (dto.getHobbies() != null) target.setHobbies(dto.getHobbies());
        if (dto.getPhoto() != null) target.setPhoto(dto.getPhoto());
        if (dto.getIdCardFront() != null) target.setIdCardFront(dto.getIdCardFront());
        if (dto.getIdCardBack() != null) target.setIdCardBack(dto.getIdCardBack());
        if (dto.getDiseases() != null) target.setDiseases(toJson(dto.getDiseases()));
        if (dto.getMedications() != null) target.setMedications(toJson(dto.getMedications()));
        if (dto.getRiskFalls() != null) target.setRiskFalls(dto.getRiskFalls());
        if (dto.getRiskLost() != null) target.setRiskLost(dto.getRiskLost());
        if (dto.getRiskChoking() != null) target.setRiskChoking(dto.getRiskChoking());
        if (dto.getRiskComa() != null) target.setRiskComa(dto.getRiskComa());
        if (dto.getRiskSuicide() != null) target.setRiskSuicide(dto.getRiskSuicide());
        if (dto.getBodyHealth() != null) target.setBodyHealth(toJson(dto.getBodyHealth()));
        if (dto.getMedicalCare() != null) target.setMedicalCare(toJson(dto.getMedicalCare()));
        if (dto.getAbilitySelf() != null) target.setAbilitySelf(dto.getAbilitySelf());
        if (dto.getBehaviorIssues() != null) target.setBehaviorIssues(toJson(dto.getBehaviorIssues()));
        if (dto.getMedicalReport() != null) target.setMedicalReport(dto.getMedicalReport());
        if (dto.getEvalScores() != null) target.setEvalScores(toJson(dto.getEvalScores()));
        if (dto.getEvalTotalScore() != null) target.setEvalTotalScore(dto.getEvalTotalScore());
        if (dto.getEvalSelfScore() != null) target.setEvalSelfScore(dto.getEvalSelfScore());
        if (dto.getEvalMentalScore() != null) target.setEvalMentalScore(dto.getEvalMentalScore());
        if (dto.getEvalPerceptionScore() != null) target.setEvalPerceptionScore(dto.getEvalPerceptionScore());
        if (dto.getEvalLevel() != null) target.setEvalLevel(dto.getEvalLevel());
        if (dto.getLevelChangeReason() != null) target.setLevelChangeReason(toJson(dto.getLevelChangeReason()));
        if (dto.getBedNo() != null) target.setBedNo(dto.getBedNo());
        if (dto.getNursingLevel() != null) target.setNursingLevel(dto.getNursingLevel());
        if (dto.getAdvisor() != null) target.setAdvisor(dto.getAdvisor());
        if (dto.getStartDate() != null) target.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) target.setEndDate(dto.getEndDate());
        if (dto.getFeeStartDate() != null) target.setFeeStartDate(dto.getFeeStartDate());
        if (dto.getFeeEndDate() != null) target.setFeeEndDate(dto.getFeeEndDate());
        if (dto.getDeposit() != null) target.setDeposit(dto.getDeposit());
        if (dto.getNursingFee() != null) target.setNursingFee(dto.getNursingFee());
        if (dto.getBedFee() != null) target.setBedFee(dto.getBedFee());
        if (dto.getOtherFee() != null) target.setOtherFee(dto.getOtherFee());
        if (dto.getInsurancePay() != null) target.setInsurancePay(dto.getInsurancePay());
        if (dto.getGovSubsidy() != null) target.setGovSubsidy(dto.getGovSubsidy());
        if (dto.getContractName() != null) target.setContractName(dto.getContractName());
        if (dto.getSignDate() != null) target.setSignDate(dto.getSignDate());
        if (dto.getPartyCName() != null) target.setPartyCName(dto.getPartyCName());
        if (dto.getPartyCPhone() != null) target.setPartyCPhone(dto.getPartyCPhone());
        if (dto.getContractFile() != null) target.setContractFile(dto.getContractFile());
        if (dto.getRemark() != null) target.setRemark(dto.getRemark());
    }

    @Override
    /** 列表条件按需拼接，未填写的条件不进入 SQL；默认按创建时间倒序。 */
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
    /**
     * 完成第一步申请入住。
     *
     * <p>先按身份证号复用或创建老人档案，再创建入住单和家属记录。此时老人仍不是“在住”，
     * 只有第五步签约完成后才改为在住，避免办理中申请污染在住老人列表。</p>
     */
    public Map<String, Object> startApply(StepSubmitDto dto, String applicant) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(dto.getElderName()) || !StringUtils.hasText(dto.getIdCard())
                || !StringUtils.hasText(dto.getAddress()) || !StringUtils.hasText(dto.getPhone())) {
            result.put("code", 400); result.put("msg", "请完整填写老人基本信息"); return result;
        }
        if (dto.getFamilyMembers() == null || dto.getFamilyMembers().isEmpty()
                || dto.getFamilyMembers().stream().anyMatch(item -> !StringUtils.hasText(item.getName())
                || !StringUtils.hasText(item.getPhone()) || !StringUtils.hasText(item.getRelation()))) {
            result.put("code", 400); result.put("msg", "家属信息不完整，请输入家属信息"); return result;
        }
        if (!StringUtils.hasText(dto.getPhoto()) || !StringUtils.hasText(dto.getIdCardFront())
                || !StringUtils.hasText(dto.getIdCardBack())) {
            result.put("code", 400); result.put("msg", "请上传一寸照片和身份证正反面"); return result;
        }

        // 1. 身份证号是老人档案的业务唯一标识：不存在则新增，已存在则同步本次填写的最新资料。
        Elderly elderly = elderlyService.queryByIdCard(dto.getIdCard());
        if (elderly != null && Integer.valueOf(1).equals(elderly.getStatus())) {
            result.put("code", 400); result.put("msg", "该老人已入住，请重新输入"); return result;
        }
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
        else {
            elderly.setRealName(dto.getElderName());
            elderly.setGender("男".equals(dto.getGender()) ? 1 : ("女".equals(dto.getGender()) ? 2 : 0));
            elderly.setBirthday(dto.getBirthday());
            elderly.setPhone(dto.getPhone());
            elderly.setAddress(dto.getAddress());
            elderly.setStatus(0);
            elderlyService.updateById(elderly);
        }

        // 2. 创建入住申请并回填 elder_id；第一步已完成，所以 currentStep 从 2 开始。
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
    /**
     * 提交入住第 2～5 步。
     * 每次先核对 flowStatus/currentStep，既防止前端越级，也避免双击或旧页面重复提交。
     */
    public Map<String, Object> submitStep(StepSubmitDto dto, String operator) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        CheckIn checkIn = checkInMapper.selectById(dto.getId());
        if (checkIn == null) { result.put("msg", "单据不存在"); return result; }
        int step = dto.getStep();
        if (step < 2 || step > 5) { result.put("msg", "无效步骤"); return result; }
        if (!"申请中".equals(checkIn.getFlowStatus())) { result.put("msg", "当前入住申请已结束，不能继续提交"); return result; }
        if (checkIn.getCurrentStep() == null || !checkIn.getCurrentStep().equals(step)) {
            result.put("msg", "流程节点已变化，请刷新页面后重试"); return result;
        }
        String operation = STEP_OPS[step - 1];
        switch (step) {
            case 2: // 入住评估 (健康+能力+报告)
                // 保存健康资料、能力评估答案、各维度分数和最终护理等级建议。
                copyWithSerialize(dto, checkIn);
                break;
            case 3: // 入住审批
                // 驳回直接关闭整张入住单；通过后才允许进入床位和费用配置。
                if (!"通过".equals(dto.getApproveResult()) && !"驳回".equals(dto.getApproveResult())) {
                    result.put("msg", "请选择审批结果"); return result;
                }
                checkIn.setApprover(operator);
                checkIn.setApproveOpinion(dto.getApproveOpinion());
                checkIn.setApproveResult(dto.getApproveResult());
                operation = "驳回".equals(dto.getApproveResult()) ? "已驳回" : "已通过";
                if ("驳回".equals(dto.getApproveResult())) {
                    checkIn.setFlowStatus("已关闭");
                }
                break;
            case 4: // 入住配置
                if (!StringUtils.hasText(dto.getBedNo()) || !StringUtils.hasText(dto.getNursingLevel())
                        || dto.getStartDate() == null || dto.getEndDate() == null
                        || dto.getFeeStartDate() == null || dto.getFeeEndDate() == null) {
                    result.put("msg", "请完整填写入住配置和费用期限"); return result;
                }
                // 床位占用和预生成合同与入住单更新处于同一事务，任一步失败都会整体回滚。
                bindBed(checkIn, dto.getBedNo());
                copyWithSerialize(dto, checkIn);
                prepareContract(checkIn, operator);
                break;
            case 5: // 签约办理
                if (!StringUtils.hasText(dto.getContractName()) || dto.getSignDate() == null
                        || !StringUtils.hasText(dto.getContractFile())) {
                    result.put("msg", "请填写合同名称、签约日期并上传合同"); return result;
                }
                copyWithSerialize(dto, checkIn);
                checkIn.setFinishTime(LocalDateTime.now());
                checkIn.setFlowStatus("已完成");
                // 签约完成后去除“待完成”标记，合同才进入合同管理列表。
                finishContract(checkIn, operator);
                // 老人状态改为在住
                if (checkIn.getElderId() != null) {
                    Elderly elderlyUpd = elderlyService.getById(checkIn.getElderId());
                    if (elderlyUpd != null) {
                        elderlyUpd.setStatus(1);
                        elderlyService.updateById(elderlyUpd);
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
        applyLogService.addLog("入住", checkIn.getId(), checkIn.getBillNo(), STEP_NAMES[step-1], operator, STEP_ROLES[step-1], operation);
        result.put("code", 200);
        result.put("msg", STEP_NAMES[step-1] + "提交成功");
        result.put("currentStep", checkIn.getCurrentStep());
        if (step == 4) {
            QueryWrapper<Contract> contractWrapper = new QueryWrapper<>();
            contractWrapper.eq("check_in_id", checkIn.getId()).last("LIMIT 1");
            Contract prepared = contractMapper.selectOne(contractWrapper);
            if (prepared != null) result.put("contractNo", prepared.getContractNo());
        }
        return result;
    }

    /**
     * 在入住配置完成时预生成合同编号，便于签约页面提前展示。
     * 重复提交配置步骤时按 check_in_id 更新同一份合同，不重复插入。
     */
    private void prepareContract(CheckIn checkIn, String creator) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("check_in_id", checkIn.getId()).last("LIMIT 1");
        Contract contract = contractMapper.selectOne(wrapper);
        if (contract == null) {
            contract = new Contract();
            contract.setContractNo(generateBillNo("HT"));
            contract.setCheckInId(checkIn.getId());
            // 预生成的合同只用于在签约页展示编号，签约完成前不进入合同跟踪列表。
            contract.setRemark("入住签约待完成");
        }
        contract.setContractName(checkIn.getContractName());
        contract.setElderId(checkIn.getElderId());
        contract.setElderName(checkIn.getElderName());
        contract.setIdCard(checkIn.getIdCard());
        contract.setStartDate(checkIn.getStartDate());
        contract.setEndDate(checkIn.getEndDate());
        contract.setStatus(judgeStatus(checkIn.getStartDate(), checkIn.getEndDate()));
        contract.setCreator(creator);
        if (contract.getId() == null) contractMapper.insert(contract); else contractMapper.updateById(contract);
    }

    /** 将预生成合同补齐签约资料并移除待完成标记。 */
    private void finishContract(CheckIn checkIn, String creator) {
        prepareContract(checkIn, creator);
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("check_in_id", checkIn.getId()).last("LIMIT 1");
        Contract contract = contractMapper.selectOne(wrapper);
        if (contract != null) {
            contract.setContractName(checkIn.getContractName());
            contract.setCreator(creator);
            contract.setRemark(null);
            contractMapper.updateById(contract);
        }
    }

    /**
     * 绑定床位前再次从数据库检查占用情况，并释放同一老人之前绑定的其他床位。
     * 该校验不能只放在前端，因为打开选择框后床位仍可能被其他请求占用。
     */
    private void bindBed(CheckIn checkIn, String bedNo) {
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.eq("bed_number", bedNo).last("LIMIT 1");
        Bed selected = bedMapper.selectOne(wrapper);
        if (selected == null) throw new IllegalArgumentException("所选床位不存在");
        if (selected.getElderlyId() != null && !selected.getElderlyId().equals(checkIn.getElderId())) {
            throw new IllegalStateException("所选床位已入住，请重新选择");
        }
        QueryWrapper<Bed> oldWrapper = new QueryWrapper<>();
        oldWrapper.eq("elderly_id", checkIn.getElderId());
        for (Bed old : bedMapper.selectList(oldWrapper)) {
            if (!old.getId().equals(selected.getId())) {
                old.setElderlyId(null);
                old.setStatus(0);
                bedMapper.updateById(old);
            }
        }
        selected.setElderlyId(checkIn.getElderId());
        selected.setStatus(1);
        bedMapper.updateById(selected);
    }

    @Override
    /** 返回未绑定老人或明确标记为空闲的床位，并按床位号排序。 */
    public List<Bed> queryAvailableBeds() {
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.and(item -> item.isNull("elderly_id").or().eq("status", 0)).orderByAsc("bed_number");
        return bedMapper.selectList(wrapper);
    }

    @Override
    /** 查询主表后从合同表补充 contractNo；该字段不是 t_check_in 的物理列。 */
    public CheckIn queryDetail(Integer id) {
        CheckIn detail = checkInMapper.selectById(id);
        if (detail == null) return null;
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.eq("check_in_id", id).last("LIMIT 1");
        Contract contract = contractMapper.selectOne(wrapper);
        if (contract != null) detail.setContractNo(contract.getContractNo());
        return detail;
    }

    @Override
    /** 单据不存在时返回空集合，避免详情页日志请求出现 500。 */
    public List<ApplyLog> queryLogs(Integer id) {
        CheckIn ci = checkInMapper.selectById(id);
        if (ci == null) return List.of();
        return applyLogService.queryByApply("入住", id);
    }

    @Override
    @Transactional
    /**
     * 撤销仍在申请中的入住单。
     * 已经配置过的床位需要显式设为 NULL，预生成合同也要删除，但老人档案保留供后续重新申请。
     */
    public Map<String, Object> revoke(Integer id, String operator) {
        Map<String, Object> result = new HashMap<>();
        CheckIn ci = checkInMapper.selectById(id);
        if (ci == null) { result.put("code", 400); result.put("msg", "单据不存在"); return result; }
        if (!"申请中".equals(ci.getFlowStatus())) {
            result.put("code", 400); result.put("msg", "仅申请中的入住单可以撤销"); return result;
        }
        QueryWrapper<Bed> bedWrapper = new QueryWrapper<>();
        bedWrapper.eq("elderly_id", ci.getElderId());
        for (Bed bed : bedMapper.selectList(bedWrapper)) {
            // MyBatis-Plus 默认忽略值为 null 的字段，因此不能通过 updateById 解除老人绑定。
            // 使用 UpdateWrapper 显式生成 elderly_id = NULL，确保撤销后床位真正恢复为空闲。
            UpdateWrapper<Bed> releaseBed = new UpdateWrapper<>();
            releaseBed.eq("id", bed.getId())
                    .set("elderly_id", null)
                    .set("status", 0);
            bedMapper.update(null, releaseBed);
        }
        QueryWrapper<Contract> contractWrapper = new QueryWrapper<>();
        contractWrapper.eq("check_in_id", id);
        contractMapper.delete(contractWrapper);
        ci.setFlowStatus("已关闭");
        checkInMapper.updateById(ci);
        applyLogService.addLog("入住", id, ci.getBillNo(), "撤销申请", operator, "发起人", "已撤销");
        result.put("code", 200); result.put("msg", "撤销成功");
        return result;
    }

    /** 生成带业务前缀的低碰撞单号；数据库主键仍是最终唯一标识。 */
    private String generateBillNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /** 根据合同期限推导日期型状态；业务型“已失效”由退住流程维护。 */
    private String judgeStatus(LocalDate start, LocalDate end) {
        if (start == null || end == null) return "未生效";
        LocalDate now = LocalDate.now();
        if (now.isBefore(start)) return "未生效";
        if (now.isAfter(end)) return "已过期";
        return "生效中";
    }

}
