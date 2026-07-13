package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.common.PageResult;
import com.soft.pojo.Bill;
import com.soft.pojo.CheckOut;
import com.soft.service.CheckOutService;
import com.soft.mapper.BillMapper;
import com.soft.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Lazy
    @Autowired
    private CheckOutService checkOutService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<Bill> findPage(int pageNum, int pageSize, String billNo, Integer elderlyId, Integer status, String billType) {
        Page<Bill> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Bill> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(billNo)) { wrapper.eq(Bill::getBillNo, billNo); }
        if (elderlyId != null) { wrapper.eq(Bill::getElderlyId, elderlyId); }
        if (status != null) { wrapper.eq(Bill::getStatus, status); }
        if (StringUtils.hasText(billType)) { wrapper.eq(Bill::getBillType, billType); }
        wrapper.orderByDesc(Bill::getCreateTime);
        IPage<Bill> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override @Transactional
    public void generateMonthlyBill(Integer elderlyId, String billMonth) {
        Bill bill = new Bill();
        bill.setBillNo("ZD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        bill.setElderlyId(elderlyId); bill.setBillMonth(billMonth);
        bill.setStatus(1); bill.setBillType("月度账单"); baseMapper.insert(bill);
    }

    @Override @Transactional
    public void pay(Long id, String paymentMethod, String paymentVoucher, String paymentRemark) {
        Bill bill = baseMapper.selectById(id); if (bill == null) return;
        bill.setStatus(2); bill.setPaymentMethod(paymentMethod);
        bill.setPaymentVoucher(paymentVoucher); bill.setPaymentRemark(paymentRemark); baseMapper.updateById(bill);
    }

    @Override @Transactional
    public void cancel(Long id, String cancelReason) {
        Bill bill = baseMapper.selectById(id); if (bill == null) return;
        bill.setStatus(3); bill.setCancelReason(cancelReason); baseMapper.updateById(bill);
    }

    @Override
    public Map<String, Object> queryByCheckOutId(Integer checkOutId) {
        CheckOut checkOut = checkOutService.getById(checkOutId);
        if (checkOut == null || checkOut.getElderId() == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("shouldRefund", List.of());
            result.put("arrears", List.of());
            result.put("balance", List.of());
            result.put("unpaid", List.of());
            result.put("all", List.of());
            return result;
        }

        List<Bill> bills = queryByElderId(checkOut.getElderId());
        Map<String, Object> result = new HashMap<>();
        result.put("shouldRefund", bills.stream()
                .filter(bill -> Integer.valueOf(1).equals(bill.getStatus()))
                .collect(Collectors.toList()));
        result.put("arrears", bills.stream()
                .filter(bill -> Integer.valueOf(0).equals(bill.getStatus()))
                .collect(Collectors.toList()));

        List<Map<String, Object>> balance = jdbcTemplate.queryForList(
                "SELECT deposit_balance AS refundableDeposit, prepaid_balance AS prepaidAmount " +
                        "FROM t_elder_balance WHERE elderly_id = ? AND del_flag = 0 " +
                        "ORDER BY change_time DESC, id DESC LIMIT 1",
                checkOut.getElderId());
        result.put("balance", balance);
        result.put("unpaid", bills.stream()
                .filter(bill -> Integer.valueOf(0).equals(bill.getStatus()))
                .collect(Collectors.toList()));
        result.put("all", bills);
        return result;
    }

    @Override
    public List<Bill> queryByElderId(Integer elderId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("elderly_id", elderId).orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }
}
