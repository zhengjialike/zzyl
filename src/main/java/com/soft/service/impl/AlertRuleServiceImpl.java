package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.RoomEquipment.AlertRuleQueryDto;
import com.soft.mapper.AlertRuleMapper;
import com.soft.pojo.AlertRule;
import com.soft.pojo.Device;
import com.soft.pojo.Product;
import com.soft.service.AlertRuleService;
import com.soft.service.DeviceService;
import com.soft.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 12
* @description 针对表【t_alert_rule(报警规则表)】的数据库操作Service实现
* @createDate 2026-07-12 00:16:01
*/
@Service
public class AlertRuleServiceImpl extends ServiceImpl<AlertRuleMapper, AlertRule>
    implements AlertRuleService{

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private DeviceService deviceService;

    @Override
    public Map<String, Object> queryAlertRuleList(AlertRuleQueryDto dto) {
        // 1. 创建分页对象
        Page<AlertRule> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<AlertRule> wrapper = new QueryWrapper<>();

        // 3. 添加查询条件
        if (StringUtils.hasText(dto.getRuleName())) {
            wrapper.like("rule_name", dto.getRuleName());
        }
        if (dto.getProductId() != null) {
            wrapper.eq("product_id", dto.getProductId());
        }
        if (StringUtils.hasText(dto.getFunctionName())) {
            wrapper.like("function_name", dto.getFunctionName());
        }
        if (dto.getStatus() != null) {
            wrapper.eq("status", dto.getStatus());
        }

        // 4. 按创建时间倒序排列
        wrapper.orderByDesc("create_time");

        // 5. 执行分页查询
        IPage<AlertRule> iPage = alertRuleMapper.selectPage(page, wrapper);
        List<AlertRule> alertRules = iPage.getRecords();

        // 6. 封装结果，添加产品名称、功能名称、设备名称等
        List<Map<String, Object>> alertRuleList = alertRules.stream().map(alertRule -> {
            Map<String, Object> alertRuleMap = new HashMap<>();
            alertRuleMap.put("id", alertRule.getId());
            alertRuleMap.put("ruleName", alertRule.getRuleName());
            alertRuleMap.put("productId", alertRule.getProductId());
            
            // 获取产品名称
            String productName = getProductName(alertRule.getProductId());
            alertRuleMap.put("productName", productName);
            
            // 获取功能名称
            alertRuleMap.put("functionName", alertRule.getFunctionName());
            
            // 获取设备名称
            String deviceName = getDeviceName(alertRule.getDeviceId());
            alertRuleMap.put("deviceId", alertRule.getDeviceId());
            alertRuleMap.put("deviceName", deviceName);
            
            alertRuleMap.put("operator", alertRule.getOperator());
            alertRuleMap.put("threshold", alertRule.getThreshold());
            alertRuleMap.put("durationPeriod", alertRule.getDurationPeriod());
            alertRuleMap.put("dataAggregationPeriod", alertRule.getDataAggregationPeriod());
            alertRuleMap.put("effectiveStart", alertRule.getEffectiveStart());
            alertRuleMap.put("effectiveEnd", alertRule.getEffectiveEnd());
            alertRuleMap.put("silentMinutes", alertRule.getSilentMinutes());
            alertRuleMap.put("status", alertRule.getStatus());
            alertRuleMap.put("createUser", alertRule.getCreateUser());
            alertRuleMap.put("createTime", alertRule.getCreateTime());
            alertRuleMap.put("updateTime", alertRule.getUpdateTime());

            return alertRuleMap;
        }).collect(Collectors.toList());

        // 7. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("total", iPage.getTotal());
        result.put("alertRules", alertRuleList);
        return result;
    }

    @Override
    public boolean isRuleNameExists(String ruleName, Integer excludeId) {
        QueryWrapper<AlertRule> wrapper = new QueryWrapper<>();
        wrapper.eq("rule_name", ruleName);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return alertRuleMapper.selectCount(wrapper) > 0;
    }

    @Override
    public Map<String, Object> getAllProducts() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Product> products = productService.list();
            List<Map<String, Object>> productList = products.stream().map(product -> {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getId());
                productMap.put("productName", product.getProductName());
                productMap.put("functions", product.getFunctions());
                return productMap;
            }).collect(Collectors.toList());
            
            result.put("code", 200);
            result.put("products", productList);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取产品列表失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getFunctionNamesByProductId(Integer productId) {
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
            if (!StringUtils.hasText(functions)) {
                result.put("code", 200);
                result.put("functionNames", new ArrayList<>());
                return result;
            }
            
            // 解析功能名称字符串（逗号分隔）
            String[] functionArray = functions.split(",");
            List<Map<String, Object>> functionNames = new ArrayList<>();
            for (String func : functionArray) {
                String trimmedFunc = func.trim();
                if (StringUtils.hasText(trimmedFunc)) {
                    Map<String, Object> funcMap = new HashMap<>();
                    funcMap.put("functionName", trimmedFunc);
                    functionNames.add(funcMap);
                }
            }
            
            result.put("code", 200);
            result.put("functionNames", functionNames);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取功能名称失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getAssociatedDevices(Integer productId) {
        Map<String, Object> result = new HashMap<>();
        if (productId == null) {
            result.put("code", 400);
            result.put("msg", "产品ID不能为空");
            return result;
        }
        
        try {
            // 查询该产品下的所有设备
            QueryWrapper<Device> wrapper = new QueryWrapper<>();
            wrapper.eq("product_id", productId);
            List<Device> devices = deviceService.list(wrapper);
            
            List<Map<String, Object>> deviceList = new ArrayList<>();
            
            // 添加"全部设备"选项（值为0）
            Map<String, Object> allDevicesOption = new HashMap<>();
            allDevicesOption.put("deviceId", 0);
            allDevicesOption.put("deviceName", "全部设备");
            deviceList.add(allDevicesOption);
            
            // 添加具体设备
            for (Device device : devices) {
                Map<String, Object> deviceMap = new HashMap<>();
                deviceMap.put("deviceId", device.getId());
                deviceMap.put("deviceName", device.getDeviceName());
                deviceList.add(deviceMap);
            }
            
            result.put("code", 200);
            result.put("devices", deviceList);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "获取设备列表失败：" + e.getMessage());
        }
        return result;
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
     * 根据设备ID获取设备名称
     */
    private String getDeviceName(Integer deviceId) {
        if (deviceId == null || deviceId == 0) {
            return "全部设备";
        }
        
        try {
            Device device = deviceService.getById(deviceId);
            return device != null ? device.getDeviceName() : "-";
        } catch (Exception e) {
            return "-";
        }
    }
}




