package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.ElderlyMapper;
import com.soft.pojo.Elderly;
import com.soft.service.ElderlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElderlyServiceImpl extends ServiceImpl<ElderlyMapper, Elderly>
    implements ElderlyService{
    
    @Autowired 
    private ElderlyMapper elderlyMapper;
    
    @Override
    public Elderly queryByIdCard(String idCard) {
        QueryWrapper<Elderly> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard);
        return elderlyMapper.selectOne(wrapper);
    }

}




