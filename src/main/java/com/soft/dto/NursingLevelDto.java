package com.soft.dto;

import lombok.Data;

@Data
public class NursingLevelDto {
    private String levelname;
    private String islock;
    private Integer pageNum=1;
    private Integer pageSize=10;
}
