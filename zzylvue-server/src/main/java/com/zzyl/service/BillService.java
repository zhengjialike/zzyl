package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Bill;

public interface BillService extends IService<Bill> {
    PageResult<Bill> findPage(int pageNum, int pageSize, String billNo, String elderName, String elderIdCard, String status, String billType);
    void generateMonthlyBill(Long elderId, String billMonth);
    void pay(Long id, String paymentMethod, String paymentVoucher, String paymentRemark);
    void cancel(Long id, String cancelReason);
}
