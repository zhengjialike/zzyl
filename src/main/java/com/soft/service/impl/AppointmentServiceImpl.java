package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.AppointmentQueryDto;
import com.soft.dto.AppointmentResponseDto;
import com.soft.dto.ArrivalConfirmDto;
import com.soft.pojo.Appointment;
import com.soft.service.AppointmentService;
import com.soft.mapper.AppointmentMapper;
import com.soft.service.VisitRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 12
* @description 针对表【t_appointment(预约记录表)】的数据库操作Service实现
* @createDate 2026-07-09 23:23:59
*/
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
    implements AppointmentService{

    private final AppointmentMapper appointmentMapper;
    private final VisitRecordService visitRecordService;


    public AppointmentServiceImpl(AppointmentMapper appointmentMapper,
                                  VisitRecordService visitRecordService) {
        this.appointmentMapper = appointmentMapper;
        this.visitRecordService = visitRecordService;
    }

    @Override
    public Page<AppointmentResponseDto> queryAppointments(AppointmentQueryDto queryDto) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();

        if (queryDto.getVisitorName() != null && !queryDto.getVisitorName().isEmpty()) {
            wrapper.like(Appointment::getVisitorName, queryDto.getVisitorName());
        }

        if (queryDto.getVisitorPhone() != null && !queryDto.getVisitorPhone().isEmpty()) {
            wrapper.eq(Appointment::getVisitorPhone, queryDto.getVisitorPhone());
        }

        if (queryDto.getStatus() != null) {
            wrapper.eq(Appointment::getStatus, queryDto.getStatus());
        }

        if (queryDto.getStartTime() != null && queryDto.getEndTime() != null) {
            wrapper.between(Appointment::getAppointmentTime, queryDto.getStartTime(), queryDto.getEndTime());
        }

        if (queryDto.getAppointmentType() != null && !queryDto.getAppointmentType().isEmpty()) {
            wrapper.eq(Appointment::getAppointmentType, queryDto.getAppointmentType());
        }

        wrapper.orderByDesc(Appointment::getAppointmentTime);

        Page<Appointment> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        Page<Appointment> appointmentPage = appointmentMapper.selectPage(page, wrapper);

        Page<AppointmentResponseDto> resultPage = new Page<>(appointmentPage.getCurrent(),
                appointmentPage.getSize(), appointmentPage.getTotal());

        List<AppointmentResponseDto> dtoList = appointmentPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        resultPage.setRecords(dtoList);
        return resultPage;
    }

    @Override
    public boolean cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null || appointment.getStatus() != 0) {
            return false;
        }

        appointment.setStatus(2);
        return appointmentMapper.updateById(appointment) > 0;
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return appointmentMapper.selectById(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public boolean confirmArrival(ArrivalConfirmDto confirmDto) {
        Appointment appointment = appointmentMapper.selectById(confirmDto.getAppointmentId());
        if (appointment == null) {
            return false;
        }

        String visitType = appointment.getAppointmentType().replace("预约", "来访");

        boolean success = visitRecordService.createVisitFromAppointment(
                appointment.getId(),
                visitType,
                appointment.getVisitorName(),
                appointment.getVisitorPhone(),
                appointment.getElderName(),
                confirmDto.getArrivalTime(),
                appointment.getCreator()
        );

        return success;
    }

    private AppointmentResponseDto convertToDto(Appointment appointment) {
        AppointmentResponseDto dto = new AppointmentResponseDto();
        BeanUtils.copyProperties(appointment, dto);
        dto.setStatusText(getStatusText(appointment.getStatus()));
        return dto;
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0:
                return "待上门";
            case 1:
                return "已完成";
            case 2:
                return "已取消";
            case 3:
                return "已过期";
            default:
                return "未知";
        }
    }

}




