package com.soft.service;

import com.soft.dto.AppointmentRecordDto;
import com.soft.pojo.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_appointment】的数据库操作Service
* @createDate 2026-07-14 00:35:23
*/
public interface AppointmentService extends IService<Appointment> {
    /*实现预约记录分页查询*/
    public Map<String,Object>
    queryAppointmentListService(AppointmentRecordDto dto);
}
