package com.soft.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soft.pojo.FamilyMember;
import com.soft.pojo.Bill;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * 入住五步流程和退住七步流程共用的步骤提交对象。
 *
 * <p>页面会携带整个响应式表单，但业务层只读取当前 step 允许修改的字段。
 * ignoreUnknown 用于兼容页面展示字段；真正的流程节点、操作人和关联老人仍以后端数据库/Session 为准。</p>
 */
public class StepSubmitDto {
    /** 业务单主键；第一步新建申请时为空。 */
    private Integer id;
    /** 当前提交节点，必须与数据库 current_step 相同。 */
    private Integer step;
    /** 保留兼容字段；实际操作人由 Controller 从 Session 传入，不能信任此值。 */
    private String operator;
    // === 入住申请表单字段 ===
    private Integer elderId;
    private String elderName;
    private String idCard;
    private String gender;
    private LocalDate birthday;
    private Integer age;
    private String address;
    private String phone;
    private String nation;
    private String politicalStatus;
    private String religion;
    private String maritalStatus;
    private String education;
    private String incomeSource;
    private String medicalSecurity;
    private String medicareCard;
    private String hobbies;
    private String photo;
    private String idCardFront;
    private String idCardBack;
    private List<FamilyMember> familyMembers;
    // 健康评估：多选和明细数据可能是数组或对象，先用 Object 接收，再由业务层序列化为 JSON。
    private Object diseases;
    private Object medications;
    private String riskFalls;
    private String riskLost;
    private String riskChoking;
    private String riskComa;
    private String riskSuicide;
    private Object bodyHealth;
    private Object medicalCare;
    private String abilitySelf;
    private Object behaviorIssues;
    private String medicalReport;
    // 能力评估：既保存完整答题/汇总 JSON，也保存分维度分数，便于后续统计查询。
    private Object evalScores;
    private Integer evalTotalScore;
    private Integer evalSelfScore;
    private Integer evalMentalScore;
    private Integer evalPerceptionScore;
    private String evalLevel;
    private Object levelChangeReason;
    // 入住/退住审批字段。不同节点读取 approveOpinion 或 approveRemark。
    private String approver;
    private String approveOpinion;
    private String approveResult;
    // 入住配置：包含入住期限、费用期限、床位、护理等级和费用构成。
    private String bedNo;
    private LocalDate checkInDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate feeStartDate;
    private LocalDate feeEndDate;
    private BigDecimal deposit;
    private BigDecimal nursingFee;
    private BigDecimal bedFee;
    private BigDecimal otherFee;
    private BigDecimal insurancePay;
    private BigDecimal govSubsidy;
    private String nursingLevel;
    private String advisor;
    // 入住签约：合同主表保存合同索引信息，文件、丙方和签约日期保存在入住单。
    private String contractName;
    private LocalDate signDate;
    private String partyCName;
    private String partyCPhone;
    private String contractFile;
    private String remark;
    // === 退住申请、合同解除和费用清算 ===
    private LocalDate checkOutDate;
    private String checkoutReason;
    private LocalDate terminateDate;
    private String terminateAgreement;
    private Integer contractId;
    private String contractNo;
    private String refundMethod;
    private String refundRemark;
    private String refundVoucher;
    private BigDecimal refundAmount;
    private BigDecimal settlementAmount;
    private String settlementStatus;
    private String approveRemark;
    // 仅退住第 4 步“调整账单”使用，业务层会校验账单必须属于当前老人。
    private List<Bill> bills;
}
