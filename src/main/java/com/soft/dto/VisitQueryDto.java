package com.soft.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 来访查询DTO
 */
@Data
public class VisitQueryDto {
    private String visitorName;
    private String visitorPhone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String visitType;
    private Integer source;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}