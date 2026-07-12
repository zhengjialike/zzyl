package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.ContractPageDto;
import com.soft.pojo.Contract;

import java.util.Map;

public interface ContractService extends IService<Contract> {

    Map<String, Object> pageList(ContractPageDto dto);

    Map<String, Object> queryDetail(Integer id);
}
