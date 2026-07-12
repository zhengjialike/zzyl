package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Bill;

import java.util.List;
import java.util.Map;

public interface BillService extends IService<Bill> {
    Map<String, Object> queryByCheckOutId(Integer checkOutId);
    List<Bill> queryByElderId(Integer elderId);
}