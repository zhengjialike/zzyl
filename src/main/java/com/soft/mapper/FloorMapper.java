package com.soft.mapper;

import com.soft.pojo.Floor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 12
* @description 针对表【t_floor(楼层表)】的数据库操作Mapper
* @createDate 2026-07-11 16:35:35
* @Entity com.soft.pojo.Floor
*/
public interface FloorMapper extends BaseMapper<Floor> {
    /**
     * 统计指定楼层的房间数量
     * @param floorId 楼层ID
     * @return 房间数量
     */
    Integer countRoomsByFloorId(@Param("floorId") Integer floorId);
}




