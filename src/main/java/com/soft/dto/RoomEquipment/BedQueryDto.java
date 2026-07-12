package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class BedQueryDto {
    private Integer roomId;         // 房间ID
    private Integer status;         // 状态
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}