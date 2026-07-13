package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.common.PageResult;
import com.soft.entity.ElderBalance;

public interface ElderBalanceService extends IService<ElderBalance> {
    PageResult<ElderBalance> findPage(int pageNum, int pageSize, Integer elderlyId, String bedNo);
}
