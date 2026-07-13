package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Prepaid;
import com.zzyl.mapper.PrepaidMapper;
import com.zzyl.service.PrepaidService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PrepaidServiceImpl extends ServiceImpl<PrepaidMapper, Prepaid> implements PrepaidService {

    @Override
    public PageResult<Prepaid> findPage(int pageNum, int pageSize, String elderName, String bedNo) {
        Page<Prepaid> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Prepaid> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(elderName)) {
            wrapper.like(Prepaid::getElderName, elderName);
        }
        if (StringUtils.hasText(bedNo)) {
            wrapper.like(Prepaid::getBedNo, bedNo);
        }
        wrapper.orderByDesc(Prepaid::getCreateTime);
        IPage<Prepaid> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    @Transactional
    public void recharge(Prepaid prepaid, Long userId) {
        String prepaidNo = "YJ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        prepaid.setPrepaidNo(prepaidNo);
        baseMapper.insert(prepaid);
    }
}
