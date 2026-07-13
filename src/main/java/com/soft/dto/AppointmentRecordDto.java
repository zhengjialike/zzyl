package com.soft.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AppointmentRecordDto {

    private String appuser;
    private String phone;
    private String islock;
    private Timestamp starttime;
    private Timestamp endtime;

    private Integer pageNum;
    private Integer pageSize;

}
