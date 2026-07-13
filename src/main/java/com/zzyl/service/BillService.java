package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.soft.pojo.Bill;
import java.util.Map;

public interface BillService extends IService<Bill> {
    PageResult<Bill> findPage(int pageNum, int pageSize, String billNo, Integer elderlyId, Integer status, String billType);
    void generateMonthlyBill(Integer elderlyId, String billMonth);
    void pay(Long id, String paymentMethod, String paymentVoucher, String paymentRemark);
    void cancel(Long id, String cancelReason);
    default Map<String, Object> queryByCheckOutId(Integer id) { return null; }
}
