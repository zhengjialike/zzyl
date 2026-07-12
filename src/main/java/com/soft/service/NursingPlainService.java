package com.soft.service;

import com.soft.dto.Nursing.NursingPlainDto;
import com.soft.dto.Nursing.NursingPlainPageDto;
import com.soft.pojo.NursingPlain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Teacher
 * @description 针对表【t_nursing_plain】的数据库操作Service
 * @createDate 2026-07-09 14:28:30
 */
public interface NursingPlainService extends IService<NursingPlain> {

    /*实现护理计划的新增*/
    public Map<String,Object>
    saveNursingPlainService(NursingPlainDto nursingPlainDto);
    /*实现护理计划的更新*/
    public Map<String,Object>
    updateNursingPlainService(NursingPlainDto nursingPlainDto);

    /*实现护理计划分页查询*/
    public Map<String,Object>
    loadNursingListPageService(NursingPlainPageDto dto);

    /*根据护理计划id，通过该计划下所有护理项总费用*/
    public Double totalPlainItemPayService(Integer id);
    
    /*删除护理计划*/
    public Map<String,Object> delNursingPlainService(Integer id);
}
