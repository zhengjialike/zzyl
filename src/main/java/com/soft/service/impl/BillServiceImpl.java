package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BillMapper;
import com.soft.pojo.Bill;
import com.soft.service.BillService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Override
    public Map<String, Object> queryByCheckOutId(Integer checkOutId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("check_out_id", checkOutId).orderByDesc("create_time");
        List<Bill> bills = this.baseMapper.selectList(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("shouldRefund", bills.stream().filter(b -> "应退".equals(b.getPayCategory())).toArray());
        result.put("arrears", bills.stream().filter(b -> "欠费".equals(b.getPayCategory())).toArray());
        result.put("balance", bills.stream().filter(b -> "余额".equals(b.getPayCategory())).toArray());
        result.put("unpaid", bills.stream().filter(b -> "未缴".equals(b.getPayCategory())).toArray());
        result.put("all", bills);
        return result;
    }

    @Override
    public List<Bill> queryByElderId(Integer elderId) {
        QueryWrapper<Bill> wrapper = new QueryWrapper<>();
        wrapper.eq("elder_id", elderId).orderByDesc("create_time");
        return this.baseMapper.selectList(wrapper);
    }
}