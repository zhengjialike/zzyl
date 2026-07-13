package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.ElderBalance;
import com.zzyl.mapper.ElderBalanceMapper;
import com.zzyl.service.ElderBalanceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ElderBalanceServiceImpl extends ServiceImpl<ElderBalanceMapper, ElderBalance> implements ElderBalanceService {

    @Override
    public PageResult<ElderBalance> findPage(int pageNum, int pageSize, Integer elderlyId, String bedNo) {
        Page<ElderBalance> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ElderBalance> wrapper = new LambdaQueryWrapper<>();
        if (elderlyId != null) { wrapper.eq(ElderBalance::getElderlyId, elderlyId); }
        if (StringUtils.hasText(bedNo)) { wrapper.like(ElderBalance::getBedNo, bedNo); }
        wrapper.orderByDesc(ElderBalance::getChangeTime);
        IPage<ElderBalance> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }
}
