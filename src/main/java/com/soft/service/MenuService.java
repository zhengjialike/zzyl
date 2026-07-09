package com.soft.service;

import com.soft.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 12
* @description 针对表【t_menu】的数据库操作Service
* @createDate 2026-07-06 10:57:36
*/
public interface MenuService extends IService<Menu> {
    /**
     * 查询系统菜单
     */
    public List<Menu> querySysMenuList();
}
