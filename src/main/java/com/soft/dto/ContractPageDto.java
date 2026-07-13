package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
/** 合同跟踪列表查询条件。 */
public class ContractPageDto {
    /** 精确匹配合同编号。 */
    private String contractNo;
    /** 按老人姓名模糊匹配。 */
    private String elderName;
    /** 合同状态：未生效、生效中、已过期或已失效。 */
    private String status;
    /** 合同开始日期查询区间起点。 */
    private LocalDate startDate;
    /** 合同开始日期查询区间终点。 */
    private LocalDate endDate;
    /** 页码从 1 开始。 */
    private Integer pageNum = 1;
    /** 每页条数。 */
    private Integer pageSize = 10;
}
