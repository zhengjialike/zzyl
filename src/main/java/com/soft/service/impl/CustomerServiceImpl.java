package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.soft.dto.Customer.CustomerQueryDto;
import com.soft.dto.Customer.CustomerResponseDto;
import com.soft.mapper.CustomerMapper;
import com.soft.pojo.Customer;
import com.soft.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> 
        implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Map<String, Object> queryCustomerList(CustomerQueryDto customerQueryDto) {
        // 1. 创建分页对象
        Page<Customer> page = new Page<>(customerQueryDto.getPageNum(), customerQueryDto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        
        // 3. 添加查询条件
        if (StringUtils.hasText(customerQueryDto.getNickname())) {
            wrapper.like("nickname", customerQueryDto.getNickname());
        }
        if (StringUtils.hasText(customerQueryDto.getPhone())) {
            wrapper.eq("phone", customerQueryDto.getPhone());
        }
        
        // 4. 按首次登录时间倒序排列
        wrapper.orderByDesc("create_time");

        // 5. 执行分页查询
        IPage<Customer> iPage = customerMapper.selectPage(page, wrapper);
        List<Customer> customers = iPage.getRecords();

        // 6. 组装返回数据（包含签约状态、下单次数、绑定老人信息）
        List<CustomerResponseDto> responseList = customers.stream().map(customer -> {
            CustomerResponseDto dto = new CustomerResponseDto();
            dto.setId(customer.getId());
            dto.setNickname(customer.getNickname());
            dto.setPhone(customer.getPhone());
            dto.setCreateTime(customer.getCreateTime());

            // 查询是否签约（需要关联合同表）
            dto.setIsSigned(checkIfSigned(customer.getPhone()));

            // 查询服务下单次数（需要关联订单表）
            dto.setOrderCount(getOrderCount(customer.getPhone()));

            // 查询绑定的老人姓名
            dto.setElderlyNames(getBoundElderlyNames(customer.getId()));

            return dto;
        }).collect(Collectors.toList());

        // 7. 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", iPage.getTotal());
        result.put("customers", responseList);
        return result;
    }

    /**
     * 检查客户是否已签约
     * 逻辑：客户手机号 = 乙方/丙方手机号（在合同管理中）
     */
    private String checkIfSigned(String phone) {
        // TODO: 待合同管理功能完成后实现
        // 这里假设有一个合同表 t_contract，包含 party_b_phone（乙方电话）和 party_c_phone（丙方电话）
        // boolean isSigned = customerMapper.checkContractExists(phone);
        // return isSigned ? "是" : "否";
        
        // 临时返回默认值
        return "否";
    }

    /**
     * 获取客户服务下单次数
     * 逻辑：财务管理->订单管理->订单总数量->订单状态为已完成、待执行（下单人手机号=客户手机号）
     */
    private Integer getOrderCount(String phone) {
        // TODO: 待财务管理-订单管理功能完成后实现
        // 这里假设有一个订单表 t_order，包含 customer_phone（下单人手机）和 status（订单状态）
        // return customerMapper.getOrderCountByPhone(phone);
        
        // 临时返回默认值
        return 0;
    }

    /**
     * 获取客户绑定的所有老人姓名
     */
    private String getBoundElderlyNames(Integer customerId) {
        List<String> elderlyNames = customerMapper.getBoundElderlyNames(customerId);
        if (elderlyNames == null || elderlyNames.isEmpty()) {
            return "—";
        }
        return String.join("、", elderlyNames);
    }
}




