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
public class StepSubmitDto {
    private Integer id;
    private Integer step;
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
    // 健康评估 (前端可能传数组,用Object接收)
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
    // 能力评估
    private Object evalScores;
    private Integer evalTotalScore;
    private Integer evalSelfScore;
    private Integer evalMentalScore;
    private Integer evalPerceptionScore;
    private String evalLevel;
    private Object levelChangeReason;
    // 审批
    private String approver;
    private String approveOpinion;
    private String approveResult;
    // 入住配置
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
    // 签约
    private String contractName;
    private LocalDate signDate;
    private String partyCName;
    private String partyCPhone;
    private String contractFile;
    private String remark;
    // === 退住申请 ===
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
    // 账单
    private List<Bill> bills;
}
