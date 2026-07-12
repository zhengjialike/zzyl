package com.soft.controller;

import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.Bed;
import com.soft.pojo.CheckIn;
import com.soft.service.CheckInService;
import com.soft.service.FamilyMemberService;
import com.soft.dto.UserLineDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class CheckInController {

    @Autowired
    private CheckInService checkInService;
    @Autowired
    private FamilyMemberService familyMemberService;

    @RequestMapping("/checkInPage")
    public Map<String, Object> pageList(@RequestBody CheckInPageDto dto) {
        return checkInService.pageList(dto);
    }

    @RequestMapping("/startCheckIn")
    public Map<String, Object> startApply(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkInService.startApply(dto, currentUserName(session));
    }

    @RequestMapping("/submitCheckInStep")
    public Map<String, Object> submitStep(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkInService.submitStep(dto, currentUserName(session));
    }

    @RequestMapping("/checkInDetail")
    public CheckIn detail(@RequestBody Map<String, Integer> payload) {
        return checkInService.queryDetail(payload.get("id"));
    }

    @RequestMapping("/checkInFamily")
    public List<com.soft.pojo.FamilyMember> family(@RequestBody Map<String, Integer> payload) {
        return familyMemberService.queryByCheckInId(payload.get("id"));
    }

    @RequestMapping("/checkInLogs")
    public List<ApplyLog> logs(@RequestBody Map<String, Integer> payload) {
        return checkInService.queryLogs(payload.get("id"));
    }

    @RequestMapping("/checkInAvailableBeds")
    public List<Bed> availableBeds() {
        return checkInService.queryAvailableBeds();
    }

    @RequestMapping("/revokeCheckIn")
    public Map<String, Object> revoke(@RequestBody Map<String, Integer> payload, HttpSession session) {
        return checkInService.revoke(payload.get("id"), currentUserName(session));
    }

    private String currentUserName(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online instanceof UserLineDto dto) return dto.getUname();
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录状态已失效，请重新登录");
    }
}
