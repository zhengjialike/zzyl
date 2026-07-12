package com.soft.controller.RoomEquipment;

import com.soft.dto.RoomEquipment.AlertRecordQueryDto;
import com.soft.service.AlertRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alertRecord")
public class AlertRecordController {

    @Autowired
    private AlertRecordService alertRecordService;

    /**
     * 报警记录分页查询
     */
    @PostMapping("/alertRecordPage")
    public Map<String, Object> alertRecordPageList(@RequestBody AlertRecordQueryDto dto) {
        return alertRecordService.queryAlertRecordList(dto);
    }

    /**
     * 处理报警记录
     */
    @PostMapping("/handleAlertRecord")
    public Map<String, Object> handleAlertRecord(
            @RequestParam("id") Integer id,
            @RequestParam("handleResult") String handleResult,
            HttpSession session) {

        // 获取当前登录用户作为处理人
        String handler = getCurrentUser(session);

        return alertRecordService.handleAlertRecord(id, handleResult, handler);
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUser(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online != null) {
            com.soft.dto.UserLineDto user = (com.soft.dto.UserLineDto) online;
            return user.getUname();
        }
        return "系统"; // 默认值
    }
}