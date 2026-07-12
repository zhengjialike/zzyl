package com.soft.controller.RoomEquipment;


import com.soft.dto.RoomEquipment.DeviceQueryDto;
import com.soft.pojo.Device;
import com.soft.service.DeviceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 新增设备
     */
    @PostMapping("/saveDevice")
    public Map<String, Object> saveDevice(@RequestBody Device device, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 验证设备名称是否为空
        if (device.getDeviceName() == null || device.getDeviceName().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "设备名称不能为空");
            return result;
        }

        // 2. 验证设备名称是否已存在
        if (deviceService.isDeviceNameExists(device.getDeviceName(), null)) {
            result.put("code", 400);
            result.put("msg", "设备名称已存在，请重新输入");
            return result;
        }

        // 3. 验证其他必填字段
        if (device.getRemarkName() == null || device.getRemarkName().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "备注名称不能为空");
            return result;
        }

        if (device.getProductId() == null) {
            result.put("code", 400);
            result.put("msg", "所属产品不能为空");
            return result;
        }

        if (device.getLocationType() == null) {
            result.put("code", 400);
            result.put("msg", "接入类别不能为空");
            return result;
        }

        if (device.getLocationId() == null) {
            result.put("code", 400);
            result.put("msg", "接入位置不能为空");
            return result;
        }

        // 4. 验证位置是否已绑定该产品
        if (deviceService.isLocationBound(device.getLocationType(), device.getLocationId(), device.getProductId(), null)) {
            result.put("code", 400);
            result.put("msg", "该老人/位置已绑定该产品，请重新选择");
            return result;
        }

        // 5. 设置默认值
        if (device.getStatus() == null) {
            device.setStatus(1); // 默认在线，只要绑定了就默认在线
        }

        // 6. 获取当前登录用户作为创建人
        String createUser = getCurrentUser(session);
        device.setCreateUser(createUser);

        // 7. 保存设备
        try {
            deviceService.save(device);
            result.put("code", 200);
            result.put("msg", "添加设备成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加设备失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设备分页查询
     */
    @PostMapping("/devicePage")
    public Map<String, Object> devicePageList(@RequestBody DeviceQueryDto dto) {
        return deviceService.queryDeviceList(dto);
    }

    /**
     * 更新设备
     */
    @PostMapping("/updateDevice")
    public Map<String, Object> updateDevice(@RequestBody Device device, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 验证设备ID
        if (device.getId() == null) {
            result.put("code", 400);
            result.put("msg", "设备ID不能为空");
            return result;
        }

        // 2. 验证设备名称是否已存在（排除自己）
        if (device.getDeviceName() != null && !device.getDeviceName().trim().isEmpty()) {
            if (deviceService.isDeviceNameExists(device.getDeviceName(), device.getId())) {
                result.put("code", 400);
                result.put("msg", "设备名称已存在，请重新输入");
                return result;
            }
        }

        // 3. 验证位置是否已绑定该产品（排除自己）
        if (device.getLocationType() != null && device.getLocationId() != null && device.getProductId() != null) {
            if (deviceService.isLocationBound(device.getLocationType(), device.getLocationId(), device.getProductId(), device.getId())) {
                result.put("code", 400);
                result.put("msg", "该老人/位置已绑定该产品，请重新选择");
                return result;
            }
        }

        // 4. 更新设备
        try {
            deviceService.updateById(device);
            result.put("code", 200);
            result.put("msg", "更新设备成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新设备失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 删除设备
     */
    @PostMapping("/deleteDevice")
    public Map<String, Object> deleteDevice(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();

        try {
            deviceService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除设备成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "删除设备失败：" + e.getMessage());
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
        return "系统"; // 默认值
    }
}