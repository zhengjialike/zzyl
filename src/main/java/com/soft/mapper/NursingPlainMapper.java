package com.soft.mapper;

import com.soft.pojo.NursingPlain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 12
* @description 针对表【t_nursing_plain】的数据库操作Mapper
* @createDate 2026-07-10 09:58:30
* @Entity com.soft.pojo.NursingPlain
*/
public interface NursingPlainMapper extends BaseMapper<NursingPlain> {
    //定义方法统计某个护理计划下的所有护理项执行总费用
    public Double totalPlainItemPayMapper(Integer plainid);

}




