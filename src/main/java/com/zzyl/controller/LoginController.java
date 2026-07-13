package com.zzyl.controller;

import com.zzyl.common.Result;
import com.zzyl.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private HttpSession session;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        Map<String, Object> result = loginService.login(params.get("account"), params.get("upwd"));
        if ((int) result.get("code") == 200) {
            return Result.success(result);
        }
        return Result.error((String) result.get("msg"));
    }

    @GetMapping("/loadInfo")
    public Result<Map<String, Object>> loadInfo() {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Result.error(401, "未登录");
        return Result.success(loginService.loadInfo(userId));
    }

    @GetMapping("/logout")
    public Result<Void> logout() {
        loginService.logout();
        return Result.success();
    }
}
