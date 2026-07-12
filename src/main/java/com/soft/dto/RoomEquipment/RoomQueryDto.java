package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class RoomQueryDto {
    private Integer floorId;        // 楼层ID
    private String roomNumber;      // 房间号
    private Integer roomTypeId;     // 房型ID
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}