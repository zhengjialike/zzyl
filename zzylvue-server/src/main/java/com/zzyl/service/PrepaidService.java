package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Prepaid;

public interface PrepaidService extends IService<Prepaid> {
    PageResult<Prepaid> findPage(int pageNum, int pageSize, String elderName, String bedNo);
    void recharge(Prepaid prepaid, Long userId);
}
