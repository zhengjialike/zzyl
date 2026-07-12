package com.soft.service;

import com.soft.dto.RoomEquipment.RoomTypeQueryDto;
import com.soft.pojo.RoomType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_room_type(房型表)】的数据库操作Service
* @createDate 2026-07-11 15:15:44
*/
public interface RoomTypeService extends IService<RoomType> {
    /**
     * 分页查询房型列表
     */
    Map<String, Object> queryRoomTypeList(RoomTypeQueryDto roomTypeQueryDto);

    /**
     * 检查房型下是否有房间（用于删除前校验）
     */
    boolean hasRooms(Integer roomTypeId);
}
