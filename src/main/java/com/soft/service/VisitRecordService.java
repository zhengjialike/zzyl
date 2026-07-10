package com.soft.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.VisitQueryDto;
import com.soft.dto.VisitRegisterDto;
import com.soft.dto.VisitResponseDto;
import com.soft.pojo.VisitRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12
* @description 针对表【t_visit_record(来访记录表)】的数据库操作Service
* @createDate 2026-07-09 23:24:22
*/
public interface VisitRecordService extends IService<VisitRecord> {
    Page<VisitResponseDto> queryVisits(VisitQueryDto queryDto);

    boolean registerDirectVisit(VisitRegisterDto registerDto, String creator);

    boolean createVisitFromAppointment(Integer appointmentId, String visitType,
                                       String visitorName, String visitorPhone,
                                       String elderName, java.time.LocalDateTime arrivalTime,
                                       String creator);

    VisitRecord getVisitById(Integer id);

}
