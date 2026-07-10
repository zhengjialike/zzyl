package com.soft.dto;

import lombok.Data;

@Data
public class NursingPlainPageDto {

    private String plainname;
    private String islock;
    private Integer pageNum=1;
    private Integer pageSize=10;
}

