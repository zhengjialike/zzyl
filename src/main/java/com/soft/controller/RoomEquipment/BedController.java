package com.soft.controller.RoomEquipment;

import com.soft.pojo.Bed;
import com.soft.pojo.Elderly;
import com.soft.service.BedService;
import com.soft.service.ElderlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BedController {

    @Autowired
    private BedService bedService;

    @Autowired
    private ElderlyService elderlyService;

    /**
     * 新增床位
     */
    @PostMapping("/saveBed")
    public Map<String, Object> saveBed(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (bed.getStatus() == null) {
                bed.setStatus(0); // 默认空闲
            }
            bedService.save(bed);
            result.put("code", 200);
            result.put("msg", "添加床位成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加床位失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据房间ID查询床位列表（包含老人姓名）
     */
    @GetMapping("/bedList")
    public Map<String, Object> getBedList(@RequestParam("roomId") Integer roomId) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("查询房间ID: " + roomId + " 的床位");
            List<Bed> beds = bedService.getBedsByRoomId(roomId);
            System.out.println("查询到的床位数量: " + beds.size());
            
            // 收集所有有老人的床位的elderlyId
            java.util.Set<Integer> elderlyIdSet = new java.util.HashSet<>();
            for (Bed bed : beds) {
                if (bed.getElderlyId() != null) {
                    elderlyIdSet.add(bed.getElderlyId());
                }
            }
            
            System.out.println("需要查询的老人ID: " + elderlyIdSet);
            
            // 批量查询老人信息
            java.util.Map<Integer, String> elderlyNameMap = new java.util.HashMap<>();
            if (!elderlyIdSet.isEmpty()) {
                List<Elderly> elderlyList = elderlyService.listByIds(new java.util.ArrayList<>(elderlyIdSet));
                for (Elderly elderly : elderlyList) {
                    elderlyNameMap.put(elderly.getId(), elderly.getRealName());
                }
            }
            
            System.out.println("老人姓名映射: " + elderlyNameMap);
            
            // 构建返回数据
            List<Map<String, Object>> bedWithNames = new java.util.ArrayList<>();
            for (Bed bed : beds) {
                Map<String, Object> bedMap = new HashMap<>();
                bedMap.put("id", bed.getId());
                bedMap.put("roomId", bed.getRoomId());
                bedMap.put("bedNumber", bed.getBedNumber());
                bedMap.put("elderlyId", bed.getElderlyId());
                bedMap.put("status", bed.getStatus());
                bedMap.put("createTime", bed.getCreateTime());
                bedMap.put("updateTime", bed.getUpdateTime());
                
                // 添加老人姓名
                if (bed.getElderlyId() != null && elderlyNameMap.containsKey(bed.getElderlyId())) {
                    bedMap.put("elderlyName", elderlyNameMap.get(bed.getElderlyId()));
                } else {
                    bedMap.put("elderlyName", null);
                }
                
                bedWithNames.add(bedMap);
            }
            
            System.out.println("返回的床位数据: " + bedWithNames);
            
            result.put("code", 200);
            result.put("data", bedWithNames);
        } catch (Exception e) {
            System.out.println("查询床位异常: " + e.getMessage());
            e.printStackTrace();
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新床位
     */
    @PostMapping("/updateBed")
    public Map<String, Object> updateBed(@RequestBody Bed bed) {
        Map<String, Object> result = new HashMap<>();
        try {
            bedService.updateById(bed);
            result.put("code", 200);
            result.put("msg", "更新床位成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新床位失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除床位
     */
    @PostMapping("/deleteBed")
    public Map<String, Object> deleteBed(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();

        if (bedService.isOccupied(id)) {
            result.put("code", 400);
            result.put("msg", "该床位已绑定老人，无法删除");
            return result;
        }

        try {
            bedService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除床位成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "删除床位失败：" + e.getMessage());
        }
        return result;
    }
}