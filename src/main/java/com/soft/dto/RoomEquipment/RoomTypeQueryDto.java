package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class RoomTypeQueryDto {
    private String typeName;      // 房型名称（模糊查询）
    private Integer status;       // 状态（0-禁用，1-启用）
    private Integer pageNum = 1;  // 当前页码
    private Integer pageSize = 10; // 每页大小
}