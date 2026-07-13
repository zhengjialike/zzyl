package com.soft.controller.RoomEquipment;

import com.soft.dto.RoomEquipment.RoomTypeQueryDto;
import com.soft.pojo.RoomType;
import com.soft.service.RoomTypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    /**
     * 新增房型
     */
    @PostMapping("/saveRoomType")
    public Map<String, Object> saveRoomType(@RequestBody RoomType roomType, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 设置默认值
            if (roomType.getStatus() == null) {
                roomType.setStatus(1); // 默认启用
            }

            // 获取当前登录用户作为创建人
            String createUser = getCurrentUser(session);
            roomType.setCreateUser(createUser);

            roomTypeService.save(roomType);
            result.put("code", 200);
            result.put("msg", "添加房型成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加房型失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 房型分页查询
     */
    @PostMapping("/roomTypePage")
    public Map<String, Object> roomTypePageList(@RequestBody RoomTypeQueryDto dto) {
        return roomTypeService.queryRoomTypeList(dto);
    }

    /**
     * 更新房型
     */
    @PostMapping("/updateRoomType")
    public Map<String, Object> updateRoomType(@RequestBody RoomType roomType, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新房型失败");

        try {
            // 如果更新了其他字段，同时更新创建人（可选）
            // roomType.setCreateUser(getCurrentUser(session));

            roomTypeService.updateById(roomType);
            result.put("code", 200);
            result.put("msg", "更新房型成功");
        } catch (Exception e) {
            result.put("msg", "更新房型失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除房型
     */
    @PostMapping("/deleteRoomType")
    public Map<String, Object> deleteRoomType(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "删除房型失败");

        try {
            // 检查是否有房间使用该房型
            if (roomTypeService.hasRooms(id)) {
                result.put("msg", "该房型下已创建房间，无法删除");
                return result;
            }

            roomTypeService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除房型成功");
        } catch (Exception e) {
            result.put("msg", "删除房型失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 切换房型状态（启用/禁用）
     */
    @PostMapping("/toggleRoomTypeStatus")
    public Map<String, Object> toggleRoomTypeStatus(@RequestBody Map<String, Object> payload) {
        Integer id = (Integer) payload.get("id");
        Integer status = (Integer) payload.get("status");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", "更新状态失败");

        try {
            RoomType roomType = new RoomType();
            roomType.setId(id);
            roomType.setStatus(status);
            roomTypeService.updateById(roomType);

            result.put("code", 200);
            result.put("msg", "更新状态成功");
        } catch (Exception e) {
            result.put("msg", "更新状态失败：" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUser(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online != null) {
            com.soft.dto.UserLineDto user = (com.soft.dto.UserLineDto) online;
            return user.getUname();
        }
        return "系统";
    }

    /**
     * 检查房型下是否有房间
     */
    @GetMapping("/checkRoomTypeHasRooms")
    public Map<String, Object> checkRoomTypeHasRooms(@RequestParam("id") Integer id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("hasRooms", roomTypeService.hasRooms(id));
        return result;
    }
}
