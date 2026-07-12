package com.soft.dto.RoomEquipment;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlertRuleResponseDto {
    private Integer id;
    private String ruleName;
    private Integer productId;
    private String productName;
    private String functionModule;
    private String functionName;
    private String deviceId;
    private String deviceName;
    private String operator;
    private BigDecimal threshold;
    private Integer durationPeriod;
    private Date effectiveStart;
    private Date effectiveEnd;
    private Integer silentMinutes;
    private Integer status;
    private String createUser;
    private Date createTime;
    private Date updateTime;
}