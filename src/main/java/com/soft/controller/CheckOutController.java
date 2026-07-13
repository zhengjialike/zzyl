package com.soft.controller;

import com.soft.dto.CheckOutPageDto;
import com.soft.dto.StepSubmitDto;
import com.soft.pojo.ApplyLog;
import com.soft.pojo.CheckOut;
import com.soft.pojo.Elderly;
import com.soft.pojo.Contract;
import com.soft.service.CheckOutService;
import com.soft.service.BillService;
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

/**
 * 退住管理 HTTP 接口。
 *
 * <p>退住流程共七步，涉及老人、合同、账单和床位等模块。控制器保持轻量，
 * 所有跨表状态变更由 CheckOutService/BillService 完成。</p>
 */
@RestController
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;
    @Autowired
    private BillService billService;

    /** 分页查询退住单，返回 list 和 total。 */
    @RequestMapping("/checkOutPage")
    public Map<String, Object> pageList(@RequestBody CheckOutPageDto dto) {
        return checkOutService.pageList(dto);
    }

    /** 发起第一步退住申请；申请人取自 Session。 */
    @RequestMapping("/startCheckOut")
    public Map<String, Object> startApply(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkOutService.startApply(dto, currentUserName(session));
    }

    /** 查询处于“在住”状态且没有办理中退住单的老人。 */
    @RequestMapping("/checkOutEligibleElders")
    public List<Elderly> eligibleElders(@RequestBody(required = false) Map<String, String> payload) {
        String keyword = payload == null ? null : payload.get("keyword");
        return checkOutService.queryEligibleElders(keyword);
    }

    /** 查询当前退住老人名下仍可解除的未生效/生效中合同。 */
    @RequestMapping("/checkOutContracts")
    public List<Contract> contracts(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryActiveContracts(payload.get("id"));
    }

    /** 提交第 2～7 步；业务层会阻止越级或重复办理。 */
    @RequestMapping("/submitCheckOutStep")
    public Map<String, Object> submitStep(@RequestBody StepSubmitDto dto, HttpSession session) {
        return checkOutService.submitStep(dto, currentUserName(session));
    }

    /** 查询退住主表，并聚合老人电话及本次关联合同。 */
    @RequestMapping("/checkOutDetail")
    public CheckOut detail(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryDetail(payload.get("id"));
    }

    /** 查询退住流程日志。 */
    @RequestMapping("/checkOutLogs")
    public List<ApplyLog> logs(@RequestBody Map<String, Integer> payload) {
        return checkOutService.queryLogs(payload.get("id"));
    }

    /** 审批通过前撤销退住单，并解除尚未正式失效的合同关联。 */
    @RequestMapping("/revokeCheckOut")
    public Map<String, Object> revoke(@RequestBody Map<String, Integer> payload, HttpSession session) {
        return checkOutService.revoke(payload.get("id"), currentUserName(session));
    }

    /** 按退住单关联老人查询并分类返回账单。 */
    @RequestMapping("/checkOutBills")
    public Map<String, Object> bills(@RequestBody Map<String, Integer> payload) {
        return billService.queryByCheckOutId(payload.get("id"));
    }

    /** 当前操作人只能从服务端登录态读取，避免请求伪造审批人。 */
    private String currentUserName(HttpSession session) {
        Object online = session.getAttribute("online");
        if (online instanceof UserLineDto dto) return dto.getUname();
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录状态已失效，请重新登录");
    }
}
