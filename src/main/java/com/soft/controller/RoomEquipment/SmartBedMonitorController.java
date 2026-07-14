package com.soft.controller.RoomEquipment;

import com.soft.pojo.*;
import com.soft.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/smartBed")
public class SmartBedMonitorController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BedService bedService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AlertRecordService alertRecordService;

    @Autowired
    private AlertRuleService alertRuleService;

    @Autowired
    private ElderlyService elderlyService;

    /**
     * 获取楼层列表（显示所有楼层，有设备的显示红点）
     */
    @GetMapping("/getFloorsWithDevices")
    public Map<String, Object> getFloorsWithDevices() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Floor> allFloors = floorService.list();
            List<Map<String, Object>> floorsWithDevices = new ArrayList<>();

            for (Floor floor : allFloors) {
                Map<String, Object> floorMap = new HashMap<>();
                floorMap.put("id", floor.getId());
                floorMap.put("floorName", floor.getFloorName());
                floorMap.put("hasAlert", hasAlertInFloor(floor.getId()));
                floorsWithDevices.add(floorMap);
            }

            result.put("code", 200);
            result.put("data", floorsWithDevices);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取楼层列表失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取房间列表（只显示绑定了设备的房间）
     */
    @GetMapping("/getRoomsWithDevices")
    public Map<String, Object> getRoomsWithDevices(@RequestParam("floorId") Integer floorId) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Room> rooms = roomService.list();
            List<Map<String, Object>> roomsWithDevices = new ArrayList<>();

            for (Room room : rooms) {
                if (room.getFloorId().equals(floorId) && hasDevicesInRoom(room.getId())) {
                    Map<String, Object> roomMap = new HashMap<>();
                    roomMap.put("id", room.getId());
                    roomMap.put("roomNumber", room.getRoomNumber());
                    roomMap.put("floorId", room.getFloorId()); // 添加楼层ID
                    roomMap.put("devices", getRoomDeviceList(room.getId()));
                    roomMap.put("hasAlert", hasAlertInRoom(room.getId()));
                    roomsWithDevices.add(roomMap);
                }
            }

            result.put("code", 200);
            result.put("data", roomsWithDevices);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取房间列表失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取床位列表（只显示绑定了设备的床位）
     */
    @GetMapping("/getBedsWithDevices")
    public Map<String, Object> getBedsWithDevices(@RequestParam("roomId") Integer roomId) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Bed> beds = bedService.list();
            List<Device> allDevices = deviceService.list();
            List<Map<String, Object>> bedsWithDevices = new ArrayList<>();

            for (Bed bed : beds) {
                if (bed.getRoomId().equals(roomId)) {
                    List<Device> devices = getBedDeviceList(bed.getId(), allDevices);

                    if (!devices.isEmpty()) {
                        Map<String, Object> bedMap = new HashMap<>();
                        bedMap.put("id", bed.getId());
                        bedMap.put("bedNumber", bed.getBedNumber());
                        bedMap.put("elderlyId", bed.getElderlyId());

                        if (bed.getElderlyId() != null) {
                            Elderly elderly = elderlyService.getById(bed.getElderlyId());
                            bedMap.put("elderlyName", elderly != null ? elderly.getRealName() : "-");
                        } else {
                            bedMap.put("elderlyName", "-");
                        }

                        bedMap.put("devices", devices.stream().map(device -> {
                            Map<String, Object> deviceMap = new HashMap<>();
                            deviceMap.put("id", device.getId());
                            deviceMap.put("deviceName", device.getDeviceName());
                            deviceMap.put("productName", getProductName(device.getProductId()));
                            deviceMap.put("functions", getProductFunctions(device.getProductId()));
                            return deviceMap;
                        }).collect(Collectors.toList()));

                        bedMap.put("hasAlert", hasUnresolvedAlertForBed(bed.getId()));
                        bedMap.put("alertInfo", getBedAlertInfo(bed.getId()));

                        bedsWithDevices.add(bedMap);
                    }
                }
            }

            result.put("code", 200);
            result.put("data", bedsWithDevices);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取床位列表失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // ==================== 辅助方法 ====================

    /**
     * 检查楼层是否有设备
     */
    private boolean hasDevicesInFloor(Integer floorId) {
        List<Room> rooms = roomService.list();
        for (Room room : rooms) {
            if (room.getFloorId().equals(floorId) && hasDevicesInRoom(room.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查房间是否有设备
     */
    private boolean hasDevicesInRoom(Integer roomId) {
        List<Bed> beds = bedService.list();
        List<Device> allDevices = deviceService.list();
        for (Bed bed : beds) {
            if (bed.getRoomId().equals(roomId)) {
                if (hasDevicesForLocation(3, bed.getId())) {
                    return true;
                }
                if (bed.getElderlyId() != null && hasDevicesForLocation(3, bed.getElderlyId())) {
                    return true;
                }
            }
        }
        return hasDevicesForLocation(1, roomId);
    }

    /**
     * 检查指定位置是否有设备
     */
    private boolean hasDevicesForLocation(Integer locationType, Integer locationId) {
        List<Device> devices = deviceService.list();
        for (Device device : devices) {
            if (device.getLocationType().equals(locationType) &&
                device.getLocationId().equals(locationId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取房间的设备列表
     */
    private List<Device> getRoomDeviceList(Integer roomId) {
        List<Device> devices = deviceService.list();
        return devices.stream()
            .filter(device -> (device.getLocationType() == 1 && device.getLocationId().equals(roomId)) ||
                             (device.getLocationType() == 2 && isBedInRoom(device.getLocationId(), roomId)))
            .collect(Collectors.toList());
    }

    /**
     * 获取床位的设备列表
     */
    private List<Device> getBedDeviceList(Integer bedId) {
        List<Device> devices = deviceService.list();
        List<Device> bedDevices = devices.stream()
            .filter(device -> device.getLocationType() == 3 && device.getLocationId().equals(bedId))
            .collect(Collectors.toList());

        Bed bed = bedService.getById(bedId);
        if (bed != null && bed.getElderlyId() != null) {
            List<Device> elderlyDevices = devices.stream()
                .filter(device -> device.getLocationType() == 3 && device.getLocationId().equals(bed.getElderlyId()))
                .collect(Collectors.toList());
            for (Device d : elderlyDevices) {
                if (bedDevices.stream().noneMatch(bd -> bd.getId().equals(d.getId()))) {
                    bedDevices.add(d);
                }
            }
        }

        return bedDevices;
    }

    /**
     * 获取床位的设备列表
     */
    private List<Device> getBedDeviceList(Integer bedId, List<Device> allDevices) {
        List<Device> bedDevices = allDevices.stream()
            .filter(device -> device.getLocationType() == 3 && device.getLocationId().equals(bedId))
            .collect(Collectors.toList());

        Bed bed = bedService.getById(bedId);
        if (bed != null && bed.getElderlyId() != null) {
            List<Device> elderlyDevices = allDevices.stream()
                .filter(device -> device.getLocationType() == 3 && device.getLocationId().equals(bed.getElderlyId()))
                .collect(Collectors.toList());
            for (Device d : elderlyDevices) {
                if (bedDevices.stream().noneMatch(bd -> bd.getId().equals(d.getId()))) {
                    bedDevices.add(d);
                }
            }
        }

        return bedDevices;
    }

    /**
     * 检查床位是否在指定房间
     */
    private boolean isBedInRoom(Integer bedId, Integer roomId) {
        Bed bed = bedService.getById(bedId);
        return bed != null && bed.getRoomId().equals(roomId);
    }

    /**
     * 获取产品名称
     */
    private String getProductName(Integer productId) {
        if (productId == null) return "-";
        Product product = productService.getById(productId);
        return product != null ? product.getProductName() : "-";
    }

    /**
     * 获取产品功能列表
     */
    private List<String> getProductFunctions(Integer productId) {
        if (productId == null) return new ArrayList<>();
        Product product = productService.getById(productId);
        if (product == null || product.getFunctions() == null) return new ArrayList<>();

        return Arrays.stream(product.getFunctions().split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

    /**
     * 检查楼层是否有未处理的报警
     */
    private boolean hasAlertInFloor(Integer floorId) {
        List<Room> rooms = roomService.list();
        for (Room room : rooms) {
            if (room.getFloorId().equals(floorId) && hasAlertInRoom(room.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查房间是否有未处理的报警
     */
    private boolean hasAlertInRoom(Integer roomId) {
        List<Bed> beds = bedService.list();
        for (Bed bed : beds) {
            if (bed.getRoomId().equals(roomId) && hasUnresolvedAlertForBed(bed.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查床位是否有未处理的报警
     */
    private boolean hasUnresolvedAlertForBed(Integer bedId) {
        List<Device> devices = getBedDeviceList(bedId);
        for (Device device : devices) {
            List<AlertRecord> records = alertRecordService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AlertRecord>()
                    .eq("device_id", device.getId().toString())
                    .eq("handle_status", 0)
            );
            if (!records.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取床位的报警信息
     */
    private List<Map<String, Object>> getBedAlertInfo(Integer bedId) {
        List<Map<String, Object>> alertInfo = new ArrayList<>();
        List<Device> devices = getBedDeviceList(bedId);

        for (Device device : devices) {
            List<AlertRecord> records = alertRecordService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AlertRecord>()
                    .eq("device_id", device.getId().toString())
                    .orderByDesc("alert_time")
            );

            for (AlertRecord record : records) {
                Map<String, Object> info = new HashMap<>();
                info.put("functionName", getFunctionNameFromRule(record.getRuleId()));
                info.put("dataValue", record.getDataValue());
                info.put("isResolved", record.getHandleStatus() == 1);
                alertInfo.add(info);
            }
        }

        return alertInfo;
    }

    /**
     * 从规则ID获取功能名称
     */
    private String getFunctionNameFromRule(Integer ruleId) {
        if (ruleId == null) return "-";
        AlertRule rule = alertRuleService.getById(ruleId);
        return rule != null ? rule.getFunctionName() : "-";
    }
}