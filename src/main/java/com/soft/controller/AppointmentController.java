package com.soft.controller;

import com.soft.dto.AppointmentRecordDto;
import com.soft.dto.VisitDto;
import com.soft.pojo.Appointment;
import com.soft.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    //定义接口实现用户预约登记
    @RequestMapping("/userApp")
    public Map<String,Object> userAppointment(
            @RequestBody Appointment appointment){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","预约登记失败......");

        appointment.setCreatetime(new Date());
        appointment.setCreateuser("马云");
        appointment.setIslock("待上门");

        appointmentService.save(appointment);
        result.put("code",200);
        result.put("msg","预约登记成功......");
        return result;
    }

    /*查询某次预约详细信息*/
    @RequestMapping("/loadAppInfo")
    public Appointment loadAppInfo(
            @RequestParam(name="id") Integer id){
        Appointment appointment = appointmentService.getById(id);
        return appointment;
    }

    //定义接口实现预约信息的取消
    @RequestMapping("/modifyStatus")
    public Map<String,Object> cancelApp(
            @RequestParam(name="id") Integer id
            ,@RequestParam(name="status") String status){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","状态更新失败......");
        Appointment appointment=new Appointment();
        appointment.setId(id);
        appointment.setIslock(status);
        appointmentService.updateById(appointment);
        result.put("code",200);
        result.put("msg","状态更新成功......");
        return result;

    }

    /*定义预约单信息分页查询*/
    @RequestMapping("/appPageList")
    public Map<String,Object> appPageList(@RequestBody AppointmentRecordDto dto){
        return appointmentService.queryAppointmentListService(dto);
    }

    /*定义记录到访时间接口,更新到访时间*/
    @RequestMapping("/updateVisittime")
    public Map<String,Object> updateVisittime(@RequestBody VisitDto dto){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","更新到访时间失败......");
        Appointment app=new Appointment();
        app.setId(dto.getId());
        app.setVisittime(dto.getVisittime());
        app.setIslock("已到院");
        appointmentService.updateById(app);
        result.put("code",200);
        result.put("msg","更新到访时间成功......");
        return result;
    }
}
