package com.soft.controller.RoomEquipment;


import com.soft.dto.RoomEquipment.DeviceQueryDto;
import com.soft.pojo.AlertRecord;
import com.soft.pojo.AlertRule;
import com.soft.pojo.Device;
import com.soft.pojo.Product;
import com.soft.service.AlertRecordService;
import com.soft.service.AlertRuleService;
import com.soft.service.DeviceService;
import com.soft.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AlertRecordService alertRecordService;

    @Autowired
    private AlertRuleService alertRuleService;

    @Autowired
    private com.soft.service.RoomService roomService;

    @Autowired
    private com.soft.service.BedService bedService;

    @Autowired
    private com.soft.service.ElderlyService elderlyService;

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

    /**
     * 获取设备详情
     */
    @GetMapping("/getDeviceInfo")
    public Map<String, Object> getDeviceInfo(@RequestParam("id") Integer id) {
        Map<String, Object> result = new HashMap<>();

        try {
            Device device = deviceService.getById(id);
            if (device == null) {
                result.put("code", 404);
                result.put("msg", "设备不存在");
                return result;
            }

            Map<String, Object> deviceMap = new HashMap<>();
            deviceMap.put("id", device.getId());
            deviceMap.put("deviceName", device.getDeviceName());
            deviceMap.put("remarkName", device.getRemarkName());
            deviceMap.put("productId", device.getProductId());
            deviceMap.put("productName", getProductName(device.getProductId()));
            deviceMap.put("locationType", device.getLocationType());
            deviceMap.put("locationId", device.getLocationId());
            deviceMap.put("locationName", getLocationName(device.getLocationType(), device.getLocationId()));
            deviceMap.put("status", device.getStatus());
            deviceMap.put("createUser", device.getCreateUser());
            deviceMap.put("createTime", device.getCreateTime());

            result.put("code", 200);
            result.put("data", deviceMap);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取设备详情失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据产品ID获取功能模块列表
     */
    @GetMapping("/getProductFunctions")
    public Map<String, Object> getProductFunctions(@RequestParam("productId") Integer productId) {
        Map<String, Object> result = new HashMap<>();

        if (productId == null) {
            result.put("code", 400);
            result.put("msg", "产品ID不能为空");
            return result;
        }

        try {
            Product product = productService.getById(productId);
            if (product == null) {
                result.put("code", 404);
                result.put("msg", "产品不存在");
                return result;
            }

            String functions = product.getFunctions();
            List<String> functionList = new ArrayList<>();

            if (functions != null && !functions.trim().isEmpty()) {
                String[] funcArray = functions.split(",");
                for (String func : funcArray) {
                    String trimmedFunc = func.trim();
                    if (!trimmedFunc.isEmpty()) {
                        functionList.add(trimmedFunc);
                    }
                }
            }

            result.put("code", 200);
            result.put("data", functionList);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取功能模块失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取设备的报警记录
     */
    @GetMapping("/getDeviceAlertRecords")
    public Map<String, Object> getDeviceAlertRecords(@RequestParam("deviceId") String deviceId) {
        Map<String, Object> result = new HashMap<>();

        if (deviceId == null || deviceId.trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "设备ID不能为空");
            return result;
        }

        try {
            QueryWrapper<AlertRecord> wrapper = new QueryWrapper<>();
            wrapper.eq("device_id", deviceId);
            wrapper.orderByDesc("alert_time");

            List<AlertRecord> records = alertRecordService.list(wrapper);

            List<Map<String, Object>> recordList = records.stream().map(record -> {
                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("id", record.getId());
                recordMap.put("ruleId", record.getRuleId());

                // 获取规则名称
                AlertRule rule = alertRuleService.getById(record.getRuleId());
                recordMap.put("ruleName", rule != null ? rule.getRuleName() : "-");

                recordMap.put("handleStatus", record.getHandleStatus());
                recordMap.put("handleResult", record.getHandleResult() != null ? record.getHandleResult() : "-");
                recordMap.put("dataValue", record.getDataValue());
                recordMap.put("handleTime", record.getHandleTime());

                return recordMap;
            }).collect(Collectors.toList());

            result.put("code", 200);
            result.put("data", recordList);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取报警记录失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取产品名称
     */
    private String getProductName(Integer productId) {
        if (productId == null) {
            return "-";
        }
        try {
            Product product = productService.getById(productId);
            return product != null ? product.getProductName() : "-";
        } catch (Exception e) {
            return "-";
        }
    }

    /**
     * 获取位置名称
     */
    private String getLocationName(Integer locationType, Integer locationId) {
        if (locationId == null) {
            return "-";
        }

        try {
            switch (locationType) {
                case 1: // 房间
                    com.soft.pojo.Room room = roomService.getById(locationId);
                    return room != null ? room.getRoomNumber() + "房间" : "-";
                case 2: // 床位
                    com.soft.pojo.Bed bed = bedService.getById(locationId);
                    return bed != null ? bed.getBedNumber() + "床位" : "-";
                case 3: // 老人
                    com.soft.pojo.Elderly elderly = elderlyService.getById(locationId);
                    return elderly != null ? elderly.getRealName() : "-";
                default:
                    return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }
}