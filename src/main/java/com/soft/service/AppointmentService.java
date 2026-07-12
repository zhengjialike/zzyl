package com.soft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.AppointmentQueryDto;
import com.soft.dto.AppointmentResponseDto;
import com.soft.dto.ArrivalConfirmDto;
import com.soft.pojo.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Appointment;

/**
* @description 针对表【t_appointment】的数据库操作Service
* @author 12
* @description 针对表【t_appointment(预约记录表)】的数据库操作Service
* @createDate 2026-07-09 23:23:59
*/
public interface AppointmentService extends IService<Appointment> {
    Page<AppointmentResponseDto> queryAppointments(AppointmentQueryDto queryDto);

    boolean confirmArrival(ArrivalConfirmDto confirmDto);

    boolean cancelAppointment(Integer appointmentId);

    Appointment getAppointmentById(Integer id);

}
