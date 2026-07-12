package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.FloorMapper;
import com.soft.pojo.Floor;
import com.soft.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor>
        implements FloorService {

    @Autowired
    private FloorMapper floorMapper;

    @Override
    public List<Floor> getAllFloors() {
        QueryWrapper<Floor> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        return floorMapper.selectList(wrapper);
    }

    @Override
    public boolean hasRooms(Integer floorId) {
        Integer count = floorMapper.countRoomsByFloorId(floorId);
        return count != null && count > 0;
    }
}


