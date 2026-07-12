package com.soft.mapper;

import com.soft.pojo.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 12
* @description 针对表【t_room(房间表)】的数据库操作Mapper
* @createDate 2026-07-11 16:35:57
* @Entity com.soft.pojo.Room
*/
public interface RoomMapper extends BaseMapper<Room> {
    /**
     * 统计指定房间的床位数量
     * @param roomId 房间ID
     * @return 床位数量
     */
    Integer countBedsByRoomId(@Param("roomId") Integer roomId);
}




