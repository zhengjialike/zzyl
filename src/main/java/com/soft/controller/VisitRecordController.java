package com.soft.controller;

import com.soft.pojo.VisitRecord;
import com.soft.service.VisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VisitRecordController {
    @Autowired
    private VisitRecordService recordService;

    /*定义实现来访记录登记接口*/
    @RequestMapping("/saveVisitRecord")
    public Map<String,Object> saveVisitRecord(
            @RequestBody VisitRecord visitRecord){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","来访记录登记失败......");
        visitRecord.setCreatetime(new Date());
        visitRecord.setCreateuser("马云");
        recordService.save(visitRecord);
        result.put("code",200);
        result.put("msg","来访记录登记成功......");
        return result;
    }
}
