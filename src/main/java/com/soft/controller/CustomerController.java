package com.soft.controller;

import com.soft.dto.Customer.CustomerQueryDto;
import com.soft.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户信息分页查询
     */
    @PostMapping("/customerPage")
    public Map<String, Object> customerPageList(@RequestBody CustomerQueryDto dto) {
        return customerService.queryCustomerList(dto);
    }
}