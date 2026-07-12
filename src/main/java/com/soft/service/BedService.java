package com.soft.service;

import com.soft.pojo.Bed;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12
* @description 针对表【t_bed(床位表)】的数据库操作Service
* @createDate 2026-07-11 16:36:09
*/
public interface BedService extends IService<Bed> {
    /**
     * 根据房间ID查询床位列表
     */
    List<Bed> getBedsByRoomId(Integer roomId);

    /**
     * 检查床位是否已绑定老人
     */
    boolean isOccupied(Integer bedId);
}
