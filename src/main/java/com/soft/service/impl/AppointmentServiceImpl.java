package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.AppointmentRecordDto;
import com.soft.pojo.Appointment;
import com.soft.service.AppointmentService;
import com.soft.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teacher
 * @description 针对表【t_appointment】的数据库操作Service实现
 * @createDate 2026-07-11 09:36:45
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment>
        implements AppointmentService{

    @Autowired
    private AppointmentMapper appointmentMapper;
    @Override
    public Map<String, Object> queryAppointmentListService(AppointmentRecordDto dto) {
        Page<Appointment> page=new Page<>(dto.getPageNum(),dto.getPageSize());
        //创建wrapper封装where条件
        QueryWrapper<Appointment> wrapper=new QueryWrapper<>();
        String appuser = dto.getAppuser();
        String phone = dto.getPhone();
        String islock = dto.getIslock();
        Timestamp starttime = dto.getStarttime();
        Timestamp endtime = dto.getEndtime();
        wrapper.eq(!StringUtils.isBlank(appuser),"appuser",appuser);
        wrapper.eq(!StringUtils.isBlank(phone),"phone",phone);
        wrapper.eq(!StringUtils.isBlank(islock),"islock",islock);
        wrapper.between(starttime!=null && endtime!=null
                ,"apptime",starttime,endtime);

        List<Appointment> appointments = appointmentMapper.selectList(page, wrapper);

        //遍历appointments，检查预约是否已经过期
        appointments.forEach(item->{
            //获得系统当前时间
            LocalDateTime now=LocalDateTime.now();
            Date apptime = item.getApptime();
            LocalDateTime appointmentTiem = apptime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            long between = ChronoUnit.HOURS.between(now, appointmentTiem);
            System.out.println("between="+between);
            if(between<=1 && item.getIslock().equals("待上门")){
                //执行update操作
                Appointment app=new Appointment();
                app.setId(item.getId());
                app.setIslock("已过期");
                appointmentMapper.updateById(app);
                item.setIslock("已过期");
            }

        });
        Map<String, Object> result=new HashMap<>();
        result.put("total",page.getTotal());
        result.put("appointments",appointments);



        return result;
    }
}




