package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Bill;
import com.zzyl.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/page")
    public Result<PageResult<Bill>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String billNo = (String) params.get("billNo");
        String elderName = (String) params.get("elderName");
        String elderIdCard = (String) params.get("elderIdCard");
        String status = (String) params.get("status");
        String billType = (String) params.get("billType");
        return Result.success(billService.findPage(pageNum, pageSize, billNo, elderName, elderIdCard, status, billType));
    }

    @GetMapping("/{id}")
    public Result<Bill> getById(@PathVariable Long id) {
        return Result.success(billService.getById(id));
    }

    @PostMapping("/generateMonthly")
    public Result<Void> generateMonthly(@RequestBody Map<String, Object> params) {
        Long elderId = Long.valueOf(params.get("elderId").toString());
        String billMonth = (String) params.get("billMonth");
        billService.generateMonthlyBill(elderId, billMonth);
        return Result.success();
    }

    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String paymentMethod = (String) params.get("paymentMethod");
        String paymentVoucher = (String) params.get("paymentVoucher");
        String paymentRemark = (String) params.get("paymentRemark");
        billService.pay(id, paymentMethod, paymentVoucher, paymentRemark);
        return Result.success();
    }

    @PostMapping("/cancel")
    public Result<Void> cancel(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String cancelReason = (String) params.get("cancelReason");
        billService.cancel(id, cancelReason);
        return Result.success();
    }
}
