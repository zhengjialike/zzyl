package com.soft.controller.RoomEquipment;

import com.soft.pojo.Room;
import com.soft.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 新增房间
     */
    @PostMapping("/saveRoom")
    public Map<String, Object> saveRoom(@RequestBody Room room) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (room.getSort() == null) {
                room.setSort(1);
            }
            roomService.save(room);
            result.put("code", 200);
            result.put("msg", "添加房间成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加房间失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据楼层ID查询房间列表
     */
    @GetMapping("/roomList")
    public Map<String, Object> getRoomList(@RequestParam("floorId") Integer floorId) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("查询楼层ID: " + floorId + " 的房间");
            List<Room> rooms = roomService.getRoomsByFloorId(floorId);
            System.out.println("查询到的房间数量: " + rooms.size());
            System.out.println("房间列表: " + rooms);
            result.put("code", 200);
            result.put("data", rooms);
        } catch (Exception e) {
            System.out.println("查询房间异常: " + e.getMessage());
            e.printStackTrace();
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新房间
     */
    @PostMapping("/updateRoom")
    public Map<String, Object> updateRoom(@RequestBody Room room) {
        Map<String, Object> result = new HashMap<>();
        try {
            roomService.updateById(room);
            result.put("code", 200);
            result.put("msg", "更新房间成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新房间失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除房间
     */
    @PostMapping("/deleteRoom")
    public Map<String, Object> deleteRoom(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();

        if (roomService.hasBeds(id)) {
            result.put("code", 400);
            result.put("msg", "该房间下已有床位，无法删除");
            return result;
        }

        try {
            roomService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除房间成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "删除房间失败：" + e.getMessage());
        }
        return result;
    }
}