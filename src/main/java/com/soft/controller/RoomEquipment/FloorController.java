package com.soft.controller.RoomEquipment;

import com.soft.pojo.Floor;
import com.soft.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FloorController {

    @Autowired
    private FloorService floorService;

    /**
     * 新增楼层
     */
    @PostMapping("/saveFloor")
    public Map<String, Object> saveFloor(@RequestBody Floor floor) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (floor.getSort() == null) {
                floor.setSort(1);
            }
            floorService.save(floor);
            result.put("code", 200);
            result.put("msg", "添加楼层成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加楼层失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询所有楼层
     */
    @GetMapping("/floorList")
    public Map<String, Object> getFloorList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Floor> floors = floorService.getAllFloors();
            result.put("code", 200);
            result.put("data", floors);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新楼层
     */
    @PostMapping("/updateFloor")
    public Map<String, Object> updateFloor(@RequestBody Floor floor) {
        Map<String, Object> result = new HashMap<>();
        try {
            floorService.updateById(floor);
            result.put("code", 200);
            result.put("msg", "更新楼层成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新楼层失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除楼层
     */
    @PostMapping("/deleteFloor")
    public Map<String, Object> deleteFloor(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();

        if (floorService.hasRooms(id)) {
            result.put("code", 400);
            result.put("msg", "该楼层下已有房间，无法删除");
            return result;
        }

        try {
            floorService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除楼层成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "删除楼层失败：" + e.getMessage());
        }
        return result;
    }
}