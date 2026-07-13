package com.soft.controller;

import com.soft.dto.Nursing.NursingItemDto;
import com.soft.pojo.NursingItem;
import com.soft.service.NursingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NursingItemController {

    @Autowired
    private NursingItemService nursingItemService;

    /**
     * 添加护理项目
     * @param nursingItem 前端提交的护理项目 JSON 数据
     * @return 操作结果（code, msg）
     */
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

    /**
     * 护理项目分页条件查询
     * 注意：路径必须与前端请求路径完全一致
     */
    //@PostMapping("/queryNursingItemList")
    //public Map<String, Object> nursingItemPageList(@RequestBody NursingItemDto dto) {
    //    return nursingItemService.queryNursingItemList(dto);
    //}

    // 分页查询（修正为POST以支持@RequestBody，匹配前端）
    @PostMapping("/nursingItemPage")
    public Map<String, Object> nursingItemPageList(@RequestBody NursingItemDto dto) {
        Map<String, Object> result = nursingItemService.queryNursingItemList(dto);
        result.put("code", 200);
        return result;
    }

    // 更新护理项目
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

    // 删除护理项目
    @PostMapping("/deleteNursingItem")
    public Map<String, Object> deleteNursingItem(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除护理项目失败......");

        nursingItemService.removeById(id);

        result.put("code", 200);
        result.put("msg", "删除护理项目成功......");
        return result;
    }
}