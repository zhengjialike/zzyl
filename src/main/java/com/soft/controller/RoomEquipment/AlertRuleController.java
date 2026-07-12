package com.soft.controller.RoomEquipment;

import com.soft.dto.RoomEquipment.AlertRuleQueryDto;
import com.soft.pojo.AlertRule;
import com.soft.service.AlertRuleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alertRule")
public class AlertRuleController {

    @Autowired
    private AlertRuleService alertRuleService;

    /**
     * 新增报警规则
     */
    @PostMapping("/saveAlertRule")
    public Map<String, Object> saveAlertRule(@RequestBody AlertRule alertRule, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 验证报警规则名称是否为空
        if (alertRule.getRuleName() == null || alertRule.getRuleName().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "报警规则名称不能为空");
            return result;
        }

        // 2. 验证报警规则名称是否已存在
        if (alertRuleService.isRuleNameExists(alertRule.getRuleName(), null)) {
            result.put("code", 400);
            result.put("msg", "报警规则名称已存在，请重新输入");
            return result;
        }

        // 3. 验证其他必填字段
        if (alertRule.getProductId() == null) {
            result.put("code", 400);
            result.put("msg", "所属产品不能为空");
            return result;
        }

        if (alertRule.getFunctionName() == null || alertRule.getFunctionName().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "功能名称不能为空");
            return result;
        }

        if (alertRule.getDeviceId() == null || alertRule.getDeviceId() == 0) {
            result.put("code", 400);
            result.put("msg", "关联设备不能为空");
            return result;
        }

        if (alertRule.getOperator() == null || alertRule.getOperator().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "运算符不能为空");
            return result;
        }

        if (alertRule.getThreshold() == null) {
            result.put("code", 400);
            result.put("msg", "阈值不能为空");
            return result;
        }

        if (alertRule.getDurationPeriod() == null) {
            result.put("code", 400);
            result.put("msg", "持续周期不能为空");
            return result;
        }

        if (alertRule.getEffectiveStart() == null) {
            result.put("code", 400);
            result.put("msg", "报警生效开始时间不能为空");
            return result;
        }

        if (alertRule.getEffectiveEnd() == null) {
            result.put("code", 400);
            result.put("msg", "报警生效结束时间不能为空");
            return result;
        }

        if (alertRule.getSilentMinutes() == null) {
            result.put("code", 400);
            result.put("msg", "报警沉默周期不能为空");
            return result;
        }

        // 4. 设置默认值
        if (alertRule.getStatus() == null) {
            alertRule.setStatus(1); // 默认启用
        }

        // 5. 获取当前登录用户作为创建人
        String createUser = getCurrentUser(session);
        alertRule.setCreateUser(createUser);

        // 6. 保存报警规则
        try {
            alertRuleService.save(alertRule);
            result.put("code", 200);
            result.put("msg", "添加报警规则成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "添加报警规则失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 报警规则分页查询
     */
    @PostMapping("/alertRulePage")
    public Map<String, Object> alertRulePageList(@RequestBody AlertRuleQueryDto dto) {
        return alertRuleService.queryAlertRuleList(dto);
    }

    /**
     * 更新报警规则
     */
    @PostMapping("/updateAlertRule")
    public Map<String, Object> updateAlertRule(@RequestBody AlertRule alertRule, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 验证报警规则ID
        if (alertRule.getId() == null) {
            result.put("code", 400);
            result.put("msg", "报警规则ID不能为空");
            return result;
        }

        // 2. 验证报警规则名称是否已存在（排除自己）
        if (alertRule.getRuleName() != null && !alertRule.getRuleName().trim().isEmpty()) {
            if (alertRuleService.isRuleNameExists(alertRule.getRuleName(), alertRule.getId())) {
                result.put("code", 400);
                result.put("msg", "报警规则名称已存在，请重新输入");
                return result;
            }
        }

        // 3. 更新报警规则
        try {
            alertRuleService.updateById(alertRule);
            result.put("code", 200);
            result.put("msg", "更新报警规则成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "更新报警规则失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 删除报警规则
     */
    @PostMapping("/deleteAlertRule")
    public Map<String, Object> deleteAlertRule(@RequestBody Map<String, Integer> payload) {
        Integer id = payload.get("id");
        Map<String, Object> result = new HashMap<>();

        try {
            alertRuleService.removeById(id);
            result.put("code", 200);
            result.put("msg", "删除报警规则成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "删除报警规则失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 启用/禁用报警规则
     */
    @PostMapping("/toggleAlertRuleStatus")
    public Map<String, Object> toggleAlertRuleStatus(@RequestBody Map<String, Object> payload) {
        Integer id = (Integer) payload.get("id");
        Integer status = (Integer) payload.get("status");
        Map<String, Object> result = new HashMap<>();

        if (id == null || status == null) {
            result.put("code", 400);
            result.put("msg", "参数不完整");
            return result;
        }

        try {
            AlertRule alertRule = alertRuleService.getById(id);
            if (alertRule == null) {
                result.put("code", 404);
                result.put("msg", "报警规则不存在");
                return result;
            }
            
            alertRule.setStatus(status);
            alertRuleService.updateById(alertRule);
            
            String statusText = status == 1 ? "启用" : "禁用";
            result.put("code", 200);
            result.put("msg", statusText + "报警规则成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "操作失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取所有产品列表（用于下拉框）
     */
    @GetMapping("/getAllProducts")
    public Map<String, Object> getAllProducts() {
        return alertRuleService.getAllProducts();
    }

    /**
     * 根据产品ID获取功能名称列表
     */
    @GetMapping("/getFunctionNames")
    public Map<String, Object> getFunctionNames(@RequestParam("productId") Integer productId) {
        return alertRuleService.getFunctionNamesByProductId(productId);
    }

    /**
     * 获取关联设备列表（用于下拉框）
     */
    @GetMapping("/getAssociatedDevices")
    public Map<String, Object> getAssociatedDevices(@RequestParam("productId") Integer productId) {
        return alertRuleService.getAssociatedDevices(productId);
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