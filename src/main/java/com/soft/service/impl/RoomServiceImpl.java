package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.RoomMapper;
import com.soft.pojo.Room;
import com.soft.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
        implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<Room> getRoomsByFloorId(Integer floorId) {
        QueryWrapper<Room> wrapper = new QueryWrapper<>();
        wrapper.eq("floor_id", floorId);
        wrapper.orderByAsc("sort");
        return roomMapper.selectList(wrapper);
    }

    @Override
    public boolean hasBeds(Integer roomId) {
        Integer count = roomMapper.countBedsByRoomId(roomId);
        return count != null && count > 0;
    }
}
