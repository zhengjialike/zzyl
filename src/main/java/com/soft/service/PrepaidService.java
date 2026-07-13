package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.common.PageResult;
import com.soft.entity.Prepaid;

public interface PrepaidService extends IService<Prepaid> {
    PageResult<Prepaid> findPage(int pageNum, int pageSize, Integer elderlyId, String bedNo);
    void recharge(Prepaid prepaid, Long userId);
}
