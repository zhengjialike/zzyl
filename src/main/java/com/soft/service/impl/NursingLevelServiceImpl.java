package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.NursingLevelDto;
import com.soft.pojo.NursingLevel;
import com.soft.service.NursingLevelService;
import com.soft.mapper.NursingLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teacher
 * @description 针对表【t_nursing_level】的数据库操作Service实现
 * @createDate 2026-07-10 16:10:27
 */
@Service
public class NursingLevelServiceImpl extends
        ServiceImpl<NursingLevelMapper, NursingLevel>
        implements NursingLevelService{

    @Autowired
    private NursingLevelMapper nursingLevelMapper;
    @Override
    public Map<String, Object> queryNursingLevelListService(NursingLevelDto dto) {

        //指定分页查询参数
        Page<NursingLevel> page=new Page<>(dto.getPageNum(),dto.getPageSize());
        //查询数据库
        List<NursingLevel> nursingLevels
                = nursingLevelMapper.queryNursingListMapper(dto);

        Map<String, Object> result=new HashMap<>();
        result.put("total",page.getTotal());
        result.put("nursingLevels",nursingLevels);
        return result;
    }
}




