package com.soft.service;

import com.soft.dto.RoomEquipment.AlertRecordQueryDto;
import com.soft.pojo.AlertRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_alert_record(报警记录表)】的数据库操作Service
* @createDate 2026-07-12 01:04:20
*/
public interface AlertRecordService extends IService<AlertRecord> {
    /**
     * 报警记录分页查询
     */
    Map<String, Object> queryAlertRecordList(AlertRecordQueryDto dto);

    /**
     * 处理报警记录
     */
    Map<String, Object> handleAlertRecord(Integer id, String handleResult, String handler);

}
