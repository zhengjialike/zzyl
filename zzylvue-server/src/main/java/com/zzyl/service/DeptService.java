package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.entity.Dept;
import java.util.List;
import java.util.Map;

public interface DeptService extends IService<Dept> {
    List<Map<String, Object>> getDeptTree(String deptName, String status);
    void addDept(Dept dept);
    void updateDept(Dept dept);
    void updateStatus(Long id, String status);
}
