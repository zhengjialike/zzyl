package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
/** 退住列表查询条件；日期范围对应 t_check_out.check_out_date。 */
public class CheckOutPageDto {
    /** 精确匹配退住单号。 */
    private String billNo;
    /** 按老人姓名模糊匹配。 */
    private String elderName;
    /** 身份证号精确匹配。 */
    private String idCard;
    /** 退住日期范围开始值。 */
    private LocalDate startDate;
    /** 退住日期范围结束值。 */
    private LocalDate endDate;
    /** 页码从 1 开始。 */
    private Integer pageNum = 1;
    /** 每页条数。 */
    private Integer pageSize = 10;
}
