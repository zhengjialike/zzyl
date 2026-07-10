package com.soft.controller;

import com.soft.dto.NursingPlainDto;
import com.soft.dto.NursingPlainPageDto;
import com.soft.pojo.NursingPlain;
import com.soft.service.NursingPlainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class NursingPlainItemController {
    //注入service
    @Autowired
    private NursingPlainService nursingPlainService;

    /*定义接口实现护理计划向保存*/
    @RequestMapping("/saveNursingPlain")
    public Map<String,Object> saveNursingPlain(
            @RequestBody NursingPlainDto nursingPlainDto){
        return nursingPlainService.saveNursingPlainService(nursingPlainDto);
    }
    /*定义分页查询接口*/
    @RequestMapping("/pageList")
    public Map<String,Object> pageList(@RequestBody NursingPlainPageDto dto){
        return nursingPlainService.loadNursingListPageService(dto);
    }
    /*定义接口实现护理计划向更新*/
    @RequestMapping("/updateNursingPlain")
    public Map<String,Object> updateNursingPlain(
            @RequestBody NursingPlainDto nursingPlainDto){
        return nursingPlainService.updateNursingPlainService(nursingPlainDto);
    }
    /*定义接口加载所有护理计划*/
    @RequestMapping("/plainList")
    public List<NursingPlain> queryPlainList(){

        List<NursingPlain> list = nursingPlainService.list();
        return list;
    }
    /*定义接口统计护理计划下执行护理项的所有费用*/
    @RequestMapping("/totalPay")
    public Double totalPlainPay(@RequestParam(name="plainid") Integer plainid){
        return nursingPlainService.totalPlainItemPayService(plainid);
    }
}
