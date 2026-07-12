package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Dept;
import com.zzyl.mapper.DeptMapper;
import com.zzyl.service.DeptService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public List<Map<String, Object>> getDeptTree(String deptName, String status) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(status)) {
            wrapper.eq(Dept::getStatus, Integer.valueOf(status));
        }
        wrapper.orderByAsc(Dept::getSort).orderByAsc(Dept::getCreateTime);
        List<Dept> allDepts = baseMapper.selectList(wrapper);

        // Build tree
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Dept dept : allDepts) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                Map<String, Object> node = deptToMap(dept);
                buildChildren(node, allDepts);
                // Filter by name
                if (!StringUtils.hasText(deptName) || 
                    ((String)node.get("deptName")).contains(deptName) || 
                    hasMatchingChild(node, deptName)) {
                    tree.add(node);
                }
            }
        }
        return tree;
    }

    private void buildChildren(Map<String, Object> parent, List<Dept> allDepts) {
        List<Map<String, Object>> children = new ArrayList<>();
        Integer parentId = (Integer) parent.get("id");
        for (Dept dept : allDepts) {
            if (dept.getParentId() != null && dept.getParentId().equals(parentId)) {
                Map<String, Object> child = deptToMap(dept);
                buildChildren(child, allDepts);
                children.add(child);
            }
        }
        if (!children.isEmpty()) {
            parent.put("children", children);
        }
    }

    private boolean hasMatchingChild(Map<String, Object> node, String name) {
        if (!StringUtils.hasText(name)) return false;
        List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
        if (children == null) return false;
        for (Map<String, Object> child : children) {
            if (((String)child.get("deptName")).contains(name) || hasMatchingChild(child, name)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> deptToMap(Dept dept) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", dept.getId());
        map.put("deptName", dept.getDeptName());
        map.put("parentId", dept.getParentId());
        map.put("sort", dept.getSort());
        map.put("leader", dept.getLeader());
        map.put("status", dept.getStatus());
        map.put("description", dept.getDescription());
        map.put("createTime", dept.getCreateTime());
        return map;
    }

    @Override
    public void addDept(Dept dept) {
        baseMapper.insert(dept);
    }

    @Override
    public void updateDept(Dept dept) {
        baseMapper.updateById(dept);
    }

    @Override
    public void updateStatus(Long id, String status) {
        List<Dept> allSubs = getAllSubDeptIds(id.intValue());
        for (Dept d : allSubs) {
            d.setStatus(Integer.valueOf(status));
            baseMapper.updateById(d);
        }
        Dept dept = new Dept();
        dept.setId(id.intValue());
        dept.setStatus(Integer.valueOf(status));
        baseMapper.updateById(dept);
    }

    private List<Dept> getAllSubDeptIds(Integer parentId) {
        List<Dept> result = new ArrayList<>();
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getParentId, parentId);
        List<Dept> children = baseMapper.selectList(wrapper);
        for (Dept child : children) {
            result.add(child);
            result.addAll(getAllSubDeptIds(child.getId()));
        }
        return result;
    }
}
