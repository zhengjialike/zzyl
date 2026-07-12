package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.entity.Role;
import java.util.List;
import java.util.Map;

public interface RoleService extends IService<Role> {
    List<Role> findAll(String roleName);
    void addRole(Role role);
    void updateRole(Role role);
    void updateStatus(Long id, String status);
    void saveMenuPerms(Long roleId, List<Long> menuIds);
    List<Long> getMenuIds(Long roleId);
    void saveDataScope(Long roleId, String dataScope, String deptIds);
    Map<String, Object> getDataScope(Long roleId);
}
