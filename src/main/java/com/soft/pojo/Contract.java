package com.soft.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "t_contract")
@Data
/**
 * 合同跟踪主表。
 *
 * <p>合同在入住配置步骤预生成编号，在入住签约完成后进入合同列表；
 * 退住解除合同步骤只建立关联，直到退住审批通过才正式置为“已失效”。</p>
 */
public class Contract {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 合同编号，格式为 HT + 时间戳 + 随机数。 */
    private String contractNo;

    private String contractName;

    private Integer elderId;

    private String elderName;

    private String idCard;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /** 合同状态：未生效、生效中、已过期、已失效。 */
    private String status;

    private String creator;

    /** 来源入住单，用于聚合签约日期、合同文件和丙方信息。 */
    private Integer checkInId;

    /** 来源退住单；审批通过前只是临时关联，驳回/撤销时会清空。 */
    private Integer checkOutId;

    /** 退住审批通过、合同正式失效的系统时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invalidTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** “入住签约待完成”标记用于隐藏尚未完成签约的预生成合同。 */
    private String remark;
}
