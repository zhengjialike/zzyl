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
    Map<String, Object> queryRefundList(RefundQueryDto dto);

    Map<String, Object> getRefundDetail(Integer refundId);

    Map<String, Object> createRefundRecord(Integer orderId, String reason);

    Map<String, Object> approveRefund(Integer refundId);

}
