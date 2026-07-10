package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Appointment;
import com.soft.service.AppointmentService;
import com.soft.mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

/**
* @description 针对表【t_appointment】的数据库操作Service实现
*/
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
        implements AppointmentService {

}
