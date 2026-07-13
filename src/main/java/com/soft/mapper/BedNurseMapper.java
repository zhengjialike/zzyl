package com.soft.mapper;

import com.soft.pojo.BedNurse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 12
* @description 针对表【t_bed_nurse(床位-护理员关联表)】的数据库操作Mapper
* @createDate 2026-07-13 09:53:32
* @Entity com.soft.pojo.BedNurse
*/
public interface BedNurseMapper extends BaseMapper<BedNurse> {
    /**
     * 查询床位的护理员列表
     */
    List<Integer> selectNurseIdsByBedId(@Param("bedId") Integer bedId);

    /**
     * 批量设置床位护理员
     */
    void batchInsertBedNurses(@Param("bedId") Integer bedId,
                              @Param("nurseIds") List<Integer> nurseIds);

    /**
     * 删除床位的护理员关联
     */
    void deleteByBedId(@Param("bedId") Integer bedId);

}




