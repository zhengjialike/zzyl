package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyApplyPageDto {
    private String billNo;
    private String billType;
    /** 全部时为空；其余取值为申请中、已完成、已关闭。 */
    private String flowStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
