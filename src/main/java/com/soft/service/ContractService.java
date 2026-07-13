package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.ContractPageDto;
import com.soft.pojo.Contract;

import java.util.Map;

public interface ContractService extends IService<Contract> {

    /** 刷新日期型状态后分页查询合同。 */
    Map<String, Object> pageList(ContractPageDto dto);

    /** 聚合合同、入住签约和退住解除信息。 */
    Map<String, Object> queryDetail(Integer id);
}
