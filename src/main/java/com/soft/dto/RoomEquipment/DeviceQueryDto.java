package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class DeviceQueryDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    /**
     * 所属产品ID筛选
     */
    private Integer productId;

    /**
     * 设备名称搜索
     */
    private String deviceName;

    /**
     * 设备ID搜索
     */
    private String deviceId;
}