package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Menu;
import com.soft.mapper.MenuMapper;
import com.soft.service.MenuService;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Map<String, Object>> getMenuTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort);
        List<Menu> allMenus = baseMapper.selectList(wrapper);
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Menu menu : allMenus) {
            if (menu.getPid() == null || menu.getPid() == 0) {
                Map<String, Object> node = menuToMap(menu);
                buildMenuChildren(node, allMenus);
                tree.add(node);
            }
        }
        return tree;
    }

    private void buildMenuChildren(Map<String, Object> parent, List<Menu> allMenus) {
        List<Map<String, Object>> children = new ArrayList<>();
        Integer parentId = (Integer) parent.get("id");
        for (Menu menu : allMenus) {
            if (menu.getPid() != null && menu.getPid().equals(parentId)) {
                Map<String, Object> child = menuToMap(menu);
                buildMenuChildren(child, allMenus);
                children.add(child);
            }
        }
        if (!children.isEmpty()) { parent.put("children", children); }
    }

    private Map<String, Object> menuToMap(Menu menu) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", menu.getId()); map.put("menuName", menu.getMname());
        map.put("parentId", menu.getPid()); map.put("path", menu.getPath());
        map.put("sort", menu.getSort()); map.put("visible", menu.getVisible());
        return map;
    }

    @Override public void addMenu(Menu menu) { baseMapper.insert(menu); }
    @Override public void updateMenu(Menu menu) { baseMapper.updateById(menu); }
    @Override public void updateStatus(Long id, String status) { /* no-op: t_menu has no status field */ }

    @Override
    public List<Menu> getButtonsByMenuId(Long menuId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getPid, menuId).orderByAsc(Menu::getSort);
        return baseMapper.selectList(wrapper);
    }

    @Override public void addButton(Menu button) { baseMapper.insert(button); }
    @Override public void updateButton(Menu button) { baseMapper.updateById(button); }
    @Override public void updateButtonStatus(Long id, String status) { /* no-op */ }
}
