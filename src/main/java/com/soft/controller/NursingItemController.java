package com.soft.controller;

import com.soft.dto.Nursing.NursingItemDto;
import com.soft.pojo.NursingItem;
import com.soft.service.NursingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NursingItemController {

    @Autowired
    private NursingItemService nursingItemService;

    @RequestMapping("/saveNursingItem")
    public Map<String, Object> saveNursingItem(@RequestBody NursingItem nursingItem) {
        Map<String, Object> result = new HashMap<>();
        try {
            nursingItemService.save(nursingItem);
            result.put("code", 200);
            result.put("msg", "添加护理项目成功......");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加护理项目失败......");
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/nursingItemPage")
    public Map<String, Object> nursingItemPageList(@RequestBody NursingItemDto dto) {
        return nursingItemService.queryNursingItemList(dto);
    }

    @PostMapping("/updateNursingItem")
    public Map<String, Object> updateNursingItem(@RequestBody NursingItem nursingItem) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新护理项目失败......");
        nursingItemService.updateById(nursingItem);
        result.put("code", 200);
        result.put("msg", "更新护理项目成功......");
        return result;
    }

    @RequestMapping("/deleteNursingItem")
    public Map<String, Object> deleteNursingItem(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除护理项目失败......");
        nursingItemService.removeById(id);
        result.put("code", 200);
        result.put("msg", "删除护理项目成功......");
        return result;
    }
}
