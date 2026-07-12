package com.soft.dto.RoomEquipment;

import lombok.Data;

@Data
public class AlertRuleQueryDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    /**
     * 报警规则名称搜索
     */
    private String ruleName;

    /**
     * 所属产品ID筛选
     */
    private Integer productId;

    /**
     * 功能名称搜索
     */
    private String functionName;

    /**
     * 状态筛选（0-禁用，1-启用）
     */
    private Integer status;
}