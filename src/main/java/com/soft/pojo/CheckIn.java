package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "t_check_in")
@Data
public class CheckIn {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String billNo;
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
    private String diseases;
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
    private String evalScores;
    private Integer evalTotalScore;
    private Integer evalSelfScore;
    private Integer evalMentalScore;
    private Integer evalPerceptionScore;
    private String evalLevel;
    private String levelChangeReason;
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
    private String contractName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate signDate;
    private String partyCName;
    private String partyCPhone;
    private String contractFile;
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
}
