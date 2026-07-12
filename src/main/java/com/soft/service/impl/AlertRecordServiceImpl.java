package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.RoomEquipment.AlertRecordQueryDto;
import com.soft.mapper.AlertRecordMapper;
import com.soft.pojo.AlertRecord;
import com.soft.pojo.AlertRule;
import com.soft.pojo.Device;
import com.soft.pojo.Product;
import com.soft.service.AlertRecordService;
import com.soft.service.AlertRuleService;
import com.soft.service.DeviceService;
import com.soft.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 12
* @description 针对表【t_alert_record(报警记录表)】的数据库操作Service实现
* @createDate 2026-07-12 01:04:20
*/
@Service
public class AlertRecordServiceImpl extends ServiceImpl<AlertRecordMapper, AlertRecord>
    implements AlertRecordService{

    @Autowired
    private AlertRecordMapper alertRecordMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AlertRuleService alertRuleService;

    @Override
    public Map<String, Object> queryAlertRecordList(AlertRecordQueryDto dto) {
        // 1. 创建分页对象
        Page<AlertRecord> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<AlertRecord> wrapper = new QueryWrapper<>();

        // 3. 添加查询条件
        if (StringUtils.hasText(dto.getDeviceName())) {
            wrapper.like("device_id", dto.getDeviceName());
        }
        if (dto.getHandleStatus() != null) {
            wrapper.eq("handle_status", dto.getHandleStatus());
        }
        if (dto.getAlertStartTime() != null && dto.getAlertEndTime() != null) {
            wrapper.between("alert_time", dto.getAlertStartTime(), dto.getAlertEndTime());
        }

        // 4. 按报警时间倒序排列
        wrapper.orderByDesc("alert_time");

        // 5. 执行分页查询
        IPage<AlertRecord> iPage = alertRecordMapper.selectPage(page, wrapper);
        List<AlertRecord> alertRecords = iPage.getRecords();

        // 6. 封装结果，添加设备名称、产品名称、功能名称、位置信息等
        List<Map<String, Object>> alertRecordList = alertRecords.stream().map(alertRecord -> {
            Map<String, Object> alertRecordMap = new HashMap<>();
            alertRecordMap.put("id", alertRecord.getId());
            alertRecordMap.put("deviceId", alertRecord.getDeviceId());
            
            // 获取设备信息
            Device device = getDeviceById(alertRecord.getDeviceId());
            if (device != null) {
                alertRecordMap.put("deviceName", device.getDeviceName());
                alertRecordMap.put("remarkName", device.getRemarkName());
                alertRecordMap.put("productId", device.getProductId());
                
                // 获取产品名称
                String productName = getProductName(device.getProductId());
                alertRecordMap.put("productName", productName);
                
                // 从设备表获取位置信息
                String locationName = getLocationName(device.getLocationType(), device.getLocationId());
                alertRecordMap.put("locationName", locationName);
            } else {
                alertRecordMap.put("deviceName", "-");
                alertRecordMap.put("remarkName", "-");
                alertRecordMap.put("productName", "-");
                alertRecordMap.put("locationName", "-");
            }
            
            // 获取规则信息，得到功能名称
            AlertRule alertRule = getAlertRuleById(alertRecord.getRuleId());
            if (alertRule != null) {
                alertRecordMap.put("functionName", alertRule.getFunctionName());
            } else {
                alertRecordMap.put("functionName", "-");
            }
            
            alertRecordMap.put("dataValue", alertRecord.getDataValue());
            alertRecordMap.put("alertTime", alertRecord.getAlertTime());
            alertRecordMap.put("handleStatus", alertRecord.getHandleStatus());
            alertRecordMap.put("handleResult", alertRecord.getHandleResult());
            alertRecordMap.put("handler", alertRecord.getHandler());
            alertRecordMap.put("handleTime", alertRecord.getHandleTime());
            alertRecordMap.put("createTime", alertRecord.getCreateTime());

            return alertRecordMap;
        }).collect(Collectors.toList());

        // 7. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("total", iPage.getTotal());
        result.put("alertRecords", alertRecordList);
        return result;
    }

    @Override
    public Map<String, Object> handleAlertRecord(Integer id, String handleResult, String handler) {
        Map<String, Object> result = new HashMap<>();
        
        if (id == null) {
            result.put("code", 400);
            result.put("msg", "报警记录ID不能为空");
            return result;
        }
        
        if (!StringUtils.hasText(handleResult)) {
            result.put("code", 400);
            result.put("msg", "处理结果不能为空");
            return result;
        }
        
        if (!StringUtils.hasText(handler)) {
            result.put("code", 400);
            result.put("msg", "处理人不能为空");
            return result;
        }
        
        try {
            AlertRecord alertRecord = this.getById(id);
            if (alertRecord == null) {
                result.put("code", 404);
                result.put("msg", "报警记录不存在");
                return result;
            }
            
            // 更新处理信息
            alertRecord.setHandleStatus(1); // 已处理
            alertRecord.setHandleResult(handleResult);
            alertRecord.setHandler(handler);
            alertRecord.setHandleTime(new Date());
            
            this.updateById(alertRecord);
            
            result.put("code", 200);
            result.put("msg", "处理成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "处理失败：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 根据设备ID获取设备信息
     */
    private Device getDeviceById(Integer deviceId) {
        if (deviceId == null) {
            return null;
        }
        try {
            return deviceService.getById(deviceId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据产品ID获取产品名称
     */
    private String getProductName(Integer productId) {
        if (productId == null) {
            return "-";
        }
        try {
            Product product = productService.getById(productId);
            return product != null ? product.getProductName() : "-";
        } catch (Exception e) {
            return "-";
        }
    }

    /**
     * 根据规则ID获取报警规则
     */
    private AlertRule getAlertRuleById(Integer ruleId) {
        if (ruleId == null) {
            return null;
        }
        try {
            return alertRuleService.getById(ruleId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据位置类型获取位置名称
     */
    private String getLocationName(Integer locationType, Integer locationId) {
        if (locationId == null) {
            return "-";
        }

        try {
            switch (locationType) {
                case 1: // 房间
                    return locationId + "房间";
                case 2: // 床位
                    return locationId + "床位";
                case 3: // 老人
                    return "老人-" + locationId;
                default:
                    return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }
}




