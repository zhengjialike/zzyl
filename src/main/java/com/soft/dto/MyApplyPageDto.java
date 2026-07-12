package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyApplyPageDto {
    private String billNo;
    private String billType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}