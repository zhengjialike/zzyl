package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckInPageDto {
    private String billNo;
    private String elderName;
    private String idCard;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}