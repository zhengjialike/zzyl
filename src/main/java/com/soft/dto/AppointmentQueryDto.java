package com.soft.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约查询DTO
 */
@Data
public class AppointmentQueryDto {
    private String visitorName;
    private String visitorPhone;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String appointmentType;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}