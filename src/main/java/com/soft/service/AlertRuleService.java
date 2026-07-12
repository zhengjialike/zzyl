package com.soft.service;

import com.soft.dto.RoomEquipment.AlertRuleQueryDto;
import com.soft.pojo.AlertRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_alert_rule(报警规则表)】的数据库操作Service
* @createDate 2026-07-12 00:16:01
*/
public interface AlertRuleService extends IService<AlertRule> {

    /**
     * 报警规则分页查询
     */
    Map<String, Object> queryAlertRuleList(AlertRuleQueryDto dto);

    /**
     * 检查报警规则名称是否已存在
     */
    boolean isRuleNameExists(String ruleName, Integer excludeId);

    /**
     * 获取所有产品列表（用于下拉框）
     */
    Map<String, Object> getAllProducts();

    /**
     * 根据产品ID获取功能名称列表（从functions字段解析）
     */
    Map<String, Object> getFunctionNamesByProductId(Integer productId);

    /**
     * 获取关联设备列表（用于下拉框）
     */
    Map<String, Object> getAssociatedDevices(Integer productId);

}
