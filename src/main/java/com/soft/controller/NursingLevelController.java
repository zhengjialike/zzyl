package com.soft.controller;

import com.soft.dto.NursingLevelDto;
import com.soft.pojo.NursingLevel;
import com.soft.service.NursingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class NursingLevelController {

    @Autowired
    private NursingLevelService nursingLevelService;

    //定义接口实现护理等级的保存
    @RequestMapping("/saveLevel")
    public Map<String,Object> saveNursingLevel(
            @RequestBody NursingLevel nursingLevel){
        Map<String,Object> result=new HashMap<>();

        result.put("code",400);
        result.put("msg","护理等级信息保存失败......");
        nursingLevel.setCreateuser("马云");
        nursingLevelService.save(nursingLevel);
        result.put("code",200);
        result.put("msg","护理等级信息保存成功......");
        return result;

    }

    //定义实现护理等级分页查询接口
    @RequestMapping("/levelList")
    public Map<String,Object> nursingLevelList(
            @RequestBody NursingLevelDto dto){
        return nursingLevelService.queryNursingLevelListService(dto);
    }
}
