package com.soft.controller;

import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckOut;
import com.soft.pojo.Elderly;
import com.soft.pojo.Contract;
import com.soft.service.CheckOutService;
import com.zzyl.service.BillService;
import com.soft.dto.UserLineDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;
    @Autowired
    private BillService billService;

    @RequestMapping("/checkOutPage")
    public Map<String, Object> pageList(@RequestBody CheckOutPageDto dto) {
        return checkOutService.pageList(dto);
    }

    @RequestMapping("/startCheckOut")
    public Map<String, Object> startApply(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkOutService.startApply(dto, currentUserName(session));
    }

    @RequestMapping("/checkOutEligibleElders")
    public List<Elderly> eligibleElders(@RequestBody(required = false) Map<String, String> payload) {
        String keyword = payload == null ? null : payload.get("keyword");
        return checkOutService.queryEligibleElders(keyword);
    }

    @RequestMapping("/checkOutContracts")
    public List<Contract> contracts(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryActiveContracts(payload.get("id"));
    }

    @RequestMapping("/submitCheckOutStep")
    public Map<String, Object> submitStep(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkOutService.submitStep(dto, currentUserName(session));
    }

    @RequestMapping("/checkOutDetail")
    public CheckOut detail(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryDetail(payload.get("id"));
    }

    @RequestMapping("/checkOutLogs")
    public List<ApplyLog> logs(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryLogs(payload.get("id"));
    }

    @RequestMapping("/revokeCheckOut")
    public Map<String, Object> revoke(@RequestBody Map<String, Integer> payload, HttpSession session) {
        return checkOutService.revoke(payload.get("id"), currentUserName(session));
    }

    @RequestMapping("/checkOutBills")
    public Map<String, Object> bills(@RequestBody Map<String, Integer> payload) {
        return billService.queryByCheckOutId(payload.get("id"));
    }

    private String currentUserName(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online instanceof UserLineDto dto) return dto.getUname();
        return "未知";
    }
}
