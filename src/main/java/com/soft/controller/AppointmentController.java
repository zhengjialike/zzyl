package com.soft.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.dto.AppointmentQueryDto;
import com.soft.dto.AppointmentResponseDto;
import com.soft.dto.ArrivalConfirmDto;
import com.soft.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/list")
    public Map<String, Object> queryAppointments(@RequestBody AppointmentQueryDto queryDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<AppointmentResponseDto> page = appointmentService.queryAppointments(queryDto);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", page.getRecords());
            result.put("total", page.getTotal());
            result.put("pageNum", page.getCurrent());
            result.put("pageSize", page.getSize());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/confirmArrival")
    public Map<String, Object> confirmArrival(@RequestBody ArrivalConfirmDto confirmDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = appointmentService.confirmArrival(confirmDto);
            if (success) {
                result.put("code", 200);
                result.put("msg", "确认到院成功");
            } else {
                result.put("code", 400);
                result.put("msg", "确认到院失败，预约记录不存在或状态异常");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "确认到院失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/cancel/{id}")
    public Map<String, Object> cancelAppointment(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = appointmentService.cancelAppointment(id);
            if (success) {
                result.put("code", 200);
                result.put("msg", "取消预约成功");
            } else {
                result.put("code", 400);
                result.put("msg", "取消预约失败，预约记录不存在或状态异常");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "取消预约失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/detail/{id}")
    public Map<String, Object> getAppointmentDetail(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            var appointment = appointmentService.getAppointmentById(id);
            if (appointment != null) {
                result.put("code", 200);
                result.put("msg", "查询成功");
                result.put("data", appointment);
            } else {
                result.put("code", 400);
                result.put("msg", "预约记录不存在");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}