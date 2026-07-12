package com.soft.controller;

import com.soft.dto.ContractPageDto;
import com.soft.pojo.Contract;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    @RequestMapping("/contractPage")
    public Map<String, Object> pageList(@RequestBody ContractPageDto dto) {
        return contractService.pageList(dto);
    }

    @RequestMapping("/contractDetail")
    public Contract detail(@RequestBody Map<String, Integer> payload) {
        return contractService.queryDetail(payload.get("id"));
    }
}