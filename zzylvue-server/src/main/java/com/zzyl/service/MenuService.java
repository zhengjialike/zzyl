package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.entity.Menu;
import java.util.List;
import java.util.Map;

public interface MenuService extends IService<Menu> {
    List<Map<String, Object>> getMenuTree();
    void addMenu(Menu menu);
    void updateMenu(Menu menu);
    void updateStatus(Long id, String status);
    List<Menu> getButtonsByMenuId(Long menuId);
    void addButton(Menu button);
    void updateButton(Menu button);
    void updateButtonStatus(Long id, String status);
}
