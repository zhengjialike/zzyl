package com.soft.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDto {
    private Integer id;
    private String appointmentType;
    private String visitorName;
    private String visitorPhone;
    private String elderName;
    private LocalDateTime appointmentTime;
    private Integer status;
    private String statusText;
    private LocalDateTime arrivalTime;
    private String creator;
    private LocalDateTime createTime;
}