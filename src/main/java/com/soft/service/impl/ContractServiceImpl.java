package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.ContractPageDto;
import com.soft.mapper.ContractMapper;
import com.soft.mapper.CheckInMapper;
import com.soft.mapper.CheckOutMapper;
import com.soft.pojo.Contract;
import com.soft.pojo.CheckIn;
import com.soft.pojo.CheckOut;
import com.soft.pojo.ApplyLog;
import com.soft.service.ApplyLogService;
import com.soft.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired private CheckInMapper checkInMapper;
    @Autowired private CheckOutMapper checkOutMapper;
    @Autowired private ApplyLogService applyLogService;

    /**
     * 合同状态由当前日期实时推导；已失效是退住业务状态，不能被日期计算覆盖。
     * 这样即使服务长期运行，列表中的未生效/生效中/已过期也不会过时。
     */
    private void refreshContractStatuses() {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.ne("status", "已失效");
        LocalDate today = LocalDate.now();
        for (Contract contract : contractMapper.selectList(wrapper)) {
            String status;
            if (contract.getStartDate() == null || today.isBefore(contract.getStartDate())) status = "未生效";
            else if (contract.getEndDate() != null && today.isAfter(contract.getEndDate())) status = "已过期";
            else status = "生效中";
            if (!status.equals(contract.getStatus())) {
                contract.setStatus(status);
                contractMapper.updateById(contract);
            }
        }
    }

    @Override
    public Map<String, Object> pageList(ContractPageDto dto) {
        refreshContractStatuses();
        Page<Contract> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        // 入住配置阶段预生成合同编号，但签约完成前不进入合同跟踪列表。
        wrapper.and(item -> item.isNull("remark").or().ne("remark", "入住签约待完成"));
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
    public Map<String, Object> queryDetail(Integer id) {
        refreshContractStatuses();
        Contract contract = contractMapper.selectById(id);
        if (contract == null) return Map.of();

        Map<String, Object> detail = new HashMap<>();
        detail.put("id", contract.getId());
        detail.put("contractNo", contract.getContractNo());
        detail.put("contractName", contract.getContractName());
        detail.put("elderName", contract.getElderName());
        detail.put("idCard", contract.getIdCard());
        detail.put("startDate", contract.getStartDate());
        detail.put("endDate", contract.getEndDate());
        detail.put("status", contract.getStatus());
        detail.put("creator", contract.getCreator());
        detail.put("createTime", contract.getCreateTime());
        detail.put("invalidTime", contract.getInvalidTime());

        // 签约资料保存在入住单中，详情接口聚合返回，避免复制数据到合同表。
        if (contract.getCheckInId() != null) {
            CheckIn checkIn = checkInMapper.selectById(contract.getCheckInId());
            if (checkIn != null) {
                detail.put("checkInBillNo", checkIn.getBillNo());
                detail.put("signDate", checkIn.getSignDate());
                detail.put("contractFile", checkIn.getContractFile());
                detail.put("partyCName", checkIn.getPartyCName());
                detail.put("partyCPhone", checkIn.getPartyCPhone());
            }
        }

        // 只有退住审批通过并使合同失效后，才返回解除记录。
        if (contract.getCheckOutId() != null && "已失效".equals(contract.getStatus())) {
            CheckOut checkOut = checkOutMapper.selectById(contract.getCheckOutId());
            if (checkOut != null) {
                detail.put("terminateDate", checkOut.getTerminateDate());
                detail.put("terminateAgreement", checkOut.getTerminateAgreement());
                List<ApplyLog> logs = applyLogService.queryByApply("退住", checkOut.getId());
                logs.stream().filter(log -> "解除合同".equals(log.getStepName())).findFirst()
                        .ifPresent(log -> detail.put("terminateSubmitter", log.getOperator()));
            }
        }
        return detail;
    }
}
