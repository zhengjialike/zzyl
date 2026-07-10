package com.soft.controller;

import com.soft.dto.NursingPlainDto;
import com.soft.dto.NursingPlainPageDto;
import com.soft.service.NursingPlainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class NursingPlainItemController {

    @Autowired
    private NursingPlainService nursingPlainService;

    @RequestMapping(value = "/saveNursingPlain")
    public Map<String, Object> saveNursingPlain(@RequestBody NursingPlainDto nursingPlainDto) {
        return nursingPlainService.saveNursingPlainService(nursingPlainDto);
    }

    /*定义分页查询接口*/
    @RequestMapping("/pageList")
    public Map<String,Object> pageList(@RequestBody NursingPlainPageDto dto){
        return nursingPlainService.loadNursingListPageService(dto);
    }
}