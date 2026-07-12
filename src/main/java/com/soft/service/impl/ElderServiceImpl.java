package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ElderMapper;
import com.soft.pojo.Elder;
import com.soft.service.ElderService;
import org.springframework.stereotype.Service;

@Service
public class ElderServiceImpl extends ServiceImpl<ElderMapper, Elder> implements ElderService {

    @Override
    public Elder queryByIdCard(String idCard) {
        QueryWrapper<Elder> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard);
        return elderMapper().selectOne(wrapper);
    }

    private ElderMapper elderMapper() {
        return this.baseMapper;
    }
}