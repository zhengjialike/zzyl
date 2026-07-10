package com.soft.service;

import com.soft.dto.NursingLevelDto;
import com.soft.pojo.NursingLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Teacher
 * @description 针对表【t_nursing_level】的数据库操作Service
 * @createDate 2026-07-10 16:10:27
 */
public interface NursingLevelService extends IService<NursingLevel> {

    //定义方法实现护理等级分页查询
    public Map<String,Object>
    queryNursingLevelListService(NursingLevelDto dto);
}
