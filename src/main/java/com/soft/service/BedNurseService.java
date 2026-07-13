package com.soft.service;

import com.soft.pojo.BedNurse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 12
* @description 针对表【t_bed_nurse(床位-护理员关联表)】的数据库操作Service
* @createDate 2026-07-13 09:53:32
*/
public interface BedNurseService extends IService<BedNurse> {
    /**
     * 设置床位护理员
     */
    Map<String, Object> setBedNurses(Integer bedId, List<Integer> nurseIds);

    /**
     * 批量设置房间所有床位的护理员
     */
    Map<String, Object> batchSetRoomNurses(Integer roomId, List<Integer> nurseIds);

    /**
     * 查询床位的护理员列表
     */
    List<Integer> getNurseIdsByBedId(Integer bedId);

}
