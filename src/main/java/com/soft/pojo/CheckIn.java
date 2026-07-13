package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "t_check_in")
@Data
/**
 * 入住业务单实体，对应从申请、评估、审批、配置到签约的完整五步流程。
 * 多选项和明细项使用 JSON 字符串保存；页面加载时再反序列化为数组。
 */
public class CheckIn {
    // ---------- 单据和老人基本资料 ----------
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 入住单号，格式为 RZ + 时间戳 + 随机数。 */
    private String billNo;
    /** 关联 t_elderly；老人档案是入住、退住之间的业务主线。 */
    private Integer elderId;
    private String elderName;
    private String idCard;
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Integer age;
    private String address;
    private String phone;
    private String nation;
    private String politicsStatus;
    private String religion;
    private String marriage;
    private String education;
    private String incomeSource;
    private String medicalInsurance;
    private String medicareCard;
    private String hobbies;
    private String photo;
    private String idCardFront;
    private String idCardBack;

    // ---------- 健康评估和能力评估 ----------
    /** 已诊断疾病 JSON 数组。 */
    private String diseases;
    /** 用药名称、方式和剂量组成的 JSON 数组。 */
    private String medications;
    private String riskFalls;
    private String riskLost;
    private String riskChoking;
    private String riskComa;
    private String riskSuicide;
    private String bodyHealth;
    private String medicalCare;
    private String abilitySelf;
    private String behaviorIssues;
    private String medicalReport;
    /** 完整评估答案/汇总 JSON，用于恢复评估表。 */
    private String evalScores;
    /** 独立分数字段用于列表、报表和统计，避免每次解析 evalScores。 */
    private Integer evalTotalScore;
    private Integer evalSelfScore;
    private Integer evalMentalScore;
    private Integer evalPerceptionScore;
    private String evalLevel;
    private String levelChangeReason;

    // ---------- 审批、入住配置和费用 ----------
    private String approveResult;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate feeStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate feeEndDate;
    private BigDecimal deposit;
    private BigDecimal nursingFee;
    private BigDecimal bedFee;
    private BigDecimal otherFee;
    private BigDecimal insurancePay;
    private BigDecimal govSubsidy;

    // ---------- 签约资料 ----------
    private String contractName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate signDate;
    private String partyCName;
    private String partyCPhone;
    private String contractFile;

    // ---------- 流程状态和审计信息 ----------
    private String bedNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    private Integer currentStep;
    private String flowStatus;
    private String applicant;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String remark;
    private String healthEval;
    private String abilityEval;
    private String nursingLevel;
    private String advisor;
    private String approver;
    private String approveOpinion;

    /** 入住配置后预生成的合同编号，来自 t_contract，不直接映射到入住表。 */
    @TableField(exist = false)
    private String contractNo;
}
