package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.soft.dto.Nursing.RefundQueryDto;
import com.soft.mapper.RefundMapper;
import com.soft.pojo.Order;
import com.soft.pojo.Refund;
import com.soft.service.OrderService;
import com.soft.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    @Lazy
    @Autowired
    private OrderService orderService;

    @Override
    public Map<String, Object> queryRefundList(RefundQueryDto dto) {
        Map<String, Object> result = new HashMap<>();

        try {
            QueryWrapper<Refund> wrapper = new QueryWrapper<>();

            // 退款编号模糊查询
            if (dto.getRefundNo() != null && !dto.getRefundNo().trim().isEmpty()) {
                wrapper.like("refund_no", dto.getRefundNo());
            }

            // 按状态筛选（Tab切换）
            if (dto.getStatus() != null) {
                wrapper.eq("status", dto.getStatus());
            }

            // 申请时间范围查询
            if (dto.getStartTime() != null) {
                wrapper.ge("applied_at", dto.getStartTime());
            }
            if (dto.getEndTime() != null) {
                wrapper.le("applied_at", dto.getEndTime());
            }

            wrapper.orderByDesc("applied_at");

            Page<Refund> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            Page<Refund> refundPage = this.page(page, wrapper);

            List<Map<String, Object>> refundList = refundPage.getRecords().stream().map(refund -> {
                Map<String, Object> refundMap = new HashMap<>();
                refundMap.put("id", refund.getId());
                refundMap.put("refundNo", refund.getRefundNo());
                
                // 获取订单编号
                Order order = orderService.getById(refund.getOrderId());
                refundMap.put("orderNo", order != null ? order.getOrderNo() : "-");
                
                refundMap.put("refundAmount", refund.getRefundAmount());
                refundMap.put("applicantName", getCustomerName(refund.getCustomerId()));
                refundMap.put("applyTime", formatDateTime(refund.getAppliedAt()));
                refundMap.put("refundTime", refund.getRefundedAt() != null ? formatDateTime(refund.getRefundedAt()) : "-");
                refundMap.put("orderStatus", getOrderStatusText(refund.getOrderId()));
                refundMap.put("refundStatus", refund.getStatus());
                refundMap.put("refundStatusText", getStatusText(refund.getStatus()));
                
                return refundMap;
            }).collect(Collectors.toList());

            result.put("code", 200);
            result.put("data", refundList);
            result.put("total", refundPage.getTotal());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询退款记录失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> getRefundDetail(Integer refundId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Refund refund = this.getById(refundId);
            if (refund == null) {
                result.put("code", 404);
                result.put("msg", "退款记录不存在");
                return result;
            }

            Order order = orderService.getById(refund.getOrderId());

            Map<String, Object> refundMap = new HashMap<>();
            refundMap.put("id", refund.getId());
            refundMap.put("refundNo", refund.getRefundNo());
            refundMap.put("orderNo", order != null ? order.getOrderNo() : "-");
            refundMap.put("orderStatus", getOrderStatusText(refund.getOrderId()));
            refundMap.put("refundStatus", refund.getStatus());
            refundMap.put("refundStatusText", getStatusText(refund.getStatus()));
            refundMap.put("applicantName", getCustomerName(refund.getCustomerId()));
            refundMap.put("applyTime", formatDateTime(refund.getAppliedAt()));
            refundMap.put("refundReason", refund.getRefundReason());
            refundMap.put("refundChannel", refund.getRefundChannel());
            refundMap.put("refundMethod", refund.getRefundMethod());
            refundMap.put("refundTime", refund.getRefundedAt() != null ? formatDateTime(refund.getRefundedAt()) : "-");
            refundMap.put("refundAmount", refund.getRefundAmount());

            result.put("code", 200);
            result.put("data", refundMap);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取退款详情失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> createRefundRecord(Integer orderId, String reason) {
        Map<String, Object> result = new HashMap<>();

        try {
            Order order = orderService.getById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("msg", "订单不存在");
                return result;
            }

            // 检查是否已存在退款记录
            QueryWrapper<Refund> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id", orderId);
            long count = this.count(wrapper);
            if (count > 0) {
                result.put("code", 400);
                result.put("msg", "该订单已存在退款记录");
                return result;
            }

            Refund refund = new Refund();
            refund.setRefundNo(generateRefundNo());
            refund.setOrderId(orderId);
            refund.setRefundAmount(order.getOrderAmount());
            refund.setCustomerId(order.getCustomerId());
            refund.setAppliedAt(LocalDateTime.now());
            refund.setRefundReason(reason);
            refund.setRefundChannel("原路退回");
            refund.setRefundMethod("微信");
            refund.setStatus(0); // 处理中

            this.save(refund);

            result.put("code", 200);
            result.put("msg", "创建退款记录成功");
            result.put("data", refund);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "创建退款记录失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // ==================== 辅助方法 ====================

    private String generateRefundNo() {
        return "TK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private String getCustomerName(Integer customerId) {
        if (customerId == null) return "-";
        // 这里应该调用CustomerService，但为避免循环依赖，暂时返回默认值
        return "用户-" + customerId;
    }

    private String getOrderStatusText(Integer orderId) {
        if (orderId == null) return "-";
        Order order = orderService.getById(orderId);
        if (order == null) return "-";
        
        switch (order.getStatus()) {
            case 0: return "待支付";
            case 1: return "待执行";
            case 2: return "已执行";
            case 3: return "已完成";
            case 4: return "已退款";
            case 5: return "已关闭";
            default: return "-";
        }
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "退款处理中";
            case 1: return "退款成功";
            case 2: return "退款失败";
            default: return "-";
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}