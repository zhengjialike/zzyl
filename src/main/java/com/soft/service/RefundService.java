package com.soft.service;

import com.soft.dto.Nursing.RefundQueryDto;
import com.soft.pojo.Refund;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_refund(退款记录表)】的数据库操作Service
* @createDate 2026-07-12 14:23:05
*/
public interface RefundService extends IService<Refund> {
    /**
     * 分页查询退款记录列表
     */
    Map<String, Object> queryRefundList(RefundQueryDto dto);

    /**
     * 获取退款详情
     */
    Map<String, Object> getRefundDetail(Long refundId);

    /**
     * 创建退款记录（从订单申请退款时调用）
     */
    Map<String, Object> createRefundRecord(Long orderId, String reason);

}
