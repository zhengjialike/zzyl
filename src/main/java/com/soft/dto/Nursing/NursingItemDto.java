package com.soft.dto.Nursing;

import lombok.Data;

@Data
public class NursingItemDto {
    private String itemname;   // 护理项目名称（模糊查询条件）
    private String islock;     // 状态（启用/禁用）
    private Integer pageNum = 1;    // 当前页码，默认为1
    private Integer pageSize = 10;  // 每页大小，默认为10
}