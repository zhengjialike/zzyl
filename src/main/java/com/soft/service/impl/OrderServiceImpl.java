package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.soft.dto.Nursing.OrderQueryDto;
import com.soft.mapper.OrderMapper;
import com.soft.pojo.*;
import com.soft.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NursingItemService nursingItemService;

    @Autowired
    private BedService bedService;

    @Lazy
    @Autowired
    private com.soft.service.RefundService refundService;

    @Autowired
    private com.soft.mapper.NursingTaskMapper nursingTaskMapper;

    @Override
    public Map<String, Object> queryOrderList(OrderQueryDto dto) {
        Map<String, Object> result = new HashMap<>();

        try {
            QueryWrapper<Order> wrapper = new QueryWrapper<>();

            // 订单编号模糊查询
            if (dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
                wrapper.like("order_no", dto.getOrderNo());
            }

            // 按状态筛选（Tab切换）
            if (dto.getStatus() != null) {
                wrapper.eq("status", dto.getStatus());
            }

            // 创建时间范围查询
            if (dto.getStartTime() != null) {
                wrapper.ge("create_time", dto.getStartTime());
            }
            if (dto.getEndTime() != null) {
                wrapper.le("create_time", dto.getEndTime());
            }

            wrapper.orderByDesc("create_time");

            Page<Order> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            Page<Order> orderPage = this.page(page, wrapper);

            List<Map<String, Object>> orderList = orderPage.getRecords().stream().map(order -> {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("orderNo", order.getOrderNo());
                orderMap.put("elderlyName", getElderlyName(order.getElderlyId()));
                orderMap.put("bedNumber", getBedNumber(order.getElderlyId()));
                orderMap.put("nursingItemName", getNursingItemName(order.getNursingItemId()));
                orderMap.put("orderAmount", order.getOrderAmount());
                orderMap.put("expectedServiceTime", formatDate(order.getExpectedServiceTime()));
                orderMap.put("customerName", getCustomerName(order.getCustomerId()));
                orderMap.put("createTime", formatDate(order.getCreateTime()));
                orderMap.put("status", order.getStatus());
                orderMap.put("statusText", getStatusText(order.getStatus()));
                orderMap.put("transactionStatus", getTransactionStatus(order));

                return orderMap;
            }).collect(Collectors.toList());

            result.put("code", 200);
            result.put("data", orderList);
            result.put("total", orderPage.getTotal());
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询订单失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> cancelOrder(Integer orderId, String reason) {
        Map<String, Object> result = new HashMap<>();

        try {
            Order order = this.getById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("msg", "订单不存在");
                return result;
            }

            if (order.getStatus() != 0 && order.getStatus() != 1) {
                result.put("code", 400);
                result.put("msg", "当前订单状态不允许取消");
                return result;
            }

            order.setStatus(5);
            order.setCancelReason(reason);
            this.updateById(order);

            // 联动取消该订单对应的护理任务
            QueryWrapper<NursingTask> taskWrapper = new QueryWrapper<>();
            taskWrapper.eq("order_id", orderId).eq("status", 0);
            List<NursingTask> tasks = nursingTaskMapper.selectList(taskWrapper);
            for (NursingTask task : tasks) {
                task.setStatus(2);
                task.setCancelerId(1);
                task.setCancelTime(LocalDateTime.now());
                task.setCancelReason("对方已经取消" + (reason != null ? reason : ""));
                task.setUpdateTime(LocalDateTime.now());
                nursingTaskMapper.updateById(task);
            }

            result.put("code", 200);
            result.put("msg", "取消订单成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "取消订单失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> refundOrder(Integer orderId, String reason) {
        Map<String, Object> result = new HashMap<>();

        try {
            Order order = this.getById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("msg", "订单不存在");
                return result;
            }

            if (order.getStatus() != 1 && order.getStatus() != 2) {
                result.put("code", 400);
                result.put("msg", "只有待执行和已执行的订单可以申请退款");
                return result;
            }

            Map<String, Object> refundResult = refundService.createRefundRecord(orderId, reason);
            
            Integer refundCode = (Integer) refundResult.get("code");
            if (refundCode != 200) {
                result.put("code", refundCode);
                result.put("msg", refundResult.get("msg"));
                return result;
            }

            result.put("code", 200);
            result.put("msg", "退款申请已提交，等待处理");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "申请退款失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> getOrderDetail(Integer orderId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Order order = this.getById(orderId);
            if (order == null) {
                result.put("code", 404);
                result.put("msg", "订单不存在");
                return result;
            }

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("elderlyName", getElderlyName(order.getElderlyId()));
            orderMap.put("bedNumber", getBedNumber(order.getElderlyId()));
            orderMap.put("nursingItemName", getNursingItemName(order.getNursingItemId()));
            orderMap.put("orderAmount", order.getOrderAmount());
            orderMap.put("expectedServiceTime", formatDate(order.getExpectedServiceTime()));
            orderMap.put("customerName", getCustomerName(order.getCustomerId()));
            orderMap.put("customerPhone", getCustomerPhone(order.getCustomerId()));
            orderMap.put("createTime", formatDate(order.getCreateTime()));
            orderMap.put("status", order.getStatus());
            orderMap.put("statusText", getStatusText(order.getStatus()));
            orderMap.put("paidAt", formatDate(order.getPaidAt()));
            orderMap.put("cancelReason", order.getCancelReason());

            result.put("code", 200);
            result.put("data", orderMap);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取订单详情失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // ==================== 辅助方法 ====================

    private String getElderlyName(Integer elderlyId) {
        if (elderlyId == null) return "-";
        Elderly elderly = elderlyService.getById(elderlyId);
        return elderly != null ? elderly.getRealName() : "-";
    }

    private String getBedNumber(Integer elderlyId) {
        if (elderlyId == null) return "-";
        // 通过老人ID查找床位
        List<Bed> beds = bedService.list();
        for (Bed bed : beds) {
            if (elderlyId.equals(bed.getElderlyId())) {
                return bed.getBedNumber();
            }
        }
        return "-";
    }

    private String getNursingItemName(Integer nursingItemId) {
        if (nursingItemId == null) return "-";
        NursingItem item = nursingItemService.getById(nursingItemId);
        return item != null ? item.getItemname() : "-";
    }

    private String getCustomerName(Integer customerId) {
        if (customerId == null) return "-";
        Customer customer = customerService.getById(customerId);
        return customer != null ? customer.getRealName(): "-";
    }

    private String getCustomerPhone(Integer customerId) {
        if (customerId == null) return "-";
        Customer customer = customerService.getById(customerId);
        return customer != null ? customer.getPhone() : "-";
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "待执行";
            case 2: return "已执行";
            case 3: return "已完成";
            case 4: return "已退款";
            case 5: return "已关闭";
            default: return "-";
        }
    }

    private String getTransactionStatus(Order order) {
        if (order.getStatus() == 5) {
            return "已关闭";
        }
        if (order.getStatus() == 4) {
            return "已退款";
        }
        if (order.getStatus() == 0) {
            return "待支付";
        }
        // 待执行(1)、已执行(2)、已完成(3) 都说明已经支付过了
        return "已支付";
    }

    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "-";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}



