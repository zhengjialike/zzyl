package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.entity.Menu;
import com.zzyl.mapper.MenuMapper;
import com.zzyl.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Map<String, Object>> getMenuTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSort).orderByAsc(Menu::getCreateTime);
        List<Menu> allMenus = baseMapper.selectList(wrapper);

        List<Map<String, Object>> tree = new ArrayList<>();
        for (Menu menu : allMenus) {
            if ("menu".equals(menu.getType()) && (menu.getParentId() == null || menu.getParentId() == 0)) {
                Map<String, Object> node = menuToMap(menu);
                buildMenuChildren(node, allMenus);
                tree.add(node);
            }
        }
        return tree;
    }

    private void buildMenuChildren(Map<String, Object> parent, List<Menu> allMenus) {
        List<Map<String, Object>> children = new ArrayList<>();
        Long parentId = (Long) parent.get("id");
        for (Menu menu : allMenus) {
            if ("menu".equals(menu.getType()) && menu.getParentId() != null && menu.getParentId().equals(parentId)) {
                Map<String, Object> child = menuToMap(menu);
                buildMenuChildren(child, allMenus);
                children.add(child);
            }
        }
        if (!children.isEmpty()) {
            parent.put("children", children);
        }
    }

    private Map<String, Object> menuToMap(Menu menu) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", menu.getId());
        map.put("menuName", menu.getMenuName());
        map.put("parentId", menu.getParentId());
        map.put("path", menu.getPath());
        map.put("icon", menu.getIcon());
        map.put("sort", menu.getSort());
        map.put("type", menu.getType());
        map.put("status", menu.getStatus());
        map.put("createTime", menu.getCreateTime());
        return map;
    }

    @Override
    public void addMenu(Menu menu) {
        if (menu.getType() == null) menu.setType("menu");
        baseMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        baseMapper.updateById(menu);
    }

    @Override
    public void updateStatus(Long id, String status) {
        List<Long> allSubIds = getAllSubMenuIds(id);
        for (Long subId : allSubIds) {
            Menu m = new Menu();
            m.setId(subId);
            m.setStatus(Integer.valueOf(status));
            baseMapper.updateById(m);
        }
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(Integer.valueOf(status));
        baseMapper.updateById(menu);
    }

    private List<Long> getAllSubMenuIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, parentId).eq(Menu::getType, "menu");
        List<Menu> children = baseMapper.selectList(wrapper);
        for (Menu child : children) {
            result.add(child.getId());
            result.addAll(getAllSubMenuIds(child.getId()));
        }
        return result;
    }

    @Override
    public List<Menu> getButtonsByMenuId(Long menuId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId).eq(Menu::getType, "button").orderByAsc(Menu::getSort);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void addButton(Menu button) {
        button.setType("button");
        baseMapper.insert(button);
    }

    @Override
    public void updateButton(Menu button) {
        baseMapper.updateById(button);
    }

    @Override
    public void updateButtonStatus(Long id, String status) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setStatus(Integer.valueOf(status));
        baseMapper.updateById(menu);
    }
}
