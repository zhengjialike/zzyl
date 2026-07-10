package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.pojo.PlainItem;
import com.soft.service.PlainItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlainItemController {

    @Autowired
    private PlainItemService plainItemService;


    /*
     * 定义接口查询某个护理计划下的所有护理项
     * */
    @RequestMapping("/queryPlainItem")
    public List<PlainItem> queryPlainItemList(@RequestParam(name="plainid") Integer plainid){
        QueryWrapper<PlainItem> wrapper=new QueryWrapper<>();
        wrapper.eq("plain_id",plainid);
        List<PlainItem> plainItems = plainItemService.list(wrapper);
        return plainItems;

    }

}
