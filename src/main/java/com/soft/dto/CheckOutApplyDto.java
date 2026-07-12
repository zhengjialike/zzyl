package com.soft.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckOutApplyDto {
    private Integer elderId;
    private String elderName;
    private String idCard;
    private LocalDate checkOutDate;
    private String remark;
}