package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.RoomEquipment.RoomTypeQueryDto;
import com.soft.pojo.RoomType;
import com.soft.service.RoomTypeService;
import com.soft.mapper.RoomTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 12
* @description 针对表【t_room_type(房型表)】的数据库操作Service实现
* @createDate 2026-07-11 15:15:44
*/
@Service
public class RoomTypeServiceImpl extends ServiceImpl<RoomTypeMapper, RoomType>
    implements RoomTypeService{

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Override
    public Map<String, Object> queryRoomTypeList(RoomTypeQueryDto roomTypeQueryDto) {
        // 1. 创建分页对象
        Page<RoomType> page = new Page<>(roomTypeQueryDto.getPageNum(), roomTypeQueryDto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<RoomType> wrapper = new QueryWrapper<>();

        // 3. 添加查询条件
        if (StringUtils.hasText(roomTypeQueryDto.getTypeName())) {
            wrapper.like("type_name", roomTypeQueryDto.getTypeName());
        }
        if (roomTypeQueryDto.getStatus() != null) {
            wrapper.eq("status", roomTypeQueryDto.getStatus());
        }

        // 4. 按创建时间倒序排列
        wrapper.orderByDesc("create_time");

        // 5. 执行分页查询
        IPage<RoomType> iPage = roomTypeMapper.selectPage(page, wrapper);
        List<RoomType> roomTypes = iPage.getRecords();

        // 6. 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("total", iPage.getTotal());
        result.put("roomTypes", roomTypes);
        return result;
    }

    @Override
    public boolean hasRooms(Integer roomTypeId) {
        Integer count = roomTypeMapper.countRoomsByTypeId(roomTypeId);
        return count != null && count > 0;
    }
}




