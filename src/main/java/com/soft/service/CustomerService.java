package com.soft.service;

import com.soft.dto.Customer.CustomerQueryDto;
import com.soft.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 12
* @description 针对表【t_customer(客户/家属表（小程序使用者）)】的数据库操作Service
* @createDate 2026-07-11 11:02:13
*/
public interface CustomerService extends IService<Customer> {
    /**
     * 分页查询客户列表（含签约状态、下单次数、绑定老人信息）
     */
    Map<String, Object> queryCustomerList(CustomerQueryDto customerQueryDto);

}
