package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.soft.pojo.Bill;
import com.zzyl.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired private BillService billService;

    @PostMapping("/page")
    public Result<PageResult<Bill>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String billNo = (String) params.get("billNo");
        Integer elderlyId = params.get("elderlyId") != null ? Integer.valueOf(params.get("elderlyId").toString()) : null;
        Integer status = params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null;
        String billType = (String) params.get("billType");
        return Result.success(billService.findPage(pageNum, pageSize, billNo, elderlyId, status, billType));
    }

    @GetMapping("/{id}") public Result<Bill> getById(@PathVariable Long id) { return Result.success(billService.getById(id)); }
    @PostMapping("/generateMonthly") public Result<Void> generateMonthly(@RequestBody Map<String, Object> params) { billService.generateMonthlyBill(Integer.valueOf(params.get("elderlyId").toString()), (String)params.get("billMonth")); return Result.success(); }
    @PostMapping("/pay") public Result<Void> pay(@RequestBody Map<String, Object> params) { billService.pay(Long.valueOf(params.get("id").toString()), (String)params.get("paymentMethod"), (String)params.get("paymentVoucher"), (String)params.get("paymentRemark")); return Result.success(); }
    @PostMapping("/cancel") public Result<Void> cancel(@RequestBody Map<String, Object> params) { billService.cancel(Long.valueOf(params.get("id").toString()), (String)params.get("cancelReason")); return Result.success(); }
}
