package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BillMapper;
import com.soft.pojo.Bill;
import com.soft.pojo.CheckOut;
import com.soft.service.BillService;
import com.soft.service.CheckOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Lazy
    @Autowired
    private CheckOutService checkOutService;

    @Override
    public Map<String, Object> queryByCheckOutId(Integer checkOutId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        
        // 先获取退住申请信息，得到老人ID
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
        
        // 通过老人ID查询账单
        wrapper.eq("elderly_id", checkOut.getElderId()).orderByDesc("create_time");
        List<Bill> bills = this.baseMapper.selectList(wrapper);
        
        Map<String, Object> result = new HashMap<>();
        
        // 根据账单状态和类型进行分类
        // status: 0-待支付, 1-已支付, 2-部分支付, 3-已取消
        // bill_type: 入住费, 护理费, 餐费等
        
        // 应退：已支付的账单（需要退款）
        result.put("shouldRefund", bills.stream()
            .filter(b -> b.getStatus() != null && b.getStatus() == 1)
            .collect(Collectors.toList()));
        
        // 欠费：待支付的账单
        result.put("arrears", bills.stream()
            .filter(b -> b.getStatus() != null && b.getStatus() == 0)
            .collect(Collectors.toList()));
        
        // 余额：有预充值或押金的记录（这里简化处理，实际应该有专门的余额表）
        result.put("balance", List.of());
        
        // 未缴：同欠费
        result.put("unpaid", bills.stream()
            .filter(b -> b.getStatus() != null && b.getStatus() == 0)
            .collect(Collectors.toList()));
        
        result.put("all", bills);
        return result;
    }

    @Override
    public List<Bill> queryByElderId(Integer elderId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("elderly_id", elderId).orderByDesc("create_time");
        return this.baseMapper.selectList(wrapper);
    }
}