package com.soft.service;

import com.soft.dto.RoomEquipment.DeviceQueryDto;
import com.soft.pojo.Device;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_device(设备表)】的数据库操作Service
* @createDate 2026-07-11 21:08:39
*/
public interface DeviceService extends IService<Device> {
    /**
     * 分页查询设备列表
     */
    Map<String, Object> queryDeviceList(DeviceQueryDto dto);

    /**
     * 检查设备名称是否已存在
     */
    boolean isDeviceNameExists(String deviceName, Integer excludeId);

    /**
     * 检查位置是否已绑定该产品
     */
    boolean isLocationBound(Integer locationType, Integer locationId, Integer productId, Integer excludeId);

}
