package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractPageDto {
    private String contractNo;
    private String elderName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}