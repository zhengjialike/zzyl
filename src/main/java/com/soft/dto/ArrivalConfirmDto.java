package com.soft.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArrivalConfirmDto {
    private Integer appointmentId;
    private LocalDateTime arrivalTime;
}