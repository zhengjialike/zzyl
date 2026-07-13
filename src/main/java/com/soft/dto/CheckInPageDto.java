package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
/** 入住列表查询条件；日期表示入住/合同开始日期的查询区间。 */
public class CheckInPageDto {
    /** 精确匹配入住单号。 */
    private String billNo;
    /** 按老人姓名模糊匹配。 */
    private String elderName;
    /** 身份证号精确匹配。 */
    private String idCard;
    /** 查询区间开始日期，必须与 endDate 同时提供才生效。 */
    private LocalDate startDate;
    /** 查询区间结束日期，必须与 startDate 同时提供才生效。 */
    private LocalDate endDate;
    /** 页码从 1 开始。 */
    private Integer pageNum = 1;
    /** 每页条数。 */
    private Integer pageSize = 10;
}
