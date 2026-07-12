package com.soft.controller;

import com.soft.dto.Nursing.OrderQueryDto;
import com.soft.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     */
    @PostMapping("/pageList")
    public Map<String, Object> pageList(@RequestBody OrderQueryDto dto) {
        return orderService.queryOrderList(dto);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestBody Map<String, Object> params) {
        Long orderId = Long.valueOf(params.get("orderId").toString());
        String reason = params.get("reason") != null ? params.get("reason").toString() : null;
        return orderService.cancelOrder(orderId, reason);
    }

    /**
     * 申请退款
     */
    @PostMapping("/refund")
    public Map<String, Object> refund(@RequestBody Map<String, Object> params) {
        Long orderId = Long.valueOf(params.get("orderId").toString());
        String reason = params.get("reason") != null ? params.get("reason").toString() : null;
        return orderService.refundOrder(orderId, reason);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/getDetail")
    public Map<String, Object> getDetail(@RequestParam("orderId") Long orderId) {
        return orderService.getOrderDetail(orderId);
    }
}