package com.soft.controller;

import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {
    //注入service对象
    @Autowired
    private MenuService menuService;
    /*定义处理加载系统菜单的接口*/
    @RequestMapping(value = "/sysMenus")
    public List<Menu> sysMenuList() {
        return menuService.querySysMenuList();
    }
}
