package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soft.dto.MyApplyPageDto;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.CheckOutMapper;
import com.soft.pojo.CheckIn;
import com.soft.pojo.CheckOut;
import com.soft.service.MyApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyApplyServiceImpl implements MyApplyService {

    @Autowired
    private CheckInMapper checkInMapper;
    @Autowired
    private CheckOutMapper checkOutMapper;

    @Override
    public Map<String, Object> pageList(MyApplyPageDto dto, String applicant) {
        List<Map<String, Object>> all = new ArrayList<>();

        // 查询入住申请
        QueryWrapper<CheckIn> cw = new QueryWrapper<>();
        cw.eq("applicant", applicant);
        if (StringUtils.hasText(dto.getBillNo())) {
            cw.eq("bill_no", dto.getBillNo());
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            cw.between("create_time", dto.getStartDate().atStartOfDay(), dto.getEndDate().plusDays(1).atStartOfDay());
        }
        List<CheckIn> checkIns = checkInMapper.selectList(cw);
        for (CheckIn ci : checkIns) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", ci.getId());
            m.put("billNo", ci.getBillNo());
            m.put("billTitle", ci.getElderName() + "的入住申请");
            m.put("billType", "入住");
            m.put("applicant", ci.getApplicant());
            m.put("applyTime", ci.getCreateTime());
            m.put("finishTime", ci.getFinishTime());
            m.put("flowStatus", ci.getFlowStatus());
            m.put("currentStep", ci.getCurrentStep());
            // 入住流程只要仍处于申请中即可撤销，后端撤销时会回滚床位和待签合同。
            m.put("canRevoke", "申请中".equals(ci.getFlowStatus()));
            all.add(m);
        }

        // 查询退住申请
        QueryWrapper<CheckOut> ow = new QueryWrapper<>();
        ow.eq("applicant", applicant);
        if (StringUtils.hasText(dto.getBillNo())) {
            ow.eq("bill_no", dto.getBillNo());
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            ow.between("create_time", dto.getStartDate().atStartOfDay(), dto.getEndDate().plusDays(1).atStartOfDay());
        }
        List<CheckOut> checkOuts = checkOutMapper.selectList(ow);
        for (CheckOut co : checkOuts) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", co.getId());
            m.put("billNo", co.getBillNo());
            m.put("billTitle", co.getElderName() + "的退住申请");
            m.put("billType", "退住");
            m.put("applicant", co.getApplicant());
            m.put("applyTime", co.getCreateTime());
            m.put("finishTime", co.getFinishTime());
            m.put("flowStatus", co.getFlowStatus());
            m.put("currentStep", co.getCurrentStep());
            // 原型规定退住审批通过前可以撤销；进入费用清算后不再允许撤销。
            m.put("canRevoke", "申请中".equals(co.getFlowStatus())
                    && co.getCurrentStep() != null && co.getCurrentStep() <= 6);
            all.add(m);
        }

        // 按单据类别过滤
        if (StringUtils.hasText(dto.getBillType())) {
            all = all.stream().filter(m -> dto.getBillType().equals(m.get("billType"))).collect(Collectors.toList());
        }
        if (StringUtils.hasText(dto.getFlowStatus())) {
            all = all.stream().filter(m -> dto.getFlowStatus().equals(m.get("flowStatus"))).collect(Collectors.toList());
        }

        // 按创建时间倒序
        all.sort(Comparator.comparing(m -> (Comparable) m.get("applyTime"), Comparator.reverseOrder()));

        // 内存分页
        int total = all.size();
        int pageNum = dto.getPageNum() == null || dto.getPageNum() < 1 ? 1 : dto.getPageNum();
        int pageSize = dto.getPageSize() == null || dto.getPageSize() < 1 ? 10 : dto.getPageSize();
        int from = (pageNum - 1) * pageSize;
        int to = Math.min(from + pageSize, total);
        List<Map<String, Object>> pageData = from < to ? all.subList(from, to) : new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageData);
        result.put("total", total);
        return result;
    }
}
