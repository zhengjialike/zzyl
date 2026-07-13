package com.soft.controller;


import com.soft.dto.Nursing.RefundQueryDto;
import com.soft.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    private RefundService refundService;

    /**
     * 分页查询退款记录列表
     */
    @PostMapping("/pageList")
    public Map<String, Object> pageList(@RequestBody RefundQueryDto dto) {
        return refundService.queryRefundList(dto);
    }

    /**
     * 获取退款详情
     */
    @GetMapping("/getDetail")
    public Map<String, Object> getDetail(@RequestParam("refundId") Integer refundId) {
        return refundService.getRefundDetail(refundId);
    }

    /**
     * 创建退款记录
     */
    @PostMapping("/create")
    public Map<String, Object> create(@RequestParam("orderId") Integer orderId,
                                     @RequestParam(value = "reason", required = false) String reason) {
        return refundService.createRefundRecord(orderId, reason);
    }

    @PostMapping("/approve")
    public Map<String, Object> approve(@RequestBody Map<String, Integer> params) {
        Integer refundId = params.get("refundId");
        return refundService.approveRefund(refundId);
    }
}