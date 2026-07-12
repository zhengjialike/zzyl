package com.soft.dto.Nursing;

import lombok.Data;
import java.util.Date;

@Data
public class OrderQueryDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderNo; // 订单编号
    private String elderlyName; // 老人姓名
    private String customerName; // 下单人
    private Date startTime; // 开始时间
    private Date endTime; // 结束时间
    private Integer status; // 订单状态（用于Tab筛选）
}