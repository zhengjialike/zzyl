package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.common.PageResult;
import com.soft.pojo.Bill;
import java.util.List;
import java.util.Map;

public interface BillService extends IService<Bill> {
    PageResult<Bill> findPage(int pageNum, int pageSize, String billNo, Integer elderlyId, Integer status, String billType);
    void generateMonthlyBill(Integer elderlyId, String billMonth);
    void pay(Long id, String paymentMethod, String paymentVoucher, String paymentRemark);
    void cancel(Long id, String cancelReason);
    Map<String, Object> queryByCheckOutId(Integer checkOutId);
    List<Bill> queryByElderId(Integer elderId);
}
