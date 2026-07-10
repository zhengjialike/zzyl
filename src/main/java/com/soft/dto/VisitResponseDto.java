package com.soft.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitResponseDto {
    private Integer id;
    private String visitType;
    private String visitorName;
    private String visitorPhone;
    private String elderName;
    private LocalDateTime visitTime;
    private Integer source;
    private String sourceText;
    private Integer appointmentId;
    private String creator;
    private LocalDateTime createTime;
}