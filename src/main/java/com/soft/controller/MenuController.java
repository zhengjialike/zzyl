package com.soft.controller;

import com.soft.common.Result;
import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired private MenuService menuService;

    @GetMapping("/tree") public Result<List<Map<String, Object>>> tree() { return Result.success(menuService.getMenuTree()); }

    @PostMapping("/add") public Result<Void> add(@RequestBody Map<String, Object> p) {
        Menu m = new Menu(); m.setMname((String)p.get("menuName")); m.setPid(p.get("parentId")!=null?Integer.valueOf(p.get("parentId").toString()):0);
        m.setPath((String)p.get("path")); m.setSort(p.get("sort")!=null?Integer.valueOf(p.get("sort").toString()):0);
        m.setVisible(p.get("visible")!=null?Integer.valueOf(p.get("visible").toString()):1); menuService.addMenu(m); return Result.success();
    }

    @PostMapping("/update") public Result<Void> update(@RequestBody Map<String, Object> p) {
        Menu m = new Menu(); m.setId(Integer.valueOf(p.get("id").toString())); m.setMname((String)p.get("menuName"));
        m.setPid(p.get("parentId")!=null?Integer.valueOf(p.get("parentId").toString()):0); m.setPath((String)p.get("path"));
        m.setSort(p.get("sort")!=null?Integer.valueOf(p.get("sort").toString()):0);
        m.setVisible(p.get("visible")!=null?Integer.valueOf(p.get("visible").toString()):1); menuService.updateMenu(m); return Result.success();
    }

    @PostMapping("/updateStatus") public Result<Void> updateStatus(@RequestBody Map<String, Object> p) { menuService.updateStatus(Long.valueOf(p.get("id").toString()), p.get("status")!=null?p.get("status").toString():null); return Result.success(); }
    @GetMapping("/buttons/{menuId}") public Result<List<Menu>> getButtons(@PathVariable Long menuId) { return Result.success(menuService.getButtonsByMenuId(menuId)); }

    @PostMapping("/addButton") public Result<Void> addButton(@RequestBody Map<String, Object> p) {
        Menu m = new Menu(); m.setMname((String)p.get("menuName")); m.setPid(Integer.valueOf(p.get("parentId").toString()));
        m.setPath((String)p.get("path")); m.setSort(p.get("sort")!=null?Integer.valueOf(p.get("sort").toString()):0);
        m.setVisible(p.get("visible")!=null?Integer.valueOf(p.get("visible").toString()):1); menuService.addButton(m); return Result.success();
    }

    @PostMapping("/updateButton") public Result<Void> updateButton(@RequestBody Map<String, Object> p) {
        Menu m = new Menu(); m.setId(Integer.valueOf(p.get("id").toString())); m.setMname((String)p.get("menuName"));
        m.setPath((String)p.get("path")); m.setSort(p.get("sort")!=null?Integer.valueOf(p.get("sort").toString()):0);
        m.setVisible(p.get("visible")!=null?Integer.valueOf(p.get("visible").toString()):1); menuService.updateButton(m); return Result.success();
    }

    @PostMapping("/updateButtonStatus") public Result<Void> updateButtonStatus(@RequestBody Map<String, Object> p) { menuService.updateButtonStatus(Long.valueOf(p.get("id").toString()), p.get("status")!=null?p.get("status").toString():null); return Result.success(); }
}
