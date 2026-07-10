package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.VisitQueryDto;
import com.soft.dto.VisitRegisterDto;
import com.soft.dto.VisitResponseDto;
import com.soft.mapper.AppointmentMapper;
import com.soft.mapper.VisitRecordMapper;
import com.soft.pojo.Appointment;
import com.soft.pojo.VisitRecord;
import com.soft.service.VisitRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 12
* @description 针对表【t_visit_record(来访记录表)】的数据库操作Service实现
* @createDate 2026-07-09 23:24:22
*/
@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord>
    implements VisitRecordService{
    private final VisitRecordMapper visitRecordMapper;
    private final AppointmentMapper appointmentMapper;

    public VisitRecordServiceImpl(VisitRecordMapper visitRecordMapper,
                                  AppointmentMapper appointmentMapper) {
        this.visitRecordMapper = visitRecordMapper;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public Page<VisitResponseDto> queryVisits(VisitQueryDto queryDto) {
        LambdaQueryWrapper<VisitRecord> wrapper = new LambdaQueryWrapper<>();

        if (queryDto.getVisitorName() != null && !queryDto.getVisitorName().isEmpty()) {
            wrapper.like(VisitRecord::getVisitorName, queryDto.getVisitorName());
        }

        if (queryDto.getVisitorPhone() != null && !queryDto.getVisitorPhone().isEmpty()) {
            wrapper.eq(VisitRecord::getVisitorPhone, queryDto.getVisitorPhone());
        }

        if (queryDto.getStartTime() != null && queryDto.getEndTime() != null) {
            wrapper.between(VisitRecord::getVisitTime, queryDto.getStartTime(), queryDto.getEndTime());
        }

        if (queryDto.getVisitType() != null && !queryDto.getVisitType().isEmpty()) {
            wrapper.eq(VisitRecord::getVisitType, queryDto.getVisitType());
        }

        if (queryDto.getSource() != null) {
            wrapper.eq(VisitRecord::getSource, queryDto.getSource());
        }

        wrapper.orderByDesc(VisitRecord::getVisitTime);

        Page<VisitRecord> page = new Page<>(queryDto.getPageNum(), queryDto.getPageSize());
        Page<VisitRecord> visitPage = visitRecordMapper.selectPage(page, wrapper);

        Page<VisitResponseDto> resultPage = new Page<>(visitPage.getCurrent(),
                visitPage.getSize(), visitPage.getTotal());

        List<VisitResponseDto> dtoList = visitPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        resultPage.setRecords(dtoList);
        return resultPage;
    }

    @Override
    public boolean registerDirectVisit(VisitRegisterDto registerDto, String creator) {
        VisitRecord visitRecord = new VisitRecord();
        BeanUtils.copyProperties(registerDto, visitRecord);
        visitRecord.setSource(1);
        visitRecord.setCreator(creator);

        return visitRecordMapper.insert(visitRecord) > 0;
    }

    @Override
    @Transactional
    public boolean createVisitFromAppointment(Integer appointmentId, String visitType, 
                                               String visitorName, String visitorPhone, 
                                               String elderName, LocalDateTime arrivalTime, 
                                               String creator) {
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setVisitType(visitType);
        visitRecord.setVisitorName(visitorName);
        visitRecord.setVisitorPhone(visitorPhone);
        visitRecord.setElderName(elderName);
        visitRecord.setVisitTime(arrivalTime);
        visitRecord.setSource(0);
        visitRecord.setAppointmentId(appointmentId);
        visitRecord.setCreator(creator);
        visitRecord.setRemark("由预约自动生成");
        
        int insertResult = visitRecordMapper.insert(visitRecord);
        
        if (insertResult > 0) {
            Appointment appointment = appointmentMapper.selectById(appointmentId);
            if (appointment != null) {
                appointment.setStatus(1);
                appointment.setArrivalTime(arrivalTime);
                appointmentMapper.updateById(appointment);
            }
        }
        
        return insertResult > 0;
    }

    @Override
    public VisitRecord getVisitById(Integer id) {
        return visitRecordMapper.selectById(id);
    }

    private VisitResponseDto convertToDto(VisitRecord visitRecord) {
        VisitResponseDto dto = new VisitResponseDto();
        BeanUtils.copyProperties(visitRecord, dto);
        dto.setSourceText(getSourceText(visitRecord.getSource()));
        return dto;
    }

    private String getSourceText(Integer source) {
        return source == 0 ? "预约到院" : "直接登记";
    }
}




