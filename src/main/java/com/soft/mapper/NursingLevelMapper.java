package com.soft.mapper;

import com.soft.dto.NursingLevelDto;
import com.soft.pojo.NursingLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Teacher
 * @description 针对表【t_nursing_level】的数据库操作Mapper
 * @createDate 2026-07-10 16:10:27
 * @Entity com.soft.pojo.NursingLevel
 */
public interface NursingLevelMapper extends BaseMapper<NursingLevel> {

    //定义方法实现护理等级动态sql查询
    public List<NursingLevel> queryNursingListMapper(NursingLevelDto dto);
}




