package com.soft.dto.RoomEquipment;

import lombok.Data;
import java.util.Date;

@Data
public class AlertRecordQueryDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    /**
     * 设备名称搜索
     */
    private String deviceName;

    /**
     * 报警开始时间
     */
    private Date alertStartTime;

    /**
     * 报警结束时间
     */
    private Date alertEndTime;

    /**
     * 处理状态（0-待处理，1-已处理，null-全部）
     */
    private Integer handleStatus;
}