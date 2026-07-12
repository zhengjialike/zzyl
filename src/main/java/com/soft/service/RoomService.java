package com.soft.service;

import com.soft.pojo.Room;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12
* @description 针对表【t_room(房间表)】的数据库操作Service
* @createDate 2026-07-11 16:35:57
*/
public interface RoomService extends IService<Room> {
    /**
     * 根据楼层ID查询房间列表
     */
    List<Room> getRoomsByFloorId(Integer floorId);

    /**
     * 检查房间下是否有床位
     */
    boolean hasBeds(Integer roomId);
}
