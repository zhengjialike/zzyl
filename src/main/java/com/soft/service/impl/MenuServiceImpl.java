package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Menu;
import com.soft.service.MenuService;
import com.soft.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 12
* @description 针对表【t_menu】的数据库操作Service实现
* @createDate 2026-07-06 10:57:36
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    //注入Mapper代理对象
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<Menu> querySysMenuList() {
        // 查询所有菜单数据
        List<Menu> menus = menuMapper.selectList(null);

        // 1. 封装到 Map，key 是菜单 id，value 是菜单对象
        Map<Integer, Menu> map = new HashMap<>();
        menus.forEach(m -> {
            m.setSubItems(new ArrayList<>());
            map.put(m.getId(), m);
        });

        // 2. 构建树形结构
        List<Menu> result = new ArrayList<>();
        menus.forEach(m -> {
            // 判断是否为根节点（假设根节点的 pid 为 0）
            if (m.getPid() == 0) {
                result.add(m);
            } else {
                // 根据 父级ID 获取父节点
                Menu parent = map.get(m.getPid());
                // 如果父节点存在，将当前节点添加到父节点的子列表中
                if (parent != null) {
                    List<Menu> subItems = parent.getSubItems();
                    subItems.add(m);
                }
            }
        });
        return result;
    }
}




