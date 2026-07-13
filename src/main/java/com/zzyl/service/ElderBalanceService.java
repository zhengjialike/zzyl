package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.zzyl.entity.ElderBalance;

public interface ElderBalanceService extends IService<ElderBalance> {
    PageResult<ElderBalance> findPage(int pageNum, int pageSize, Integer elderlyId, String bedNo);
}
