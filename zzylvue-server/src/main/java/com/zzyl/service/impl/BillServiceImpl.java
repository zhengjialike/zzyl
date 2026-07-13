package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Bill;
import com.zzyl.mapper.BillMapper;
import com.zzyl.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Override
    public PageResult<Bill> findPage(int pageNum, int pageSize, String billNo, String elderName, String elderIdCard, String status, String billType) {
        Page<Bill> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Bill> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(billNo)) {
            wrapper.eq(Bill::getBillNo, billNo);
        }
        if (StringUtils.hasText(elderName)) {
            wrapper.like(Bill::getElderName, elderName);
        }
        if (StringUtils.hasText(elderIdCard)) {
            wrapper.eq(Bill::getElderIdCard, elderIdCard);
        }
        if (StringUtils.hasText(status) && !"全部".equals(status)) {
            wrapper.eq(Bill::getStatus, status);
        }
        if (StringUtils.hasText(billType)) {
            wrapper.eq(Bill::getBillType, billType);
        }
        wrapper.orderByDesc(Bill::getCreateTime);
        IPage<Bill> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    @Transactional
    public void generateMonthlyBill(Long elderId, String billMonth) {
        // Simplified: create a new bill record
        Bill bill = new Bill();
        String billNo = "ZD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        bill.setBillNo(billNo);
        bill.setBillMonth(billMonth);
        bill.setStatus(1);
        bill.setBillType("月度账单");
        baseMapper.insert(bill);
    }

    @Override
    @Transactional
    public void pay(Long id, String paymentMethod, String paymentVoucher, String paymentRemark) {
        Bill bill = baseMapper.selectById(id);
        if (bill == null) return;
        bill.setStatus(2);
        bill.setPaymentMethod(paymentMethod);
        bill.setPaymentVoucher(paymentVoucher);
        bill.setPaymentRemark(paymentRemark);
        baseMapper.updateById(bill);
    }

    @Override
    @Transactional
    public void cancel(Long id, String cancelReason) {
        Bill bill = baseMapper.selectById(id);
        if (bill == null) return;
        bill.setStatus(3);
        bill.setCancelReason(cancelReason);
        baseMapper.updateById(bill);
    }
}
