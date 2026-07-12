package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.ContractPageDto;
import com.soft.mapper.ContractMapper;
import com.soft.pojo.Contract;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public Map<String, Object> pageList(ContractPageDto dto) {
        Page<Contract> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(dto.getContractNo())) {
            wrapper.eq("contract_no", dto.getContractNo());
        }
        if (StringUtils.hasText(dto.getElderName())) {
            wrapper.like("elder_name", dto.getElderName());
        }
        if (StringUtils.hasText(dto.getStatus())) {
            wrapper.eq("status", dto.getStatus());
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            wrapper.between("start_date", dto.getStartDate(), dto.getEndDate());
        }
        wrapper.orderByDesc("create_time");
        Page<Contract> resultPage = contractMapper.selectPage(page, wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("list", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        return result;
    }

    @Override
    public Contract queryDetail(Integer id) {
        return contractMapper.selectById(id);
    }
}