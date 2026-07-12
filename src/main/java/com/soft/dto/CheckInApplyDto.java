package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckInApplyDto {
    private Integer elderId;
    private String elderName;
    private String idCard;
    private String bedNo;
    private LocalDate checkInDate;
    private String remark;
}