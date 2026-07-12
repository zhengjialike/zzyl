package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class FloorQueryDto {
    private String floorName;   // 楼层名称
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}