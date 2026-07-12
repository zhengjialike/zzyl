package com.soft.service;

import com.soft.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.Nursing.OrderQueryDto;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_order(订单表)】的数据库操作Service
* @createDate 2026-07-12 12:52:34
*/
public interface OrderService extends IService<Order> {
    /**
     * 分页查询订单列表
     */
    Map<String, Object> queryOrderList(OrderQueryDto dto);

    /**
     * 取消订单
     */
    Map<String, Object> cancelOrder(Integer orderId, String reason);

    /**
     * 申请退款
     */
    Map<String, Object> refundOrder(Integer orderId, String reason);

    /**
     * 获取订单详情
     */
    Map<String, Object> getOrderDetail(Integer orderId);

}
