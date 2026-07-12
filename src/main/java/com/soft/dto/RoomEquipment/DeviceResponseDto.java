package com.soft.dto.RoomEquipment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceResponseDto {
    private Integer id;
    private String deviceName;
    private String remarkName;
    private String productName;
    private Integer locationType;
    private Integer locationId;
    private String locationName; // 位置名称（房间号/床位号/老人姓名）
    private Integer status;
    private String createUser;
    private LocalDateTime createTime;
}