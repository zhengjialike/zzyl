package com.zzyl.controller;

import com.zzyl.common.Result;
import com.zzyl.entity.Menu;
import com.zzyl.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> tree() {
        return Result.success(menuService.getMenuTree());
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody Menu menu) {
        menuService.addMenu(menu);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        menuService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/buttons/{menuId}")
    public Result<List<Menu>> getButtons(@PathVariable Long menuId) {
        return Result.success(menuService.getButtonsByMenuId(menuId));
    }

    @PostMapping("/addButton")
    public Result<Void> addButton(@RequestBody Menu button) {
        menuService.addButton(button);
        return Result.success();
    }

    @PostMapping("/updateButton")
    public Result<Void> updateButton(@RequestBody Menu button) {
        menuService.updateButton(button);
        return Result.success();
    }

    @PostMapping("/updateButtonStatus")
    public Result<Void> updateButtonStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Object statusObj = params.get("status");
        String status = statusObj != null ? statusObj.toString() : null;
        menuService.updateButtonStatus(id, status);
        return Result.success();
    }
}
