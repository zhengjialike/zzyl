package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Role;
import com.soft.entity.RoleMenu;
import com.soft.entity.RoleDataScope;
import com.soft.mapper.RoleMapper;
import com.soft.mapper.RoleMenuMapper;
import com.soft.mapper.RoleDataScopeMapper;
import com.soft.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private RoleDataScopeMapper roleDataScopeMapper;

    @Override
    public List<Role> findAll(String roleName) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(roleName)) {
            wrapper.like(Role::getRoleName, roleName);
        }
        wrapper.orderByDesc(Role::getCreateTime);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void addRole(Role role) {
        baseMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        baseMapper.updateById(role);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Role role = new Role();
        role.setId(id);
        role.setStatus(Integer.valueOf(status));
        baseMapper.updateById(role);
    }

    @Override
    @Transactional
    public void saveMenuPerms(Long roleId, List<Long> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                RoleMenu rm = new RoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                roleMenuMapper.insert(rm);
            }
        }
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        return roleMenuMapper.getMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void saveDataScope(Long roleId, String dataScope, String deptIds) {
        roleDataScopeMapper.deleteByRoleId(roleId);
        RoleDataScope scope = new RoleDataScope();
        scope.setRoleId(roleId);
        scope.setDataScope(dataScope);
        scope.setDeptIds(deptIds);
        roleDataScopeMapper.insert(scope);
    }

    @Override
    public Map<String, Object> getDataScope(Long roleId) {
        Map<String, Object> result = new HashMap<>();
        RoleDataScope scope = roleDataScopeMapper.getByRoleId(roleId);
        if (scope != null) {
            result.put("dataScope", scope.getDataScope());
            result.put("deptIds", scope.getDeptIds());
        } else {
            result.put("dataScope", "PERSONAL");
            result.put("deptIds", "");
        }
        return result;
    }
}
