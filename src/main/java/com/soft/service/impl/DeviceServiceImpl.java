package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.RoomEquipment.DeviceQueryDto;
import com.soft.mapper.DeviceMapper;
import com.soft.pojo.Bed;
import com.soft.pojo.Device;
import com.soft.pojo.Elderly;
import com.soft.pojo.Product;
import com.soft.pojo.Room;
import com.soft.service.BedService;
import com.soft.service.DeviceService;
import com.soft.service.ElderlyService;
import com.soft.service.ProductService;
import com.soft.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 12
* @description 针对表【t_device(设备表)】的数据库操作Service实现
* @createDate 2026-07-11 21:08:39
*/
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService{
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BedService bedService;

    @Autowired
    private ElderlyService elderlyService;

    @Autowired
    private ProductService productService;

    @Override
    public Map<String, Object> queryDeviceList(DeviceQueryDto dto) {
        // 1. 创建分页对象
        Page<Device> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<Device> wrapper = new QueryWrapper<>();

        // 3. 添加查询条件
        if (dto.getProductId() != null) {
            wrapper.eq("product_id", dto.getProductId());
        }
        if (StringUtils.hasText(dto.getDeviceName())) {
            wrapper.like("device_name", dto.getDeviceName());
        }

        // 4. 按创建时间倒序排列
        wrapper.orderByDesc("create_time");

        // 5. 执行分页查询
        IPage<Device> iPage = deviceMapper.selectPage(page, wrapper);
        List<Device> devices = iPage.getRecords();

        // 6. 封装结果，添加位置名称和产品名称
        List<Map<String, Object>> deviceList = devices.stream().map(device -> {
            Map<String, Object> deviceMap = new HashMap<>();
            deviceMap.put("id", device.getId());
            deviceMap.put("deviceName", device.getDeviceName());
            deviceMap.put("remarkName", device.getRemarkName());
            deviceMap.put("productId", device.getProductId());

            // 根据 productId 获取产品名称
            String productName = getProductName(device.getProductId());
            deviceMap.put("productName", productName);

            deviceMap.put("locationType", device.getLocationType());
            deviceMap.put("locationId", device.getLocationId());
            deviceMap.put("status", device.getStatus());
            deviceMap.put("createUser", device.getCreateUser());
            deviceMap.put("createTime", device.getCreateTime());

            // 根据位置类型获取位置名称
            String locationName = getLocationName(device.getLocationType(), device.getLocationId());
            deviceMap.put("locationName", locationName);

            return deviceMap;
        }).collect(Collectors.toList());

        // 7. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("total", iPage.getTotal());
        result.put("devices", deviceList);
        return result;
    }

    @Override
    public boolean isDeviceNameExists(String deviceName, Integer excludeId) {
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        wrapper.eq("device_name", deviceName);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return deviceMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean isLocationBound(Integer locationType, Integer locationId, Integer productId, Integer excludeId) {
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        wrapper.eq("location_type", locationType)
                .eq("location_id", locationId)
                .eq("product_id", productId);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return deviceMapper.selectCount(wrapper) > 0;
    }

    /**
     * 根据产品ID获取产品名称
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
     * 根据位置类型获取位置名称
     */
    private String getLocationName(Integer locationType, Integer locationId) {
        if (locationId == null) {
            return "-";
        }

        try {
            switch (locationType) {
                case 1: // 房间
                    Room room = roomService.getById(locationId);
                    return room != null ? room.getRoomNumber() + "房间" : "-";
                case 2: // 床位
                    Bed bed = bedService.getById(locationId);
                    return bed != null ? bed.getBedNumber() + "床位" : "-";
                case 3: // 老人
                    Elderly elderly = elderlyService.getById(locationId);
                    return elderly != null ? elderly.getRealName() : "-";
                default:
                    return "-";
            }
        } catch (Exception e) {
            return "-";
        }
    }
}




