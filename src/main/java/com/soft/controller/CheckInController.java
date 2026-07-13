package com.soft.controller;

import com.soft.dto.CheckInPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.Bed;
import com.soft.pojo.CheckIn;
import com.soft.service.CheckInService;
import com.soft.service.FamilyMemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * 入住管理 HTTP 接口。
 *
 * <p>控制器只负责接收页面参数、取得当前登录人并转交业务层。入住五步流程的节点校验、
 * 床位占用、老人状态和合同生成都由 {@link CheckInService} 在事务中处理。</p>
 */
@RestController
public class CheckInController {

    @Autowired
    private CheckInService checkInService;
    @Autowired
    private FamilyMemberService familyMemberService;

    /** 按单号、老人信息和入住期限分页查询入住单。 */
    @RequestMapping("/checkInPage")
    public Map<String, Object> pageList(@RequestBody CheckInPageDto dto) {
        return checkInService.pageList(dto);
    }

    /** 创建入住申请并完成第一步“申请入住”；操作人取自服务端 Session。 */
    @RequestMapping("/startCheckIn")
    public Map<String, Object> startApply(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkInService.startApply(dto, currentUserName(session));
    }

    /** 提交当前入住节点；dto.step 必须与数据库 currentStep 完全一致。 */
    @RequestMapping("/submitCheckInStep")
    public Map<String, Object> submitStep(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkInService.submitStep(dto, currentUserName(session));
    }

    /** 查询入住主表详情，业务层会额外回填预生成的合同编号。 */
    @RequestMapping("/checkInDetail")
    public CheckIn detail(@RequestBody Map<String, Integer> payload) {
        return checkInService.queryDetail(payload.get("id"));
    }

    /** 家属信息独立存表，因此使用入住单 id 单独查询。 */
    @RequestMapping("/checkInFamily")
    public List<com.soft.pojo.FamilyMember> family(@RequestBody Map<String, Integer> payload) {
        return familyMemberService.queryByCheckInId(payload.get("id"));
    }

    /** 查询五步流程操作轨迹，供详情页时间线展示。 */
    @RequestMapping("/checkInLogs")
    public List<ApplyLog> logs(@RequestBody Map<String, Integer> payload) {
        return checkInService.queryLogs(payload.get("id"));
    }

    /** 查询可选空闲床位；最终绑定时业务层还会再次校验，防止并发抢占。 */
    @RequestMapping("/checkInAvailableBeds")
    public List<Bed> availableBeds() {
        return checkInService.queryAvailableBeds();
    }

    /** 撤销入住单，并释放已占床位、删除尚未完成的预生成合同。 */
    @RequestMapping("/revokeCheckIn")
    public Map<String, Object> revoke(@RequestBody Map<String, Integer> payload, HttpSession session) {
        return checkInService.revoke(payload.get("id"), currentUserName(session));
    }

    /**
     * 从登录 Session 取得真实操作人，保证申请和审批日志可追溯。
     * 未登录时返回 401，不使用默认用户名生成无责任人的记录。
     */
    private String currentUserName(HttpSession session) {
        Object realName = session.getAttribute("realName");
        if (realName instanceof String name && !name.isBlank()) return name;
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录状态已失效，请重新登录");
    }
}
