package com.soft.service;

import com.soft.pojo.Floor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12
* @description 针对表【t_floor(楼层表)】的数据库操作Service
* @createDate 2026-07-11 16:35:35
*/
public interface FloorService extends IService<Floor> {
    /**
     * 查询所有楼层（按排序）
     */
    List<Floor> getAllFloors();

    /**
     * 检查楼层下是否有房间
     */
    boolean hasRooms(Integer floorId);
}
