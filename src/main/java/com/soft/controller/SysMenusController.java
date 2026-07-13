package com.soft.controller;

import com.soft.common.Result;
import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class SysMenusController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/sysMenus")
    public Result<List<Map<String, Object>>> sysMenus() {
        List<Map<String, Object>> tree = menuService.getMenuTree();
        // Transform to the format expected by MainIndex.vue
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> node : tree) {
            Map<String, Object> menu = new LinkedHashMap<>();
            menu.put("id", node.get("id"));
            menu.put("mname", node.get("menuName"));
            menu.put("path", node.get("path"));
            List<Map<String, Object>> subs = (List<Map<String, Object>>) node.get("children");
            List<Map<String, Object>> subItems = new ArrayList<>();
            if (subs != null) {
                for (Map<String, Object> sub : subs) {
                    Map<String, Object> subMenu = new LinkedHashMap<>();
                    subMenu.put("id", sub.get("id"));
                    subMenu.put("mname", sub.get("menuName"));
                    subMenu.put("path", sub.get("path"));
                    List<Map<String, Object>> subChildren = (List<Map<String, Object>>) sub.get("children");
                    List<Map<String, Object>> subChildItems = new ArrayList<>();
                    if (subChildren != null) {
                        for (Map<String, Object> child : subChildren) {
                            Map<String, Object> childItem = new LinkedHashMap<>();
                            childItem.put("id", child.get("id"));
                            childItem.put("mname", child.get("menuName"));
                            childItem.put("path", child.get("path"));
                            subChildItems.add(childItem);
                        }
                    }
                    subMenu.put("subItems", subChildItems);
                    subItems.add(subMenu);
                }
            }
            menu.put("subItems", subItems);
            result.add(menu);
        }
        return Result.success(result);
    }
}
