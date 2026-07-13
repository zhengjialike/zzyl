package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.NursingTaskMapper;
import com.soft.mapper.UserMapper;
import com.soft.mapper.ElderlyMapper;
import com.soft.mapper.BedMapper;
import com.soft.mapper.NursingItemMapper;
import com.soft.pojo.NursingTask;
import com.soft.pojo.User;
import com.soft.pojo.Elderly;
import com.soft.pojo.Bed;
import com.soft.pojo.NursingItem;
import com.soft.service.NursingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class NursingTaskServiceImpl extends ServiceImpl<NursingTaskMapper, NursingTask> implements NursingTaskService {

    @Autowired private UserMapper userMapper;
    @Autowired private ElderlyMapper elderlyMapper;
    @Autowired private BedMapper bedMapper;
    @Autowired private NursingItemMapper nursingItemMapper;

    public Map<String, Object> pageList(int pageNum, int pageSize, String taskNo, String elderlyName, Integer status) {
        LambdaQueryWrapper<NursingTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(taskNo)) {
            wrapper.eq(NursingTask::getTaskNo, taskNo);
        }
        if (status != null) {
            wrapper.eq(NursingTask::getStatus, status);
        }
        wrapper.orderByDesc(NursingTask::getCreateTime);
        Page<NursingTask> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        List<Map<String, Object>> list = new ArrayList<>();
        for (NursingTask t : page.getRecords()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.getId());
            m.put("taskNo", t.getTaskNo());
            m.put("elderlyId", t.getElderlyId());
            Elderly e = elderlyMapper.selectById(t.getElderlyId());
            m.put("elderlyName", e != null ? e.getRealName() : "");
            m.put("bedId", t.getBedId());
            Bed b = t.getBedId() != null ? bedMapper.selectById(t.getBedId()) : null;
            m.put("bedNo", b != null ? b.getBedNumber() : "");
            m.put("nursingItemId", t.getNursingItemId());
            NursingItem ni = nursingItemMapper.selectById(t.getNursingItemId());
            m.put("nursingItemName", ni != null ? ni.getItemname() : "");
            m.put("itemType", t.getItemType());
            m.put("nurseId", t.getNurseId());
            User u = t.getNurseId() != null ? userMapper.selectById(t.getNurseId()) : null;
            m.put("nurseName", u != null ? u.getRealname() : "");
            m.put("expectedServiceTime", t.getExpectedServiceTime());
            m.put("status", t.getStatus());
            m.put("creator", t.getCreator());
            m.put("createTime", t.getCreateTime());
            m.put("executionRecord", t.getExecutionRecord());
            list.add(m);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", page.getTotal());
        return result;
    }
}
