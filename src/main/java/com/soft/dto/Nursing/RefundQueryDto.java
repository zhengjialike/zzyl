package com.soft.dto.Nursing;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RefundQueryDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String refundNo; // 退款编号
    private String orderNo; // 订单编号
    private String applicantName; // 申请人
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
    private Integer status; // 退款状态（用于Tab筛选）
}
