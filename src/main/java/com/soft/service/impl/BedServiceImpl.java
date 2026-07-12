package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BedMapper;
import com.soft.pojo.Bed;
import com.soft.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed>
        implements BedService {

    @Autowired
    private BedMapper bedMapper;

    @Override
    public List<Bed> getBedsByRoomId(Integer roomId) {
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.eq("room_id", roomId);
        return bedMapper.selectList(wrapper);
    }

    @Override
    public boolean isOccupied(Integer bedId) {
        Bed bed = bedMapper.selectById(bedId);
        return bed != null && bed.getElderlyId() != null;
    }
}
