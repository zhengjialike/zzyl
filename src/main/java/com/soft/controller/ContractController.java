package com.soft.controller;

import com.soft.dto.ContractPageDto;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 合同管理查询接口。
 *
 * <p>合同由入住流程创建，由退住流程置为失效；本控制器不提供直接增删改接口，
 * 避免绕过入住、退住流程破坏合同与业务单据之间的关系。</p>
 */
@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    /** 按合同编号、老人、状态和期限分页查询已进入跟踪阶段的合同。 */
    @RequestMapping("/contractPage")
    public Map<String, Object> pageList(@RequestBody ContractPageDto dto) {
        return contractService.pageList(dto);
    }

    /** 查询合同主表，并聚合入住签约资料和退住解除资料。 */
    @RequestMapping("/contractDetail")
    public Map<String, Object> detail(@RequestBody Map<String, Integer> payload) {
        return contractService.queryDetail(payload.get("id"));
    }
}
